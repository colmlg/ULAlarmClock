package legear.colm.ulalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by colml on 27/04/2017.
 * Sets alarms on device boot.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ULAlarmClock", "Started BootCompleteReceiver");
        DatabaseHandler db = new DatabaseHandler(context);
        ArrayList<Alarm> listAlarms = db.getAllAlarms();
        AlarmSetter setter = new AlarmSetter(context);
        for(Alarm alarm : listAlarms)
        {
            if(alarm.isEnabled())
            {
                setter.setAlarm(alarm);
            }

        }

    }
}
