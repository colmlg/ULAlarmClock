package legear.colm.ulalarmclock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SetNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ULAlarm", "Received Set Notification Broadcast");
        int id = intent.getIntExtra("id",0) + 5000; //+5000 for the notification alarm, as we dont want to interfere with repeating alarms

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Create the notification, when the notification is clicked the alarm will be cancelled
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Are you awake?");
        builder.setContentText("Click this notification to stop alarm");
        builder.setSmallIcon(R.drawable.ic_alarm_black_24dp);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent notifyIntent = new Intent(context, StopAlarmService.class);
        notifyIntent.putExtra("id", id);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getService(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, builder.build());

        //Create the alarm that will go off in 5 minutes
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("id", id);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("ULAlarm", "Started notification alarm " + id);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, alarmPendingIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, alarmPendingIntent);
        Log.d("ULAlarm", "Alarm will go off in : " + 30000 + "ms");
    }


}
