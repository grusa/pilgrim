package ru.ai.pilgrim;
/**
@author sgrushin70@gmail.com
@version 1.0
@param readingMode boolean if true
 */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import android.content.ContentValues;

public class PointActivity extends AppCompatActivity {
    private Double latitude,longitude;
    private TextView tv_date;
    private static final int CAMERA_REQUEST = 0;
    private ImageView ivPhoto;
    private Date curDate;
    private EditText descriptionEt;
    private EditText latitudeEt;
    private EditText longitudeEt;
    private EditText dataEt;
    private Cursor cursor;
    private SQLiteOpenHelper routeDatabaseHelper;
    private SQLiteDatabase db;
    private boolean readingMode = false;
    private Bitmap thumbnailBitmap=null;
    private final String formatCoordinate = "%1$.15f";
    private String fileName=null;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private int callRule;
    private int position;
    ImageButton buttonNext;
    ImageButton buttonPrevious;
    ImageButton buttonSaveData;
    ImageButton buttonMakePhoto;
    ImageButton buttonDel,buttonMyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link dataFields
        setContentView(R.layout.activity_point);
        Intent intent = getIntent();

        callRule = intent.getIntExtra("callRule",1);
        position=intent.getIntExtra("pos",0);
        latitudeEt = findViewById(R.id.latitude);
        longitudeEt =  findViewById(R.id.longitude);
        descriptionEt = findViewById(R.id.description);
        dataEt = findViewById(R.id.date);
        buttonNext = findViewById(R.id.next);
        buttonMyData = findViewById(R.id.mydata);
        buttonMyData.setVisibility(View.INVISIBLE);
        buttonPrevious = findViewById(R.id.previous);
        buttonSaveData = findViewById(R.id.saveData);
        buttonMakePhoto = findViewById(R.id.makePhoto);
        buttonDel = findViewById(R.id.delButton);
        //COMPLETED check coordinate
        //COMPLETED make try
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = new Location(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                latitudeEt.setText("" + location.getLatitude());
                longitudeEt.setText("" + location.getLongitude());
            } else {
                latitudeEt.setText("0.0");
                longitudeEt.setText("0.0" );
                Toast.makeText(this,"Fine Location not granted to Pilgrim",Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error getting location"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        //set current date
        tv_date=findViewById(R.id.date);
        curDate = new Date();
        tv_date.setText(dateFormat.format(curDate));
        //Make photo
        ivPhoto = findViewById(R.id.photo);
        if (callRule==2)  {
            clickedMyData(null);
        }
    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        fileName = null;
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Фотка сделана, извлекаем картинку
            thumbnailBitmap=null;
            try {
                thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            } catch (NullPointerException e) {
                Toast.makeText(this,"Photo"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
            if (thumbnailBitmap!=null) {
                ivPhoto.setImageBitmap(thumbnailBitmap);
            }
        }
    }
    @Override
    public void onDestroy () {
        super.onDestroy();
        cursor.close();
        db.close();
    }
    public void clickedSaveData (View view){
        try {
            SQLiteOpenHelper routeDatabaseHelper = new RouteDatabaseHelper(this);
            //COMPLETED down
            SQLiteDatabase db = routeDatabaseHelper.getWritableDatabase();
            Long curDateLong=null;
            //COMPLETED insert is wrong
            //TODO change DATE format
            try {
                //dateFormat = new SimpleDateFormat(dateFormat);
                curDate = dateFormat.parse(tv_date.getText().toString());
                curDateLong = curDate.getTime()/1000;
            } catch (ParseException e) {
                Toast.makeText(this,"Parse date error"+e.getMessage(),Toast.LENGTH_LONG).show();
                curDateLong=null;
            }
            try {
                RouteDatabaseHelper.insertPoint(db, curDateLong, Double.parseDouble(latitudeEt.getText().toString()),
                        Double.valueOf(longitudeEt.getText().toString()), thumbnailBitmap,
                        descriptionEt.getText().toString());
            } catch (NullPointerException e) {
                Toast.makeText(this,"Insert error "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
            db.close();
        } catch (SQLException e ) {
            Toast.makeText(this,"SQLException:"+e.toString(),Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this,Pilgrim.class);
        startActivity(intent);
        finish();
    }
    public void clickedMyData (View view) {
        if (readingMode) {
            buttonNext.setVisibility(View.INVISIBLE);
            buttonPrevious.setVisibility(View.INVISIBLE);
            buttonSaveData.setVisibility(View.VISIBLE);
            buttonMakePhoto.setVisibility(View.VISIBLE);
            buttonDel.setVisibility(View.INVISIBLE);
            readingMode=false;
            cursor.close();
            db.close();
        } else{
            //cursor opening
            try {
                routeDatabaseHelper = new RouteDatabaseHelper(this);
                db = routeDatabaseHelper.getWritableDatabase();
                cursor = db.query("POINTS",new String[]
                        {"DATE","LATITUDE","LONGITUDE","DESCRIPTION","PHOTO","_ID"},
                        null,null,null,null,"DATE");
            } catch (SQLException e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
            if (cursor.moveToFirst()) {
                cursor.move(position);
                descriptionEt.setText(cursor.getString(3));
                latitudeEt.setText(String.format(formatCoordinate,cursor.getFloat(1)));
                longitudeEt.setText(String.format(formatCoordinate,cursor.getFloat(2)));
                byte [] blob = cursor.getBlob(4);
                if (blob != null) {
                    ivPhoto.setImageBitmap(RouteDatabaseHelper.getPhoto(blob));
                }
                dataEt.setText(getDate(cursor.getLong(0))); //""+dateFormat.format(curDate));
            }
            buttonNext.setVisibility(View.VISIBLE);
            buttonPrevious.setVisibility(View.VISIBLE);
            buttonSaveData.setVisibility(View.INVISIBLE);
            buttonMakePhoto.setVisibility(View.INVISIBLE);
            buttonDel.setVisibility(View.VISIBLE);
            readingMode=true;
        }
    }
    public void clickedNext(View view) {
        if (cursor.moveToNext()) {
            //descriptionEt.setSelection(3);
            descriptionEt.setText(cursor.getString(3));
            //TODO check format lat ans long
            latitudeEt.setText(String.format(formatCoordinate, cursor.getFloat(1)));
            longitudeEt.setText(String.format(formatCoordinate, cursor.getFloat(2)));
            if (cursor.getBlob(4) !=null) {
                ivPhoto.setImageBitmap(RouteDatabaseHelper.getPhoto(cursor.getBlob(4)));
            } else {
                ivPhoto.setImageBitmap(null);
            }
            dataEt.setText(getDate(cursor.getInt(1))); //""+dateFormat.format(curDate));
        } else {
            Toast.makeText(this, "LastRow", Toast.LENGTH_LONG).show();
        }
    }
    public void clickedDel (View view) {
        try {
            //COMPLETED check is delete working
            db.execSQL("DELETE FROM POINTS WHERE _ID = " + cursor.getInt(5));
            cursor.close();
            readingMode=false;
            clickedMyData(view);
            //COMPLETED remake fields
        } catch (SQLException e) {
            Toast.makeText(this, "Delete exception:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void clickedPrevious (View view) {
        if (cursor.moveToPrevious()) {
            //descriptionEt.setSelection(3);
            descriptionEt.setText(cursor.getString(3));
            latitudeEt.setText(String.format(formatCoordinate, cursor.getFloat(1)));
            longitudeEt.setText(String.format(formatCoordinate, cursor.getFloat(2)));
            if (cursor.getBlob(4) != null) {
                ivPhoto.setImageBitmap(RouteDatabaseHelper.getPhoto(cursor.getBlob(4)));
            } else {
                ivPhoto.setImageBitmap(null);
            }
            dataEt.setText( getDate(cursor.getInt(1)));
        } else {
            Toast.makeText(this, "FirstRow", Toast.LENGTH_LONG).show();
        }
    }
    public void clickedMakePhoto (View view){
        PackageManager pm = this.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
        {Toast.makeText(this,"Please grant camera",Toast.LENGTH_LONG).show();}
        //TODO add 2 messages(!) to STRING.XML
        else {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager())!= null) {
        startActivityForResult(cameraIntent, CAMERA_REQUEST);} else {
        Toast.makeText(this,"Not apps for camera",Toast.LENGTH_LONG).show();}
        }
    }
    private String getDate(long timeFromDatabase) {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeFromDatabase);
        return dateFormat.format(calendar.getTime());
    }
}
