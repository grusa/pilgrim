package ru.ai.pilgrim;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import java.io.ByteArrayOutputStream;
import android.database.Cursor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.sql.Blob;

public class RouteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="routes";
    private static final int DB_VERSION = 1;
    SQLiteDatabase db;


    RouteDatabaseHelper (Context context) {
        super ( context, DB_NAME,null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE routes (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL);");
        db.execSQL("CREATE TABLE points (_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "date NUMERIC,\n" +
                "latitude REAL,\n" +
                "longitude REAL,\n" +
                "photo BLOB,\n" +
                "description TEXT,\n" +
                "route_id INTEGER(4),\n" +
                "\tFOREIGN KEY (route_id) REFERENCES routes(id));");
        db.execSQL("CREATE TABLE routecoordinates (_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "latitude REAL,\n" +
                "longitude REAL,\n" +
                "date NUMERIC,\n" +
                "route_id INTEGER,\n" +
                "\tFOREIGN KEY (route_id) REFERENCES routes(id));");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS route;");
        db.execSQL("DROP TABLE IF EXISTS points;");
        db.execSQL("DROP TABLE IF EXISTS routecoordinates;");
        onCreate(db);

    }
    public static void insertPoint (SQLiteDatabase db,
                                     long currentDate,
                                     double latitude,
                                     double longitude,
                                     Bitmap makedPhoto,
                                     String description) {
        try {
            ContentValues pointValues = new ContentValues();
            pointValues.put("DATE", currentDate);
            pointValues.put("LATITUDE", latitude);
            pointValues.put("LONGITUDE", longitude);
            pointValues.put("PHOTO", getBytes(makedPhoto));
            pointValues.put("DESCRIPTION", description);
            db.insert("POINTS", null, pointValues);
        } catch (Exception e) {

        }

    }
   /* public static byte[] readImage(Bitmap fileImage) {
        ByteArrayOutputStream baos=null;
        try {
            //File f = new File(fileName);
            //FileInputStream fis = new FileInputStream(fileName);
            return Utility fileImage)
            byte [] buffer; //new byte[fis.available()];
            Buffer buffer1;
            fileImage.copyPixelsToBuffer(buffer);
            fis.read(buffer);
            baos = new ByteArrayOutputStream();
            fileImage.copyPixelsToBuffer(buffer1);
            /*for (int len; (len=fis.read(buffer)) !=-1;) {
                baos.write(buffer,0,len);
            }*/

            //fis.close();
        /*} catch (FileNotFoundException e1) {
            System.err.println(e1.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return baos !=null ? baos.toByteArray() : null;
    }*/

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getPhoto(byte[] image) {
       if (image!=null) {return BitmapFactory.decodeByteArray(image, 0, image.length);}
       else {return null;}
    }

}

