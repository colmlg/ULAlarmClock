package legear.colm.ulalarmclock;

import java.util.Calendar;

/**
 * Created by colml on 28/03/2017.
 */

public class Alarm {

    private Calendar alarmTime;
    private String emailAddress;
    private String phoneNumber;

    public Alarm(Calendar alarmTime)
    {
        this.alarmTime = alarmTime;
        emailAddress = "";
        phoneNumber = "";
    }

    public Alarm(Calendar alarmTime, String emailAddress, String phoneNumber)
    {
        this.alarmTime = alarmTime;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public Calendar getCalendar()
    {
        return alarmTime;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

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
}
