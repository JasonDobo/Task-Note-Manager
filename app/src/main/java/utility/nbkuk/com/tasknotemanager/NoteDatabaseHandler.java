package utility.nbkuk.com.tasknotemanager;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by jason.dobo on 18/07/2016.
 */
public class NoteDatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // private static final String TAG = "NoteDatabaseHandler";
    private static final String DATABASE_NAME = "noteManager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TASKS_TABLE_NAME = "notes";

    // to do Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTE = "note";
    private static final String KEY_DATE = "date";

    final private int TITLE_LENGTH=30;

    public NoteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TASKS_TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_NOTE + " TEXT,"
                + KEY_DATE + " INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Adding new task
    public void addTask(String newTask) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get current date in dd-MM-yyy format
        long dtMili = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        if (newTask.length() >= TITLE_LENGTH) {
            values.put(KEY_TITLE, newTask.substring(0, TITLE_LENGTH)); // Note first line
        }
        else {
            values.put(KEY_TITLE, newTask);
        }

        values.put(KEY_NOTE, newTask); // Complete note
        values.put(KEY_DATE, dtMili); // Current date

        // Inserting Row
        db.insert(TASKS_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Cursor getTask(long id) {
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + TASKS_TABLE_NAME +
                " where " + KEY_ID + " = ?", new String[] { String.valueOf(id) });

        cursor.moveToFirst();
        return cursor ;
    }

    // Getting All task
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        //String selectQuery = "SELECT  * FROM " + TASKS_TABLE_NAME;
        //Cursor cursor = db.rawQuery(selectQuery, null);

        String[] columns = new String[]{KEY_ID, KEY_TITLE, KEY_NOTE, KEY_DATE};
        Cursor cursor = db.query(TASKS_TABLE_NAME, columns,
                null, null, null, null, KEY_DATE + " DESC");

        // return contact list
        cursor.moveToFirst();
        return cursor;
    }

    // Getting task Count
    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TASKS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Updating single task
    public int updateTask(long id, String updateTask) {
        SQLiteDatabase db = this.getWritableDatabase();

        long dtMili = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        if (updateTask.length() >= TITLE_LENGTH) {
            values.put(KEY_TITLE, updateTask.substring(0, TITLE_LENGTH)); // Note first line
        }
        else {
            values.put(KEY_TITLE, updateTask);
        }
        values.put(KEY_NOTE, updateTask);
        values.put(KEY_DATE, dtMili);

        // updating row
        return db.update(TASKS_TABLE_NAME, values, KEY_ID + " = ?",	new String[] { String.valueOf(id) });
    }

    // Deleting single task
    public void deleteTask(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TASKS_TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }
}
