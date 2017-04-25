package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class CreateAlarm extends AppCompatActivity {
    private int hour;
    private int minute;
    private PendingIntent alarmIntent;
    private AlarmManager alarmManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        
        //Set up the alarm manager
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", 0);

        if(id > 0)
        {
            Alarm alarm = db.getAlarm(id);
            int [] repeatDays = alarm.getRepeatDays();

            ((TimePicker) findViewById(R.id.timePicker2)).setHour(alarm.getCalendar().get(Calendar.HOUR_OF_DAY));
            ((TimePicker) findViewById(R.id.timePicker2)).setMinute(alarm.getCalendar().get(Calendar.MINUTE));
            ((CheckBox) findViewById(R.id.checkBoxMon)).setChecked(repeatDays[0] == 1);
            ((CheckBox) findViewById(R.id.checkBoxTue)).setChecked(repeatDays[1] == 1);
            ((CheckBox) findViewById(R.id.checkBoxWed)).setChecked(repeatDays[2] == 1);
            ((CheckBox) findViewById(R.id.checkBoxThu)).setChecked(repeatDays[3] == 1);
            ((CheckBox) findViewById(R.id.checkBoxFri)).setChecked(repeatDays[4] == 1);
            ((CheckBox) findViewById(R.id.checkBoxSat)).setChecked(repeatDays[5] == 1);
            ((CheckBox) findViewById(R.id.checkBoxSun)).setChecked(repeatDays[6] == 1);

        }



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

                if(id > 0)
                {
                    alarm = db.getAlarm(id);
                    alarm.setEnabled(true);
                }


                CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxMon);
                repeatDays[0] = checkBox.isChecked() ? 1 : 0;

                checkBox = (CheckBox) findViewById(R.id.checkBoxTue);
                repeatDays[1] = checkBox.isChecked() ? 1 : 0;

                checkBox = (CheckBox) findViewById(R.id.checkBoxWed);
                repeatDays[2] = checkBox.isChecked() ? 1 : 0;

                checkBox = (CheckBox) findViewById(R.id.checkBoxThu);
                repeatDays[3] = checkBox.isChecked() ? 1 : 0;

                checkBox = (CheckBox) findViewById(R.id.checkBoxFri);
                repeatDays[4] = checkBox.isChecked() ? 1 : 0;

                checkBox = (CheckBox) findViewById(R.id.checkBoxSat);
                repeatDays[5] = checkBox.isChecked() ? 1 : 0;

                checkBox = (CheckBox) findViewById(R.id.checkBoxSun);
                repeatDays[6] = checkBox.isChecked() ? 1 : 0;

                alarm.setTime(hour, minute);
                alarm.setRepeatDays(repeatDays);

                if(id > 0)
                {
                    db.updateAlarm(alarm);
                }
                else {
                    db.addAlarm(alarm);
                    alarm.setId(db.getLastInsertId());
                    //Toast.makeText(getApplicationContext(), "Adding alarm " + alarm.getId() + " to the DB.",LENGTH_SHORT).show();

                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("id", alarm.getId());


                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("id", alarm.getId());
                alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Calendar currentTime = new GregorianCalendar();


                if(!alarm.isRepeating())
                {
                    while(alarm.getCalendar().getTimeInMillis() < currentTime.getTimeInMillis())
                    {
                        alarm.getCalendar().add(Calendar.DAY_OF_YEAR, 1);
                    }
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getCalendar().getTimeInMillis(), alarmIntent);
                    long timeDiff = alarm.getCalendar().getTimeInMillis() - currentTime.getTimeInMillis();
                    Toast.makeText(getApplicationContext(), "Alarm will go off in " + TimeUnit.MILLISECONDS.toHours(timeDiff) + " hours " + TimeUnit.MILLISECONDS.toMinutes(timeDiff) % TimeUnit.HOURS.toMinutes(1) + " minutes.",LENGTH_LONG).show();
                }

                else {
                    long timeDiff = 0;
                    GregorianCalendar alarmCalendar = new GregorianCalendar();
                    alarmCalendar.setTimeInMillis(System.currentTimeMillis());
                    boolean future = false;
                    long smallestTimeDiff = System.currentTimeMillis();

                    for (int i = 0; i < repeatDays.length; i++) {
                        if (repeatDays[i] == 1) {

                            alarmCalendar.set(Calendar.HOUR_OF_DAY,alarm.getCalendar().get(Calendar.HOUR_OF_DAY));
                            alarmCalendar.set(Calendar.MINUTE, alarm.getCalendar().get(Calendar.MINUTE));
                            alarmCalendar.set(Calendar.DAY_OF_WEEK, (i+2));
                            if(i == 6)
                                alarmCalendar.set(Calendar.DAY_OF_WEEK,1);


                            if(alarmCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                                alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                                future = true;
                            }


                            timeDiff = alarmCalendar.getTimeInMillis() - System.currentTimeMillis();
                            smallestTimeDiff = smallestTimeDiff > timeDiff ? timeDiff : smallestTimeDiff;

                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, alarmIntent);



                            if(future)
                            {
                                alarmCalendar.add(Calendar.WEEK_OF_YEAR, -1);
                                future = false;
                            }
                        }
                    }

                    Toast.makeText(getApplicationContext(), "Alarm will go off in " + TimeUnit.MILLISECONDS.toHours(smallestTimeDiff) + " hours " + TimeUnit.MILLISECONDS.toMinutes(smallestTimeDiff) % TimeUnit.HOURS.toMinutes(1) + " minutes.", LENGTH_LONG).show();


                }



                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }


            }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);


        //If the alarm already exists
        if(id > 0)
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_create_alarm, menu);
            return true;
        }

        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id1 = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id1 == R.id.action_delete_alarm) {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            int id = getIntent().getIntExtra("id", 0);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            intent.putExtra("id", id);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            db.deleteAlarm(id);

            setResult(Activity.RESULT_OK);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }





}
