package legear.colm.ulalarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MathPuzzle extends AppCompatActivity {
    String [] puzzle = {"1 + 2 = " +
            "3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_puzzle);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());



        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(puzzle[0]);

        Button myButton = (Button) findViewById(R.id.button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.editText);
                String x = et.getText().toString();
                if(x.equals(puzzle[1])) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}
