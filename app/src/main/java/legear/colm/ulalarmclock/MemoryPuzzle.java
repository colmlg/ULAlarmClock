package legear.colm.ulalarmclock;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import static android.R.color.holo_blue_dark;

public class MemoryPuzzle extends AppCompatActivity {

     String answer = "";
    private int objectLength = 4; //array of objects(buttons)
    private int [][] patterns = {{1,0,3,2}, {1,3,2,0}, {3,0,2,1}, {0,2,3,1}, {1,0,3,2}, {1,3,2,0}, {0,3,1,2}, {3,1,2,0}}; //pattern of buttons to copy
    int [] pattern;
    String correctPattern;
    ImageButton imageButtons[] = new ImageButton[objectLength];
    int i;
    int counter;
    int j;
    int textViewCounter;
    String remember = "Remeber the Sequence";
    String recall = "Repeat the Sequence";
    TextView tv;
    int timer =1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_puzzle);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        tv = (TextView) findViewById(R.id.textView3);
        pattern = patterns[(int)(Math.random() * (patterns.length - 1))];

        correctPattern = getCorrectPattern(pattern);
        imageButtons[0] = (ImageButton)findViewById(R.id.imageButton);
        imageButtons[1] = (ImageButton)findViewById(R.id.imageButton3);
        imageButtons[2] = (ImageButton)findViewById(R.id.imageButton4);
        imageButtons[3] = (ImageButton)findViewById(R.id.imageButton5);
        tv.setText("Remember");
        setColorChange();


        for(j = 0 ; j<imageButtons.length; j++ ) {
            imageButtons[j].setOnClickListener(new View.OnClickListener() {
                int index = j;

                @Override
                public void onClick(View v) {
                    answer += index;
                    Log.d("Alarm", answer);
                    if (answer.length() == correctPattern.length()) {
                        if (answer.equals(correctPattern)) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            pattern = patterns[(int)(Math.random() * (patterns.length - 1))];
                            correctPattern = getCorrectPattern(pattern);
                            answer = "";
                            tv.setText("Remember");
                            setColorChange();
                        }
                    }

                }

            });
        }}

    private void setColorChange() {
        textViewCounter = 0;
        timer = 1000;
        Log.d("ULAlarm", correctPattern);
        for (counter=0 ; counter < pattern.length; counter++) {
            int tempCounter = counter;
            final AnimationDrawable drawable = new AnimationDrawable();
            final Handler handler = new Handler();
            drawable.addFrame(new ColorDrawable(getResources().getColor(holo_blue_dark)), 0);
            drawable.addFrame(new ColorDrawable(Color.RED), 200);
            drawable.addFrame(new ColorDrawable(getResources().getColor(holo_blue_dark)), 100);
            drawable.setOneShot(true);

            imageButtons[pattern[tempCounter]].setBackgroundDrawable(drawable);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawable.start();
                    textViewCounter ++;
                    if(textViewCounter >= 4)
                    {
                        tv.setText("Recall");
                    }

                }
            }, timer);

            timer += 1000;
        }

    }

    private String getCorrectPattern(int [] pattern)
    {
        String correctPattern = "";
        for(int i : pattern)
        {
            correctPattern += i;
        }
        return correctPattern;
    }
}