package softproduct.volvo.com.eco_drive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lesliedahlberg on 2016-11-21.
 * This class creates and manager the database for scores and gives access to methods which can
 * read and manipulate data in it.
 * !!!! IF YOU CHANGE THE DATABASE SCHEMA YOU NEED TO INCREMENT THE DATABASE_VERSION VARIABLE !!!!
 */
public class ScoreDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Database";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SCORES = "scores";

    public static final String KEY_LIST_ID = "_id";
    public static final String KEY_LIST_ALIAS = "alias";
    public static final String KEY_LIST_SCORE = "score";
    public static final String KEY_LIST_DATE = "date";
    public static final String KEY_LIST_RPM = "rpm";
    public static final String KEY_LIST_ACCELERATION = "acceleration";
    public static final String KEY_LIST_DISTANCE = "distance";
    public static final String KEY_LIST_LOAD = "load";
    public static final String KEY_LIST_CONSUMPTION = "consumption";
    public static final String KEY_LIST_ALTITUDE = "altitude";

    private static ScoreDbHelper sInstance;

    private ScoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ScoreDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ScoreDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    public void addListItem(ScoreItem scoreItem) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(KEY_LIST_ALIAS, scoreItem.alias);
            values.put(KEY_LIST_SCORE, scoreItem.score);
            values.put(KEY_LIST_DATE, scoreItem.date);
            values.put(KEY_LIST_RPM, scoreItem.rpm);
            values.put(KEY_LIST_ACCELERATION, scoreItem.acceleration);
            values.put(KEY_LIST_DISTANCE, scoreItem.distance);
            values.put(KEY_LIST_LOAD, scoreItem.load);
            values.put(KEY_LIST_CONSUMPTION, scoreItem.consumption);
            values.put(KEY_LIST_ALTITUDE, scoreItem.altitude);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_SCORES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add score item to database");
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getCursor(){
        String SCORES_SELECT_QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC", TABLE_SCORES, KEY_LIST_ID);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SCORES_SELECT_QUERY, null);
        return cursor;
    }

    public void createDummyData(int count){
        for(int i = 0; i < count; i++){
            addListItem(new ScoreItem(0, "Alias", 1234, "12-12-2012", 2500, 0.01, 25.0, 350.0, 1.5, 250.0));
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_SCORES +
                "(" +
                KEY_LIST_ID + " INTEGER PRIMARY KEY," +
                KEY_LIST_ALIAS + " TEXT, " +
                KEY_LIST_SCORE + " INTEGER, " +
                KEY_LIST_DATE + " TEXT, " +
                KEY_LIST_RPM + " REAL, " +
                KEY_LIST_ACCELERATION + " REAL, " +
                KEY_LIST_DISTANCE + " REAL, " +
                KEY_LIST_LOAD + " REAL, " +
                KEY_LIST_CONSUMPTION + " REAL, " +
                KEY_LIST_ALTITUDE + " REAL" +
                ")";
        db.execSQL(CREATE_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
            onCreate(db);
        }
    }
}
