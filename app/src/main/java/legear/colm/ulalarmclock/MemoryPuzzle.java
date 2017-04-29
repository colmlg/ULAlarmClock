package legear.colm.ulalarmclock;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MemoryPuzzle extends AppCompatActivity {

    final ArrayList<Integer> answer = new ArrayList<Integer>();
  private int objectLength = 4; //array of elements(buttons)
    private int [] pattern = {1,4,3,2}; //pattern of buttons to copy
    ImageButton imageButtons[] = new ImageButton[objectLength];
    int i;
    String remember = "Remeber the Sequence";
    String recall = "Repeat the Sequence";
    TextView tv = (TextView) findViewById(R.id.textView3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_puzzle);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        imageButtons[0] = (ImageButton)findViewById(R.id.imageButton);
        imageButtons[1] = (ImageButton)findViewById(R.id.imageButton3);
        imageButtons[2] = (ImageButton)findViewById(R.id.imageButton4);
        imageButtons[3] = (ImageButton)findViewById(R.id.imageButton5);
         }


         private void setColorChange() {
             for (i=0 ; i <= pattern.length; i++) {
                 CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {

                     @Override
                     public void onTick(long millisUntilFinished) {
                         tv.setText(remember);
                         imageButtons[i].setBackgroundColor(Color.RED);
                     }

                     @Override
                     public void onFinish() {
                         imageButtons[i].setBackgroundColor(Color.BLUE);
                     }
                 }.start();
             }
         }


         private void getAnswer(){
             for(int j = 0 ; j<=imageButtons.length; j++ ){
                 imageButtons[j].setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         //answer.add(/*the button they tap*/);
                     }
                 });
             }

         }
}