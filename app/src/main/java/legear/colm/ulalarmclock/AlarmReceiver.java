package legear.colm.ulalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static java.security.AccessController.getContext;

/**
 * Created by colml on 04/04/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id",0);
        Toast toast = Toast.makeText(context,"Alarm " + id + " received!", Toast.LENGTH_LONG);
        toast.show();

        Intent intent1 = new Intent(context, AddAlarm.class);
        context.startService(intent1);
    };


}
