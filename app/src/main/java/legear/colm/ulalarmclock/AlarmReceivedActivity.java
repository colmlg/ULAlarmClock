package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class AlarmReceivedActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private boolean finishedPuzzles = false;
    private Alarm alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_received);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        Log.d("ULAlarm", "Started activity for alarm " + id);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        alarm = db.getAlarm(id);
        String [] puzzles = alarm.getPuzzles().split(",");

        playSound(this);

        Intent puzzleIntent = new Intent();

        if(puzzles.length > 0)
        {
            for(String s : puzzles)
            {
                if(!s.equals("")) {
                    switch (Integer.parseInt(s)) {
                        case 0:
                            puzzleIntent = new Intent(getApplicationContext(), MathPuzzle.class);
                            break;
                        case 1:
                            puzzleIntent = new Intent(getApplicationContext(), MemoryPuzzle.class);
                            break;
                        case 2:
                            puzzleIntent = new Intent(getApplicationContext(), PasswordPuzzle.class);
                            break;
                        case 3:
                            setNotification();

                    }

                    if(!s.equals("3"))
                        startActivityForResult(puzzleIntent, 2);

                }
            }

            finishedPuzzles = true;

        }


        Button stopButton = (Button) findViewById(R.id.button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                setResult(Activity.RESULT_OK);
                finish();
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
        if(resultCode == Activity.RESULT_OK && finishedPuzzles){
            mMediaPlayer.stop();
            finish();
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
}
