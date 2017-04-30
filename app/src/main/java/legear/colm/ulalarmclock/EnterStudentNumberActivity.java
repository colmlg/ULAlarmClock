package legear.colm.ulalarmclock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Activity that allows users to set their alarms automatically from the UL timetable by entering their student number.
 * Uses the JSoup library to get information and parse HTML.
 */
public class EnterStudentNumberActivity extends AppCompatActivity {
    ProgressDialog mPD;
    double offset;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_enter_student_number);
        final EditText et = (EditText) findViewById(R.id.editText6);
        final Spinner spin = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_offset_choices, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spin.setAdapter(adapter);
        spin.setSelection(2);

        Button okButton = (Button) findViewById(R.id.button3);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvInvalid = (TextView) findViewById(R.id.textViewInvalid);

                if (et.getText().toString().length() > 8 || et.getText().toString().length() < 7) {
                    tvInvalid.setVisibility(View.VISIBLE);
                } else {
                    int studentNumber = Integer.parseInt(et.getText().toString());
                    tvInvalid.setVisibility(View.INVISIBLE);

                    String offSet = spin.getSelectedItem().toString();
                    String[] split = offSet.split(" ");
                    offset = Double.parseDouble(split[0]);
                    String urlString = "http://tt.daniel.ie/tt.php?id=" + studentNumber;
                    WebpageGetter asyncGetter = new WebpageGetter();
                    asyncGetter.execute(urlString);
                }
            }
        });

    }

    void setAlarms(String document)
    {
        boolean atLeastOneAlarm = false;

        //If we cannot connect to the internet
        if(document.contains("IOException"))
        {
            mPD.dismiss();
            setResult(3);
            finish();
        }
        else {
            Document doc = Jsoup.parse(document);
            Element containerDiv = doc.getElementById("container");
            Elements lectures = containerDiv.getElementsByTag("div");
            //Time of the first lecture, monday to friday
            int[] firstLectureTimes = {99, 99, 99, 99, 99};

            for (Element lecture : lectures) {
                String styleString = lecture.attr("style");
                String[] styleSplit = styleString.split(";");
                String[] styleComponentSplit = {};
                int day = 0;
                String margin = "";

                for (String s : styleSplit) {
                    //Lecture Days
                    if (s.contains("left")) {
                        styleComponentSplit = s.split(":");
                        margin = styleComponentSplit[1];
                        switch (margin) {
                            case "105px":
                                day = 0;
                                break;
                            case "208px":
                                day = 1;
                                break;
                            case "311px":
                                day = 2;
                                break;
                            case "414px":
                                day = 3;
                                break;
                            case "517px":
                                day = 4;
                                break;

                        }

                    }
                    //Lecture Times
                    else if (s.contains("top")) {
                        styleComponentSplit = s.split(":");
                        margin = styleComponentSplit[1];
                        switch (margin) {
                            case "28px":
                                firstLectureTimes[day] = 9;
                                break;
                            case "78px":
                                firstLectureTimes[day] = firstLectureTimes[day] > 10 ? 10 : firstLectureTimes[day];
                                break;
                            case "128px":
                                firstLectureTimes[day] = firstLectureTimes[day] > 11 ? 11 : firstLectureTimes[day];
                                break;
                            case "178px":
                                firstLectureTimes[day] = firstLectureTimes[day] > 12 ? 12 : firstLectureTimes[day];
                                break;
                            case "228px":
                                firstLectureTimes[day] = firstLectureTimes[day] > 13 ? 13 : firstLectureTimes[day];
                                break;
                            case "278px":
                                firstLectureTimes[day] = firstLectureTimes[day] > 14 ? 14 : firstLectureTimes[day];
                                break;
                            case "328px":
                                firstLectureTimes[day] = firstLectureTimes[day] > 15 ? 15 : firstLectureTimes[day];
                                break;
                            case "378px":
                                firstLectureTimes[day] = firstLectureTimes[day] > 16 ? 16 : firstLectureTimes[day];
                                break;
                            case "428px":
                                firstLectureTimes[day] = firstLectureTimes[day] > 17 ? 17 : firstLectureTimes[day];
                                break;
                        }
                    }

                }


            }

            //Create the alarm objects and set the alarms
            AlarmSetter setter = new AlarmSetter(getApplicationContext());
            int[] repeatDays;
            Alarm alarm;
            DatabaseHandler db = new DatabaseHandler(context);
            for (int i = 0; i < firstLectureTimes.length; i++) {
                repeatDays = new int[7];
                if (firstLectureTimes[i] < 99) {
                    repeatDays[i] = 1;
                    alarm = new Alarm();
                    if (offset > 0) {
                        if (offset % 1 > 0) {
                            firstLectureTimes[i] = firstLectureTimes[i] - 1 - (int) (offset - 0.5);
                            alarm.setTime(firstLectureTimes[i], 30);
                        } else {
                            firstLectureTimes[i] = firstLectureTimes[i] - (int) (offset);
                            alarm.setTime(firstLectureTimes[i], 0);
                        }
                    } else
                        alarm.setTime(firstLectureTimes[i], 0);

                    alarm.setRepeatDays(repeatDays);
                    alarm.setUri(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                    if(alarm.getUri() == null)
                        alarm.setUri(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                    db.addAlarm(alarm);
                    alarm.setId(db.getLastInsertId());
                    setter.setAlarm(alarm);

                    atLeastOneAlarm = true;
                }

            }

            if (mPD.isShowing())
                mPD.dismiss();

            Intent returnIntent = new Intent();
            if (atLeastOneAlarm)
                setResult(Activity.RESULT_OK, returnIntent);
            else
                setResult(2, returnIntent);

            finish();
        }
    }


    private class WebpageGetter extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            mPD = new ProgressDialog(context);
            mPD.setMessage("Retreiving timetable info...");
            mPD.setIndeterminate(true);
            mPD.setCancelable(false);
            mPD.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return Jsoup.connect(urls[0]).get().toString();
            }
            catch (IOException e) {
                return "IOException";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Runs in the UI thread
            setAlarms(result);
        }
    }
}



