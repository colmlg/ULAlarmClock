package legear.colm.ulalarmclock;

import android.app.Activity;
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

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Alarm> listAlarms = new ArrayList<Alarm>();
    private AlarmAdapter adapter;
    private DatabaseHandler db;
    private int i = 0;

    private ListView alarmListView;






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


        //Set up the adapter and listview
        db = new DatabaseHandler(getApplicationContext());
        listAlarms = db.getAllAlarms();
        alarmListView = (ListView) findViewById(R.id.AlarmList);
        adapter = new AlarmAdapter(this,listAlarms);
        alarmListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


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
            if(resultCode == Activity.RESULT_OK){
                listAlarms = db.getAllAlarms();
                adapter = new AlarmAdapter(this,listAlarms);
                alarmListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), "Toasty NOT!", Toast.LENGTH_SHORT);
                toast.show();
            }

    }//onActivityResult


    private void addAlarm()
    {
        Intent addAlarm = new Intent(getApplicationContext(), CreateAlarm.class);
        startActivityForResult(addAlarm, 1);
    }

}
