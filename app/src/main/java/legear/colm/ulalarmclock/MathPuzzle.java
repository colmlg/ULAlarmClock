package legear.colm.ulalarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MathPuzzle extends AppCompatActivity {
    String [][] puzzles = {{"3 * 24 = ?", "72"}, {"27 + 7 = ?", "34"}, {"48 * 2 = ?", "96"}, {"128 - 42 - 3 = ?", "83"}};
    String [] puzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_puzzle);

        puzzle = puzzles[(int)(Math.random() * (puzzles.length - 1))];
        final TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(puzzle[0]);

        Button myButton = (Button) findViewById(R.id.button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = (EditText) findViewById(R.id.editText);
                String x = et.getText().toString();
                if(x.equals(puzzle[1])) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                else {
                    puzzle = puzzles[(int)(Math.random() * (puzzles.length - 1))];
                    tv.setText(puzzle[0]);
                    et.setText("");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
