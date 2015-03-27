package ca.sfu.generiglesias.dutchie_meetly;

/**
* Created by David on 2015-03-24.
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    public static final String KEY_EVENTNAME = "event_name";
    public static final String KEY_EVENTDATE = "event_date";
    public static final String KEY_LOCATION = "event_location";
    public static final String KEY_EVENTDESCRIPTION = "description";
    public static final String KEY_EVENTSTARTTIME = "start_time";
    public static final String KEY_EVENTENDTIME = "end_time";
    public static final String KEY_EVENTDURATION = "duration";
//    public static final String KEY_EVENTICONID = "icon_id";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    // shared flag
    public static final String KEY_SHAREDFLAG = "sharedFlag";

    //Table column indexes
    public static final int COL_EVENTNAME = 1;
    public static final int COL_EVENTDATE = 2;
    public static final int COL_LOCATION = 3;
    public static final int COL_EVENTDESCRIPTION = 4;
    public static final int COL_EVENTSTARTTIME = 5;
    public static final int COL_EVENTENDTIME = 6;
    public static final int COL_EVENTDURATION = 7;
//    public static final int COL_EVENTICONID = 8;
    public static final int COL_LATITUDE = 8;
    public static final int COL_LONGITUDE = 9;
    public static final int COL_SHAREDFLAG = 10;

    public static final String[] ALL_KEYS = new String[] {
            KEY_ROWID,
            KEY_EVENTNAME,
            KEY_EVENTDATE,
            KEY_LOCATION,
            KEY_EVENTDESCRIPTION,
            KEY_EVENTSTARTTIME,
            KEY_EVENTENDTIME,
            KEY_EVENTDURATION,
            KEY_LATITUDE,
            KEY_LONGITUDE,
            KEY_SHAREDFLAG
    };

    public static final String DATABASE_NAME = "MyDb";
    public static final String DATABASE_TABLE = "events";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
                    + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_EVENTNAME + " text not null, "
                    + KEY_EVENTDATE + " text not null, "
                    + KEY_LOCATION + " text not null,"
                    + KEY_EVENTDESCRIPTION + " text not null,"
                    + KEY_EVENTSTARTTIME + " text not null,"
                    + KEY_EVENTENDTIME + " text not null,"
                    + KEY_EVENTDURATION + " text not null,"
                    + KEY_LATITUDE + " float not null,"
                    + KEY_LONGITUDE + " float not null,"
                    + KEY_SHAREDFLAG + " text not null"
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String event_name,
                          String event_date,
                          String event_location,
                          String event_description,
                          String event_start_time,
                          String event_end_time,
                          String event_duration,
                          double latitude,
                          double longitude,
                          String sharedFlag) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_EVENTNAME, event_name);
        initialValues.put(KEY_EVENTDATE, event_date);
        initialValues.put(KEY_LOCATION, event_location);
        initialValues.put(KEY_EVENTDESCRIPTION, event_description);
        initialValues.put(KEY_EVENTSTARTTIME, event_start_time);
        initialValues.put(KEY_EVENTENDTIME, event_end_time);
        initialValues.put(KEY_EVENTDURATION, event_duration);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);
        initialValues.put(KEY_SHAREDFLAG, sharedFlag);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId,
                             String event_name,
                             String event_date,
                             String event_location,
                             String event_description,
                             String event_start_time,
                             String event_end_time,
                             String event_duration,
                             double latitude,
                             double longitude,
                             String sharedFlag){
        String where = KEY_ROWID + "=" + rowId;

        ContentValues newValues = new ContentValues();
        newValues.put(KEY_EVENTNAME, event_name);
        newValues.put(KEY_EVENTDATE, event_date);
        newValues.put(KEY_LOCATION, event_location);
        newValues.put(KEY_EVENTDESCRIPTION, event_description);
        newValues.put(KEY_EVENTSTARTTIME, event_start_time);
        newValues.put(KEY_EVENTENDTIME, event_end_time);
        newValues.put(KEY_EVENTDURATION, event_duration);
        newValues.put(KEY_LATITUDE, latitude);
        newValues.put(KEY_LONGITUDE, longitude);
        newValues.put(KEY_SHAREDFLAG, sharedFlag);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }



    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
