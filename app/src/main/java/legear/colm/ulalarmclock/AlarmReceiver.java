package legear.colm.ulalarmclock;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.support.v4.content.ContextCompat.startActivity;


/**
 * Created by colml on 04/04/2017.
 * Receives the pending intent when an alarm goes off and starts the alarm received activity.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id",0);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Log.d("ULAlarm", "Received alarm " + id + " at " + sdf.format(date));

        DatabaseHandler db = new DatabaseHandler(context);

        if(id < 5000 && !db.getAlarm(id).isRepeating())
        {
            db.toggleAlarmActive(id, false);
        }
        if(id > 5000)
        {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(id);
        }
        else
            Log.d("ULAlarm", "Difference: " + (System.currentTimeMillis() - db.getAlarm(id).getCalendar().getTimeInMillis()) + "ms");



        Intent alarmIntent = new Intent(context, AlarmReceivedActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("id", id);
        startActivity(context, alarmIntent, null);


    };


}
