package com.neeraj.notesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseClass extends SQLiteOpenHelper {
    Context context;

    public static final int DatabaseVersion = 1;
    public static final String DatabaseName = "MyNotes";
    private static final String TableName = "mynotes";

    private static final String ColumnId = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnDescription = "description";
    private static final String UserId = "userId";

    public DatabaseClass(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TableName + " (" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ColumnTitle + " TEXT, " + ColumnDescription + " TEXT, " + UserId + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }

    public void addNotes(String title, String description, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);
        cv.put(UserId, userId);

        long resultValue = db.insert(TableName, null, cv);

        if (resultValue == -1) {
            Toast.makeText(context, "Data could not be added", Toast.LENGTH_SHORT).show();
            Log.d("status", "not added");
        } else {
            Toast.makeText(context, "Data added successfully", Toast.LENGTH_SHORT).show();
            Log.d("status", "added");
        }
    }

    public Cursor readAllData(String userId) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery("SELECT * FROM " + TableName +
                    " WHERE UserId = ?", new String[] {userId}, null);
        }
        return cursor;
    }

    public void deleteAllNotes(String userId) {
        try{
            SQLiteDatabase database = this.getWritableDatabase();
            String query = "DELETE FROM " + TableName + " WHERE UserId='" + userId + "'";
            database.execSQL(query);
            Log.i("success", "done");
        } catch (Exception e){
            Log.e("Exception", e.toString());
        }
    }

    public void updateNotes(String title, String description, String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long result = database.update(TableName, cv, "id=?", new String[]{id});

        if (result == -1) {
            Toast.makeText(context, "Could no update note", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Note updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteSingleItem(String id, String userId) {
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TableName, "id=? AND UserId=?", new String[]{id, userId});

        if (result == -1){
            Toast.makeText(context, "Item Not Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}