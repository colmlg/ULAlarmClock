package legear.colm.ulalarmclock;

import android.app.Activity;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PasswordPuzzle extends AppCompatActivity {
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_puzzle);

        final String [] passwords = {"mAp$q^D", "7mMC^q_r", "Fgr#K7sj", "^CjpmG9&#", "J4w#c$v2h", "w!56Cc?56",
                                "yA_j@ff6N", "6F@7ebNJ*", "%rM+*bA2F", "eG*4=Zkg$", "_a4V*EywV", "_SwwH%@3M",
                                "$2*T!=k=&", "xKs*F6KjV", "GwMd?AN3q", "_K*FGw8q=", "DH2ug**c$", "U?Tgv*2Xf"};

        password = passwords[(int)(Math.random() * (passwords.length - 1))];

        final TextView tv = (TextView) findViewById(R.id.textView5);
        tv.setText(password);

        Button myButton = (Button) findViewById(R.id.button4);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.editText2);
                String answer = et.getText().toString();

                if(answer.equals(password))
                {
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                else {
                    password = passwords[(int)(Math.random() * (passwords.length - 1))];
                    tv.setText(password);
                    et.setText("");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
    }
}
