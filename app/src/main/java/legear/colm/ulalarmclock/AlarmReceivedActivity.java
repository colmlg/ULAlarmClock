package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * Activity that is started by the alarm broadcast receiver.
 * Plays sound and starts the puzzle activities.
 */
public class AlarmReceivedActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private Alarm alarm;
    private boolean notificationPuzzle;
    private int globalRequestCode = 1;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_received);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Log.d("ULAlarm", "Started activity for alarm " + id);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        if(id > 5000)//If this is a notification alarm
            id = id - 5000;

        alarm = db.getAlarm(id);

        String [] puzzles = alarm.getPuzzles().split(",");

        playSound(this);

        Intent puzzleIntent = new Intent();
        notificationPuzzle = false;
        if(puzzles.length > 0)
        {
            for(String s : puzzles)
            {
                if(!s.equals("")) {
                    switch (Integer.parseInt(s)) {
                        case 0:
                            puzzleIntent = new Intent(getApplicationContext(), MathPuzzle.class);
                            globalRequestCode++;
                            break;
                        case 1:
                            puzzleIntent = new Intent(getApplicationContext(), MemoryPuzzle.class);
                            globalRequestCode++;
                            break;
                        case 2:
                            puzzleIntent = new Intent(getApplicationContext(), PasswordPuzzle.class);
                            globalRequestCode++;
                            break;
                        case 3:
                            notificationPuzzle = true;

                    }

                    if(!s.equals("3"))
                        startActivityForResult(puzzleIntent, globalRequestCode);

                }
            }


        }


        Button stopButton = (Button) findViewById(R.id.button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(notificationPuzzle)
                   setNotification();
                stopActivity();
            }
        });

    }

    private void playSound(Context context) {
        mMediaPlayer = new MediaPlayer();
        Uri alert = alarm.getUri();


        try {
            mMediaPlayer.setDataSource(context, alert);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode >= globalRequestCode){
            if(notificationPuzzle)
            {
                setNotification();
                notificationPuzzle = false;
            }


            stopActivity();
        }
        if (resultCode == Activity.RESULT_CANCELED) {

        }

    }//onActivityResult

    @Override
    public void onBackPressed() {
    }

    public void setNotification()
    {
        //Notification will be set in 5 minutes
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, SetNotificationReceiver.class);
        intent.putExtra("id", alarm.getId());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, alarm.getId() + 2000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, alarmIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, alarmIntent);
        Log.d("ULAlarm", "Notification will go off in : " + 30000 + "ms");
    }

    private void stopActivity()
    {
        mMediaPlayer.stop();
        setResult(Activity.RESULT_OK);
        finish();
    }
}
