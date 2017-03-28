package legear.colm.ulalarmclock;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

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
                Intent returnIntent = new Intent();
                returnIntent.putExtra("hour",hour);
                returnIntent.putExtra("minute",minute);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });
    }

}
