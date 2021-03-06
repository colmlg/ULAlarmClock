package legear.colm.ulalarmclock;

import android.net.Uri;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by colml on 28/03/2017.
 * A Class representing an alarm
 */

public class Alarm {

    private Calendar alarmTime;
    private int [] repeatDays;
    public int id = 0;
    private boolean enabled;
    //A string of comma seperated values referring to the puzzles associated with this alarm 0 = math 1= memory game 2=password 3=Notification
    private String puzzles;
    private Uri uri;


    public Alarm()
    {
        alarmTime = new GregorianCalendar();
        repeatDays = new int[] {0, 0, 0, 0, 0, 0, 0};
        id = 0;
        enabled = true;
        puzzles = "";

    }


    public Alarm(Calendar alarmTime)
    {
        this.alarmTime = alarmTime;
        repeatDays = new int[7];
    }


    public Alarm(Calendar alarmTime, int [] repeatDays)
    {
        this.alarmTime = alarmTime;
        this.repeatDays = repeatDays;

    }

    public boolean isRepeating()
    {
        for(int i : repeatDays)
        {
            if (i == 1)
                return true;
        }

        return false;
    }

    //Sets the puzzles associated with this alarm
    public void setPuzzles(String puzzles)
    {
        this.puzzles = puzzles;
    }

    public String getPuzzles()
    {
        return puzzles;
    }

    public Calendar getCalendar()
    {
        return alarmTime;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getTime()
    {
        int hour = alarmTime.get(Calendar.HOUR_OF_DAY);
        String hourString = "" + hour;
        if(hour / 10 == 0)
            hourString = "0" + hour;

        int minute = alarmTime.get(Calendar.MINUTE);
        String minuteString = "" + minute;
        if(minute / 10 == 0)
            minuteString = "0" + minute;

        return  hourString + ":" + minuteString;
    }


    public void setTime(String time)
    {
        String [] timeSplit = time.split(":");
        alarmTime.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeSplit[0]));
        alarmTime.set(Calendar.MINUTE,Integer.parseInt(timeSplit[1]));
        alarmTime.set(Calendar.SECOND,0);
    }

    public void setTime(int hour, int minute)
    {
        alarmTime.set(Calendar.SECOND,0);
        alarmTime.set(Calendar.HOUR_OF_DAY,hour);
        alarmTime.set(Calendar.MINUTE,minute);
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId()
    {
        return id;
    }



    public int[] getRepeatDays() { return  repeatDays;}

    public void setRepeatDays(int[] repeatDays) {this.repeatDays = repeatDays;}

    public String getRepeatString()
    {
        String repeatString =  "Repeat: ";
        String [] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        boolean repeats = false;

        for(int i = 0; i < repeatDays.length; i++)
        {
            if(repeatDays[i] == 1){
                repeatString += " " + daysOfWeek[i];
                repeats = true;
            }

        }

        if(!repeats)
            repeatString += "None";

        return repeatString;
    }

    public String getPuzzleString()
    {
        String puzzleString = "Puzzles: ";
        String [] puzzlesArray = puzzles.split(",");

        for(int i = 0; i < puzzlesArray.length; i++)
        {
            switch(puzzlesArray[i]){
                case "0":
                    puzzleString += "Maths  ";
                    break;
                case "1":
                    puzzleString += "Memory  ";
                    break;
                case "2":
                    puzzleString += "Password  ";
                    break;
                case "3":
                    puzzleString += "Notification  ";
                    break;
            }
        }

        if(puzzleString.equals("Puzzles: "))
            puzzleString = "";
        else {
            puzzleString = puzzleString.trim();
            puzzleString = puzzleString.replaceAll("  ", ", ");
        }

        return puzzleString;
    }

    @Override
    public String toString()
    {

        int hour = alarmTime.get(Calendar.HOUR_OF_DAY);
        String hourString = "" + hour;
        if(hour / 10 == 0)
            hourString = "0" + hour;

        int minute = alarmTime.get(Calendar.MINUTE);
        String minuteString = "" + minute;
        if(minute / 10 == 0)
            minuteString = "0" + minute;

        String alarmString =  hourString + ":" + minuteString + "\n" + "Repeat: ";
        String [] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        boolean repeats = false;

        for(int i = 0; i < repeatDays.length; i++)
        {
            if(repeatDays[i] == 1){
                alarmString += " " + daysOfWeek[i];
                repeats = true;
            }

        }

        if(!repeats)
            alarmString += "None";

        return alarmString;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
