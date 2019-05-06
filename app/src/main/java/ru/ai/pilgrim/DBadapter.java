package ru.ai.pilgrim;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;


public class DBadapter {
    SQLiteDatabase db;
    Context c;
    RouteDatabaseHelper dbHelper;
    public DBadapter(Context ctx) {
        this.c=ctx;
        dbHelper = new RouteDatabaseHelper(c);
    }
    public DBadapter open () {
        try {
            db= dbHelper.getWritableDatabase();

        }catch (SQLException e) {
            e.printStackTrace();
        } return this;
    }
    public void close() {
        try {
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Cursor getAllPoints()
    {
        String[] columns={"_ID","DESCRIPTION","PHOTO"};
        return db.query("POINTS",columns,null,null,null,null,null);
    }
}
