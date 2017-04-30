package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import static android.content.Context.ALARM_SERVICE;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by colml on 03/04/2017.
 * Adapter for the listview.
 */

class AlarmAdapter extends ArrayAdapter<Alarm>{

    private Context context;
    private PendingIntent alarmIntent;
    private AlarmManager alarmManager;


    AlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, 0, alarms);
        this.context = context;
        //Set up the alarm manager
        alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);

    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @Nullable ViewGroup  parent) {
        final DatabaseHandler db = new DatabaseHandler(getContext());
        // Get the data item for this position
        final Alarm alarm = db.getAlarm(getItem(position).getId());
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
        }
        // Lookup view for data population
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvRepeat = (TextView) convertView.findViewById(R.id.tvRepeat);
        Switch alarmSwitch = (Switch) convertView.findViewById(R.id.switch1);
        alarmSwitch.setChecked(alarm.isEnabled());


        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                //Check if the view is shown, as we are re-using views in the listview
                if(buttonView.isShown()) {
                    if (isChecked) {
                        alarm.setEnabled(true);
                        db.toggleAlarmActive(alarm.getId(), true);
                        AlarmSetter setter = new AlarmSetter(getContext());
                        setter.setAlarm(alarm);
                        Log.d("COLMLG", "Set Alarm : " + alarm.getId());

                    }


                    else {
                        Intent intent = new Intent(getContext(), AlarmReceiver.class);
                        intent.putExtra("id", alarm.getId());
                        alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                        alarmIntent = PendingIntent.getBroadcast(getContext(), alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.cancel(alarmIntent);
                        alarmIntent.cancel();
                        db.toggleAlarmActive(alarm.getId(), false);
                    }
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAlarm = new Intent(getContext(), CreateAlarm.class);
                addAlarm.putExtra("id", alarm.getId());
                Activity mainActivity = (Activity) context;
                startActivityForResult(mainActivity, addAlarm, 1, null);

            }
        });
        // Populate the data into the template view using the data object
        tvTime.setText(alarm.getTime());
        tvRepeat.setText(alarm.getRepeatString() + "\n" + alarm.getPuzzleString());
        // Return the completed view to render on screen
        return convertView;
    }

}



