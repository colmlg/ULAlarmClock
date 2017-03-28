package legear.colm.ulalarmclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddAlarm extends AppCompatActivity {
    private int hour;
    private int minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);


        Button okButton = (Button) findViewById(R.id.button2);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker picker = (TimePicker) findViewById(R.id.timePicker2);
                hour = picker.getHour();
                minute = picker.getMinute();
                showToast();

            }
        });
    }

    private void showToast(){
        String text = "Hour: " + hour + "   Minute: " + minute;
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        //toast.show();
        finish();



    }
}
