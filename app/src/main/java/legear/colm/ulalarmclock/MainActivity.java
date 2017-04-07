package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Alarm> listAlarms = new ArrayList<Alarm>();
    private AlarmAdapter adapter;
    private DatabaseHandler db;
    private int i = 0;

    private ListView alarmListView;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAlarm = new Intent(view.getContext(), AddAlarm.class);
                startActivityForResult(addAlarm, 1);
            }
        });


        //Set up the adapter and listview
        db = new DatabaseHandler(getApplicationContext());
        listAlarms = db.getAllAlarms();
        alarmListView = (ListView) findViewById(R.id.AlarmList);
        adapter = new AlarmAdapter(this,listAlarms);
        alarmListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Set up the alarm manager
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int id;
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                id = data.getIntExtra("id", 0);
                adapter.add(db.getAlarm(id));

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("id", id);
                alarmIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar currentTime = new GregorianCalendar();
                Alarm currentAlarm = db.getAlarm(id);

                //If the alarm is to be set for before the current time, schedule it to start tomorrow
                if((currentTime.get(Calendar.HOUR_OF_DAY) > currentAlarm.getCalendar().get(Calendar.HOUR_OF_DAY)) ||
                        (currentTime.get(Calendar.HOUR_OF_DAY) == currentAlarm.getCalendar().get(Calendar.HOUR_OF_DAY) && currentTime.get(Calendar.MINUTE) > currentAlarm.getCalendar().get(Calendar.MINUTE)))
                {
                    currentAlarm.getCalendar().add(Calendar.DAY_OF_YEAR, 1);
                }

                if(!currentAlarm.isRepeating())
                {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, currentAlarm.getCalendar().getTimeInMillis(), alarmIntent);
                }

                else {
                    int [] repeatDays = currentAlarm.getRepeatDays();
                    for(int i = 0; i < repeatDays.length; i++)
                    {
                        if(repeatDays[i] == 1)
                        {
                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY * 7, currentAlarm.getCalendar().getTimeInMillis(), alarmIntent);
                        }

                        currentAlarm.getCalendar().add(Calendar.DAY_OF_YEAR, 1);
                    }
                }


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), "Toasty NOT!", LENGTH_LONG);
                toast.show();
            }
        }
    }//onActivityResult



}
