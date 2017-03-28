package legear.colm.ulalarmclock;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by colml on 28/03/2017.
 */

public class Alarm {

    private Calendar alarmTime;
    private String emailAddress;
    private String phoneNumber;
    public int [] repeatDays;
    public int id;

    public Alarm()
    {
        alarmTime = new GregorianCalendar();
        repeatDays = new int[] {0, 0, 0, 0, 0, 0, 0};
        id = 0;

    }


    public Alarm(Calendar alarmTime)
    {
        this.alarmTime = alarmTime;
        emailAddress = "";
        phoneNumber = "";
        repeatDays = new int[7];
    }

    public Alarm(Calendar alarmTime, String emailAddress, String phoneNumber)
    {
        this.alarmTime = alarmTime;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public Alarm(Calendar alarmTime, int [] repeatDays)
    {
        this.alarmTime = alarmTime;
        this.repeatDays = repeatDays;
    }

    public Calendar getCalendar()
    {
        return alarmTime;
    }

    public String getTime()
    {
        return alarmTime.get(Calendar.HOUR_OF_DAY) + ":" + alarmTime.get(Calendar.MINUTE);
    }

    public void setTime(String time)
    {
        String [] timeSplit = time.split(":");
        alarmTime.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeSplit[0]));
        alarmTime.set(Calendar.MINUTE,Integer.parseInt(timeSplit[1]));
    }

    public void setTime(int hour, int minute)
    {

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

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public int[] getRepeatDays() { return  repeatDays;}

    public void setRepeatDays(int[] repeatDays) {this.repeatDays = repeatDays;}

    public void setAlarmTime(Calendar alarmTime)
    {
        this.alarmTime = alarmTime;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString()
    {
        return alarmTime.get(Calendar.HOUR_OF_DAY) + ":" + alarmTime.get(Calendar.MINUTE);
    }
}
