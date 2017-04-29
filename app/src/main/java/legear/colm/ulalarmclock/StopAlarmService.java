package legear.colm.ulalarmclock;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StopAlarmService extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public StopAlarmService() {
        super("StopAlarmService");
        Log.d("ULAlarm", "Stop alarm service created.");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("ULAlarm", "Started stop alarm service.");
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int id = intent.getIntExtra("id", 0);


        Intent intentFirst = new Intent(getApplicationContext(), AlarmReceiver.class);
        intentFirst.putExtra("id", id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intentFirst, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);


        Log.d("ULAlarm", "Service Cancelling alarm " + id);
        stopSelf();
    }
}