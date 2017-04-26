package legear.colm.ulalarmclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AlarmReceivedActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private boolean finishedPuzzles = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_received);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Alarm alarm = db.getAlarm(id);
        String [] puzzles = alarm.getPuzzles().split(",");

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(alarm.getTime());
        playSound(this);

        Intent puzzleIntent = new Intent();

        if(puzzles.length > 0)
        {
            for(String s : puzzles)
            {
                switch(Integer.parseInt(s)) {
                    case 0:
                         puzzleIntent = new Intent(getApplicationContext(), MathPuzzle.class);
                         break;
                    case 1:
                        puzzleIntent = new Intent(getApplicationContext(), MemoryPuzzle.class);
                        break;
                }

                startActivityForResult(puzzleIntent, 2);
            }

            finishedPuzzles = true;

        }


        Button stopButton = (Button) findViewById(R.id.button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                finish();
            }
        });

    }

    private void playSound(Context context) {
        mMediaPlayer = new MediaPlayer();
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
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
            System.out.println("ERROR PLAYING ALARM SOUND");
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
}
