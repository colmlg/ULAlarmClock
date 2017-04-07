package legear.colm.ulalarmclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by colml on 03/04/2017.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm>{


    public AlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, 0, alarms);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Alarm alarm = getItem(position);
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
                DatabaseHandler db = new DatabaseHandler(getContext());

                //Check if the view is shown, as we are re-using views in the listview
                if(buttonView.isShown()) {
                    if (isChecked) {
                        Toast toast = Toast.makeText(getContext(), "Alarm " + alarm.getId() + " enabled!", Toast.LENGTH_SHORT);
                        toast.show();
                        alarm.setEnabled(true);
                        db.toggleAlarmActive(alarm.getId(), true);
                    }


                    else {
                        Toast toast = Toast.makeText(getContext(), "Alarm " + alarm.getId() + " disabled!", Toast.LENGTH_SHORT);
                        toast.show();
                        alarm.setEnabled(false);
                        db.toggleAlarmActive(alarm.getId(), false);
                    }
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), "Clicked " + position, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        // Populate the data into the template view using the data object
        tvTime.setText(alarm.getTime());
        tvRepeat.setText(alarm.getRepeatString());
        // Return the completed view to render on screen
        return convertView;
    }
}



