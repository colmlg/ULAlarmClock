package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static android.support.v4.content.ContextCompat.startActivity;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by colml on 03/04/2017.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm>{

    private Context context;
    private PendingIntent alarmIntent;
    private AlarmManager alarmManager;


    public AlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, 0, alarms);
        this.context = context;
        //Set up the alarm manager
        alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);

    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
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
                        int [] repeatDays = alarm.getRepeatDays();
                        Intent intent = new Intent(getContext(), AlarmReceiver.class);
                        intent.putExtra("id", alarm.getId());
                        alarmIntent = PendingIntent.getBroadcast(getContext(), alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        Calendar currentTime = new GregorianCalendar();


                        if(!alarm.isRepeating())
                        {
                            while(alarm.getCalendar().getTimeInMillis() < currentTime.getTimeInMillis())
                            {
                                alarm.getCalendar().add(Calendar.DAY_OF_YEAR, 1);
                            }
                            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getCalendar().getTimeInMillis(), alarmIntent);
                            long timeDiff = alarm.getCalendar().getTimeInMillis() - currentTime.getTimeInMillis();
                            Toast.makeText(getContext(), "Alarm will go off in " + TimeUnit.MILLISECONDS.toHours(timeDiff) + " hours " + TimeUnit.MILLISECONDS.toMinutes(timeDiff) % TimeUnit.HOURS.toMinutes(1) + " minutes.",LENGTH_LONG).show();
                        }

                        else {
                            long timeDiff = 0;
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

                            Toast.makeText(getContext(), "Alarm will go off in " + TimeUnit.MILLISECONDS.toHours(smallestTimeDiff) + " hours " + TimeUnit.MILLISECONDS.toMinutes(smallestTimeDiff) % TimeUnit.HOURS.toMinutes(1) + " minutes.", LENGTH_LONG).show();


                        }

                        db.toggleAlarmActive(alarm.getId(), true);
                    }


                    else {
                        Intent intent = new Intent(getContext(), AlarmReceiver.class);
                        intent.putExtra("id", alarm.getId());
                        alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                        alarmIntent = PendingIntent.getBroadcast(getContext(), alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

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


    void toggleAlarm(boolean toggle)
    {

    }

}



