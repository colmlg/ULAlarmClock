package legear.colm.ulalarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by colml on 28/03/2017.
 * A database handler for our database.
 */

class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 9;

    // Database Name
    private static final String DATABASE_NAME = "alarms.db";

    // table name
    private static final String TABLE_ALARMS = "alarms";
    private static final String TABLE_ALARMREPEATDAYS = "alarmRepeatDays";
   // private static final String TABLE_MATH_PUZZLES = "math_puzzles";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_PUZZLES = "puzzles";
    private static final String KEY_RINGTONE = "ringtone";

    private static final String KEY_MONDAY = "monday";
    private static final String KEY_TUESDAY = "tuesday";
    private static final String KEY_WEDNESDAY = "wednesday";
    private static final String KEY_THURSDAY = "thursday";
    private static final String KEY_FRIDAY = "friday";
    private static final String KEY_SATURDAY = "saturday";
    private static final String KEY_SUNDAY = "sunday";

   // private static final String KEY_QUESTION = "question";
    //private static final String KEY_ANSWER = "answer";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME + " TEXT," + KEY_ACTIVE + " BOOLEAN," + KEY_PUZZLES + " TEXT," + KEY_RINGTONE + " TEXT " + ")";
        db.execSQL(CREATE_ALARMS_TABLE);

        String CREATE_ALARMREPEATDAYS_TABLE = "CREATE TABLE " + TABLE_ALARMREPEATDAYS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MONDAY + " BOOLEAN,"
                + KEY_TUESDAY + " BOOLEAN," + KEY_WEDNESDAY + " BOOLEAN," + KEY_THURSDAY + " BOOLEAN,"
                + KEY_FRIDAY + " BOOLEAN," + KEY_SATURDAY + " BOOLEAN," + KEY_SUNDAY + " BOOLEAN" + ")";
        db.execSQL(CREATE_ALARMREPEATDAYS_TABLE);
    }

    public void addAlarm(Alarm alarm)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, alarm.getTime());
        values.put(KEY_ACTIVE, alarm.isEnabled() ? 1 : 0);
        values.put(KEY_PUZZLES, alarm.getPuzzles());
        values.put(KEY_RINGTONE, alarm.getUri().toString());
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

    public void toggleAlarmActive(int id, boolean enabled)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVE, enabled ? 1 : 0);
        db.update(TABLE_ALARMS, values, "id=" + id, null);
        db.close();

    }

    public int getLastInsertId()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_ID + ") FROM " + TABLE_ALARMS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        db.close();
        return cursor.getInt(0);
    }

    public void updateAlarm(Alarm alarm)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateAlarmTable = "UPDATE " + TABLE_ALARMS + " SET " + KEY_TIME + " = '" + alarm.getTime() + "', " + KEY_ACTIVE + " = '" + (alarm.isEnabled() ? 1 : 0) + "', " + KEY_PUZZLES + " = '" + alarm.getPuzzles() + "', " + KEY_RINGTONE + " = '" + alarm.getUri().toString() + "' WHERE " + KEY_ID + " = '" + alarm.getId() + "'";

        db.execSQL(updateAlarmTable);
        int [] repeatDays = alarm.getRepeatDays();
        String updateRepeatTable = "UPDATE " + TABLE_ALARMREPEATDAYS + " SET " + KEY_MONDAY + " = "  + (repeatDays[0] == 1 ? 1 : 0) + ", " + KEY_TUESDAY + " = "  + (repeatDays[1] == 1 ? 1 : 0) + ", "
                + KEY_WEDNESDAY + " = "  + (repeatDays[2] == 1 ? 1 : 0) + ", " + KEY_THURSDAY + " = "  + (repeatDays[3] == 1 ? 1 : 0) + ", " + KEY_FRIDAY + " = "  + (repeatDays[4] == 1 ? 1 : 0) + ", " +
                KEY_SATURDAY+ " = "  + (repeatDays[5] == 1 ? 1 : 0) + ", " + KEY_SUNDAY + " = "  + (repeatDays[1] == 1 ? 1 : 0) + " WHERE " + KEY_ID + " = " + alarm.getId();
        db.execSQL(updateRepeatTable);
        db.close();
    }

    public void deleteAlarm(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, "id = " + id, null);
        db.delete(TABLE_ALARMREPEATDAYS, "id = " + id, null);
        db.close();

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
                alarm.setEnabled(cursor.getInt(2) == 1);
                alarm.setPuzzles(cursor.getString(3));
                alarm.setUri(Uri.parse(cursor.getString(4)));

                repeatDays = new int[]{Integer.parseInt(cursor.getString(6)),Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)),
                                        Integer.parseInt(cursor.getString(10)), Integer.parseInt(cursor.getString(11)),  Integer.parseInt(cursor.getString(12)) };
                alarm.setRepeatDays(repeatDays);



                // Adding contact to list
                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
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
        alarm.setEnabled(cursor.getInt(2) == 1);
        alarm.setPuzzles(cursor.getString(3));
        alarm.setUri(Uri.parse(cursor.getString(4)));

        int [] repeatDays = new int[]{Integer.parseInt(cursor.getString(6)),Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)),
                Integer.parseInt(cursor.getString(10)), Integer.parseInt(cursor.getString(11)),  Integer.parseInt(cursor.getString(12)) };
        alarm.setRepeatDays(repeatDays);
        cursor.close();
        db.close();
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