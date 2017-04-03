package legear.colm.ulalarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colml on 28/03/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "alarms.db";

    // table name
    private static final String TABLE_ALARMS = "alarms";
    private static final String TABLE_ALARMREPEATDAYS = "alarmRepeatDays";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_MONDAY = "monday";
    private static final String KEY_TUESDAY = "tuesday";
    private static final String KEY_WEDNESDAY = "wednesday";
    private static final String KEY_THURSDAY = "thursday";
    private static final String KEY_FRIDAY = "friday";
    private static final String KEY_SATURDAY = "saturday";
    private static final String KEY_SUNDAY = "sunday";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME + " VARCHAR(255)" + ")";
        db.execSQL(CREATE_ALARMS_TABLE);

        String CREATE_ALARMREPEATDAYS_TABLE = "CREATE TABLE " + TABLE_ALARMREPEATDAYS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MONDAY + " BOOLEAN,"
                + KEY_TUESDAY + " BOOLEAN," + KEY_WEDNESDAY + " BOOLEAN," + KEY_THURSDAY + " BOOLEAN,"
                + KEY_FRIDAY + " BOOLEAN," + KEY_SATURDAY + " BOOLEAN," + KEY_SUNDAY + " BOOLEAN" + ")";
        db.execSQL(CREATE_ALARMREPEATDAYS_TABLE);
    }


    /**
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }**/

    public void addAlarm(Alarm alarm)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, alarm.getTime());
        // Inserting Row
        db.insert(TABLE_ALARMS, null, values);

        int [] repeatDays = alarm.getRepeatDays();
        ContentValues repeatValues = new ContentValues();
        repeatValues.put(KEY_MONDAY, repeatDays[0] == 1 ? 1 : 0);
        repeatValues.put(KEY_TUESDAY, repeatDays[1] == 1 ? 1 : 0);
        repeatValues.put(KEY_WEDNESDAY, repeatDays[2] == 1 ? 1 : 0);
        repeatValues.put(KEY_THURSDAY, repeatDays[3] == 1 ? 1 : 0);
        repeatValues.put(KEY_FRIDAY, repeatDays[4] == 1 ? 1 : 0);
        repeatValues.put(KEY_SATURDAY, repeatDays[5] == 1 ? 1 : 0);
        repeatValues.put(KEY_SUNDAY, repeatDays[6] == 1 ? 1 : 0);
        db.insert(TABLE_ALARMREPEATDAYS, null, repeatValues);

        db.close(); // Closing database connection
    }

    public ArrayList<Alarm> getAllAlarms() {
        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMS + " JOIN " + TABLE_ALARMREPEATDAYS + " ON " + TABLE_ALARMS + ".id = " + TABLE_ALARMREPEATDAYS + ".id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int [] repeatDays;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setId(Integer.parseInt(cursor.getString(0)));
                alarm.setTime(cursor.getString(1));
                repeatDays = new int[]{Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                                        Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)) };
                alarm.setRepeatDays(repeatDays);



                // Adding contact to list
                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return alarmList;
    }

    public Alarm getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMS + " JOIN " + TABLE_ALARMREPEATDAYS + " ON " + TABLE_ALARMS + ".id = " + TABLE_ALARMREPEATDAYS + ".id WHERE " + TABLE_ALARMS +".id = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();

        Alarm alarm = new Alarm();
        alarm.setId(Integer.parseInt(cursor.getString(0)));
        alarm.setTime(cursor.getString(1));
        int [] repeatDays = {Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)) };
        alarm.setRepeatDays(repeatDays);
        // return alarm
        return alarm;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMREPEATDAYS);

        // Create tables again
        onCreate(db);
    }
}