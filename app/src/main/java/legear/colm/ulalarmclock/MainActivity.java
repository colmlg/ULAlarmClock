package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Alarm> listAlarms = new ArrayList<Alarm>();
    private AlarmAdapter adapter;
    private DatabaseHandler db;
    private int i = 0;
    private ListView alarmListView;
    private AlarmManager alarmManager;

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
                addAlarm();
            }
        });
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //Set up the adapter and listview
        db = new DatabaseHandler(getApplicationContext());
        alarmListView = (ListView) findViewById(R.id.AlarmList);
        refreshAlarmList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_alarms_from_id) {
                Intent ttIntent = new Intent(getApplicationContext(),EnterStudentNumberActivity.class);
                startActivityForResult(ttIntent, 3);
                refreshAlarmList();

        }
        else if(id == R.id.action_delete_all_alarms)
        {
            Intent intent;
            PendingIntent alarmIntent;
            for(Alarm alarm : listAlarms)
            {
                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("id", alarm.getId());
                alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(alarmIntent);
                alarmIntent.cancel();
                db.deleteAlarm(alarm.getId());
            }

            refreshAlarmList();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            refreshAlarmList();
        }

        else if (resultCode == 2 && requestCode == 3) {
            Toast.makeText(getApplicationContext(), "Error setting alarms from timetable, is your student number correct?", Toast.LENGTH_LONG).show();
        }

        else if (resultCode == 3 && requestCode == 3) {
            Toast.makeText(getApplicationContext(), "Error setting alarms from timetable, are you connected to the internet?", Toast.LENGTH_LONG).show();
        }

    }//onActivityResult


    private void addAlarm()
    {
        Intent addAlarm = new Intent(getApplicationContext(), CreateAlarm.class);
        startActivityForResult(addAlarm, 1);
    }

    private void refreshAlarmList()
    {
        listAlarms = db.getAllAlarms();
        adapter = new AlarmAdapter(this,listAlarms);
        alarmListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }




}
