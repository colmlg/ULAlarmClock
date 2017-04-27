package legear.colm.ulalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by colml on 27/04/2017.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHandler db = new DatabaseHandler(context);
        ArrayList<Alarm> listAlarms = db.getAllAlarms();
        AlarmSetter setter = new AlarmSetter(context);
        for(Alarm alarm : listAlarms)
        {
            if(alarm.isEnabled())
                setter.setAlarm(alarm);
        }
    }
}
