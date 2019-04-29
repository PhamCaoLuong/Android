package com.luong.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String NOTE_TABLE = "note";
    private static final String ID_COL = "ID";

    public DatabaseHelper(Context context) {
        super(context, NOTE_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS init(isInit INT(1))");

        db.execSQL("CREATE TABLE IF NOT EXISTS note (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "place VARCHAR," +
                "start_day TEXT NOT NULL," +
                "end_day TEXT NOT NULL," +
                "cost INTEGER NOT NULL, member INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS money_member_note (" +
                "id_note INTEGER NOT NULL," +
                "name_member VARCHAR," +
                "money INTEGER NOT NULL," +
                "FOREIGN KEY (id_note) REFERENCES note(ID) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS member_note (" +
                "id_note INTEGER NOT NULL," +
                "name_member VARCHAR," +
                "FOREIGN KEY (id_note) REFERENCES note(ID)  ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS cost_item (" +
                "id_note INTEGER NOT NULL," +
                "name VARCHAR," +
                "cost INTEGER NOT NULL," +
                "FOREIGN KEY (id_note) REFERENCES note(ID)  ON DELETE CASCADE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addData(String table, Object[] values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (table) {
            case "note":
                contentValues.put("place", (String) values[0]);
                contentValues.put("start_day", (String) values[1]);
                contentValues.put("end_day",(String) values[2] );
                contentValues.put("cost", (int) values[3]);
                contentValues.put("member", (int)values[4]);
                break;
            case "money_member_note":
                contentValues.put("id_note", Integer.parseInt((String) values[0]));
                contentValues.put("name_member", (String) values[1]);
                contentValues.put("money", Integer.parseInt( (String)values[2]));
                break;
            case "member_note":
                contentValues.put("id_note", Integer.parseInt((String) values[0]));
                contentValues.put("name_member", (String)values[1]);
                break;
            case "cost_item":
                contentValues.put("id_note", Integer.parseInt((String) values[0]));
                contentValues.put("name", (String)values[1]);
                contentValues.put("cost", Integer.parseInt( (String)values[2]));
                break;
        }

        long result = db.insert(table, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            Log.i("db", "add data success");
            return true;
        }
    }

    Cursor getData(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(query, null);
        if (data != null) {
            Log.i("db", "get data success");
        }
        return  data;
    }

    public void deleteData(String table, String condition){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table + " WHERE " + condition);
        Log.i("delete db", "success");
    }

    public void updateData(String table, String update ,String condition) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + table + " SET " + update + " WHERE "+ condition);
        Log.i("update db", "success");
    }
}
