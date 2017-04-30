package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.Calendar;

/**
 * Activity to create and edit alarms.
 */
public class CreateAlarm extends AppCompatActivity {

    private AlarmManager alarmManager;
    Context toneContext;
    Uri ringtoneUri;
    TextView ringToneName;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Create Alarm");

        setContentView(R.layout.activity_create_alarm);
        toneContext = this;
        
        //Set up the alarm manager
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        ringToneName = (TextView) findViewById(R.id.textViewRingtoneName);
        ringToneName.setText(RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)).getTitle(this).replace("Default ringtone (", "").replace(")",""));

        TextView changeButton = (TextView) findViewById(R.id.textViewChangeButton);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for alarm:");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent,999);
            }
        };

        changeButton.setOnClickListener(listener);
        ringToneName.setOnClickListener(listener);


        //If the alarm already exists
        if(id > 0)
        {
            getSupportActionBar().setTitle("Edit Alarm");
            Alarm alarm = db.getAlarm(id);
            int [] repeatDays = alarm.getRepeatDays();
            String puzzles = alarm.getPuzzles();
            ((CustomTimePicker) findViewById(R.id.timePicker2)).setHour(alarm.getCalendar().get(Calendar.HOUR_OF_DAY));
            ((CustomTimePicker) findViewById(R.id.timePicker2)).setMinute(alarm.getCalendar().get(Calendar.MINUTE));
            ((CheckBox) findViewById(R.id.checkBoxMon)).setChecked(repeatDays[0] == 1);
            ((CheckBox) findViewById(R.id.checkBoxTue)).setChecked(repeatDays[1] == 1);
            ((CheckBox) findViewById(R.id.checkBoxWed)).setChecked(repeatDays[2] == 1);
            ((CheckBox) findViewById(R.id.checkBoxThu)).setChecked(repeatDays[3] == 1);
            ((CheckBox) findViewById(R.id.checkBoxFri)).setChecked(repeatDays[4] == 1);
            ((CheckBox) findViewById(R.id.checkBoxSat)).setChecked(repeatDays[5] == 1);
            ((CheckBox) findViewById(R.id.checkBoxSun)).setChecked(repeatDays[6] == 1);
            ((CheckBox) findViewById(R.id.checkBoxMathPuzzle)).setChecked(puzzles.contains("0"));
            ((CheckBox) findViewById(R.id.checkBoxMemoryPuzzle)).setChecked(puzzles.contains("1"));
            ((CheckBox) findViewById(R.id.checkBoxPassword)).setChecked(puzzles.contains("2"));
            ((CheckBox) findViewById(R.id.checkBoxNotification)).setChecked(puzzles.contains("3"));
            ringToneName.setText(RingtoneManager.getRingtone(this, alarm.getUri()).getTitle(this).replace("Default ringtone (", "").replace(")",""));

        }



    }

    private void saveAlarm()
    {
        CustomTimePicker picker = (CustomTimePicker) findViewById(R.id.timePicker2);
        int hour = picker.getHour();
        int minute = picker.getMinute();
        int [] repeatDays = new int[7];
        Alarm alarm = new Alarm();
        String puzzles = "";
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        if(id > 0)
        {
            alarm = db.getAlarm(id);
            alarm.setEnabled(true);
        }

        if(ringtoneUri != null)
        {
            alarm.setUri(ringtoneUri);
        }
        else{
            alarm.setUri(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            if(alarm.getUri() == null)
                alarm.setUri(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        }

        //REPEATING DAYS CHECKBOXES
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

        //PUZZLES CHECKBOXES
        checkBox = (CheckBox) findViewById(R.id.checkBoxMathPuzzle);
        puzzles = checkBox.isChecked() ? puzzles += "0," : puzzles;
        checkBox = (CheckBox) findViewById(R.id.checkBoxMemoryPuzzle);
        puzzles = checkBox.isChecked() ? puzzles += "1," : puzzles;
        checkBox = (CheckBox) findViewById(R.id.checkBoxPassword);
        puzzles = checkBox.isChecked() ? puzzles += "2," : puzzles;
        checkBox = (CheckBox) findViewById(R.id.checkBoxNotification);
        puzzles = checkBox.isChecked() ? puzzles += "3," : puzzles;

        alarm.setTime(hour, minute);
        alarm.setRepeatDays(repeatDays);
        alarm.setPuzzles(puzzles);

        if(id > 0)
        {
            db.updateAlarm(alarm);
        }
        else {
            db.addAlarm(alarm);
            alarm.setId(db.getLastInsertId());
        }

        AlarmSetter setter = new AlarmSetter(getApplicationContext());
        setter.setAlarm(alarm);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        getMenuInflater().inflate(R.menu.menu_create_alarm, menu);

        //If the alarm already exists
        if(id > 0)
        {
            MenuItem deleteAction = menu.findItem(R.id.action_delete_alarm);
            deleteAction.setVisible(true);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_alarm) {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            int id = getIntent().getIntExtra("id", 0);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            intent.putExtra("id", id);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            db.deleteAlarm(id);
            setResult(Activity.RESULT_OK);
            finish();
        }

        else if(item.getItemId() == R.id.action_save){
            saveAlarm();
            setResult(Activity.RESULT_OK);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringToneName.setText(RingtoneManager.getRingtone(this, ringtoneUri).getTitle(this));

        }
    }

}


