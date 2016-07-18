package utility.nbkuk.com.tasknotemanager;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by jason.dobo on 18/07/2016.
 */
public class TaskDatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // private static final String TAG = "ToDoDatabaseHandler";
    private static final String DATABASE_NAME = "todoManager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TASKS_TABLE_NAME = "tasks";

    // to do Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";

    public TaskDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TASKS_TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_STATUS + " INTEGER,"
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
        values.put(KEY_NAME, newTask); // Task Name
        values.put(KEY_STATUS, 0); // Task Status
        values.put(KEY_DATE, dtMili); // New Date

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

        String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_STATUS, KEY_DATE};
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
    public int updateTask(long id, String updateTask, boolean updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = getTask(id);
        c.moveToFirst();

        long dtMili = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, updateTask);
        if (updateStatus) {
            values.put(KEY_STATUS, 1);
        } else {
            values.put(KEY_STATUS, 0);
        }
        if (!c.getString(c.getColumnIndex(KEY_NAME)).equals(updateTask)) {
            values.put(KEY_DATE, dtMili);
        }
        else {
            values.put(KEY_DATE, c.getString(c.getColumnIndex(KEY_DATE)));
        }

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
