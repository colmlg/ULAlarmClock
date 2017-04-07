package legear.colm.ulalarmclock;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
                int [] repeatDays = new int[7];
                Alarm alarm = new Alarm();
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());


                CheckBox checkBoxMon = (CheckBox) findViewById(R.id.checkBoxMon);
                if (checkBoxMon.isChecked())
                   repeatDays[0] = 1;

                else
                    repeatDays[0] = 0;

                CheckBox checkBoxTue = (CheckBox) findViewById(R.id.checkBoxTue);
                if (checkBoxTue.isChecked())
                    repeatDays[1] = 1;
                else
                    repeatDays[1] = 0;

                CheckBox checkBoxWed = (CheckBox) findViewById(R.id.checkBoxWed);
                if (checkBoxWed.isChecked())
                    repeatDays[2] = 1;
                else
                    repeatDays[2] = 0;

                CheckBox checkBoxThu = (CheckBox) findViewById(R.id.checkBoxThu);
                if (checkBoxThu.isChecked())
                    repeatDays[3] = 1;
                else
                    repeatDays[3] = 0;

                CheckBox checkBoxFri = (CheckBox) findViewById(R.id.checkBoxFri);
                if (checkBoxFri.isChecked())
                    repeatDays[4] = 1;
                else
                    repeatDays[4] = 0;

                CheckBox checkBoxSat = (CheckBox) findViewById(R.id.checkBoxSat);
                if (checkBoxSat.isChecked())
                    repeatDays[5] = 1;
                else
                    repeatDays[5] = 0;

                CheckBox checkBoxSun = (CheckBox) findViewById(R.id.checkBoxSun);
                if (checkBoxSun.isChecked())
                    repeatDays[6] = 1;
                else
                    repeatDays[6] = 0;

                alarm.setTime(hour, minute);
                alarm.setRepeatDays(repeatDays);
                db.addAlarm(alarm);
                alarm.setId(db.getLastInsertId());

                Intent returnIntent = new Intent();
                returnIntent.putExtra("id", alarm.getId());

                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }


            }
        );


    }

}
