package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

    ArrayList<Alarm> listAlarms = new ArrayList<Alarm>();
    ArrayAdapter<Alarm> adapter;
    DatabaseHandler db;
    int i = 0;

    private ListView alarmListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(getApplicationContext());
        listAlarms = db.getAllAlarms();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAlarm = new Intent(view.getContext(), AddAlarm.class);
                startActivityForResult(addAlarm, 1);
            }
        });

        alarmListView = (ListView) findViewById(R.id.AlarmList);
        adapter = new ArrayAdapter<Alarm>(this, android.R.layout.simple_list_item_1,listAlarms);
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

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                listAlarms = db.getAllAlarms();
                adapter = new ArrayAdapter<Alarm>(this, android.R.layout.simple_list_item_1,listAlarms);
                alarmListView.setAdapter(adapter);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), "Toasty NOT!", LENGTH_LONG);
                toast.show();
            }
        }
    }//onActivityResult

    private void toastListofAlarms()
    {
        String text = listAlarms.size() + "\n";
        for(Alarm a : listAlarms)
        {
            text += a.toString() + "\n";
        }

        Toast toast = Toast.makeText(getApplicationContext(), text, LENGTH_LONG);
        toast.show();
    }

}
