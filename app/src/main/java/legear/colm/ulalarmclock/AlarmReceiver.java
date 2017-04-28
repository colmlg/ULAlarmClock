package legear.colm.ulalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.support.v4.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;

/**
 * Created by colml on 04/04/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id",0);
        DatabaseHandler db = new DatabaseHandler(context);

        if(!db.getAlarm(id).isRepeating())
        {
            db.toggleAlarmActive(id, false);
        }

        Intent alarmIntent = new Intent(context, AlarmReceivedActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("id", id);
        startActivity(context, alarmIntent, null);


    };


}
