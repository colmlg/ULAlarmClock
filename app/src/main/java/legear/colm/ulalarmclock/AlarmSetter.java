package legear.colm.ulalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by colml on 26/04/2017.
 * Sets an alarm clock.
 */

class AlarmSetter {
    private AlarmManager alarmManager;
    private Context context;

    AlarmSetter(Context context)
    {
        this.context = context;
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(Alarm alarm)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("id", alarm.getId());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar currentTime = new GregorianCalendar();
        alarm.getCalendar().set(Calendar.SECOND, 0);

        if(!alarm.isRepeating())
        {
            while(alarm.getCalendar().getTimeInMillis() < currentTime.getTimeInMillis())
            {
                alarm.getCalendar().add(Calendar.DAY_OF_YEAR, 1);
            }
            Date date = new Date(alarm.getCalendar().getTimeInMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
            Log.d("ULAlarm", "Alarm " + alarm.getId() + " set for " + sdf.format(date));
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getCalendar().getTimeInMillis(), alarmIntent);
            long timeDiff = alarm.getCalendar().getTimeInMillis() - currentTime.getTimeInMillis();
            Toast.makeText(context, "Alarm will go off in " + TimeUnit.MILLISECONDS.toHours(timeDiff) + " hours " + TimeUnit.MILLISECONDS.toMinutes(timeDiff) % TimeUnit.HOURS.toMinutes(1) + " minutes.",LENGTH_LONG).show();
        }
        else {
            int [] repeatDays = alarm.getRepeatDays();
            long timeDiff;
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
            Toast.makeText(context, "Alarm will go off in " + TimeUnit.MILLISECONDS.toHours(smallestTimeDiff) + " hours " + TimeUnit.MILLISECONDS.toMinutes(smallestTimeDiff) % TimeUnit.HOURS.toMinutes(1) + " minutes.", LENGTH_LONG).show();
        }
        Log.d("ULAlarm" , "Set alarm " + alarm.getId());
    }
}
