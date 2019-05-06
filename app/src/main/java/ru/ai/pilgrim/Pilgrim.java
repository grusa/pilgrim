package ru.ai.pilgrim;
/**
 * @author sgrushin70@gmail.com
 * @version 1.0
 */
import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.textclassifier.TextLinks;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.View;
import java.util.ArrayList;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class Pilgrim extends AppCompatActivity {
    private TextView mTextMessage;
    private FloatingActionButton buttonFab;
    Bitmap pointBitmap;
    ConstraintLayout contaner;
    RecyclerView rv;
    PointAdapter pointAdapter;
    ArrayList<Point> points = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilgrim);
        mTextMessage = findViewById(R.id.message);
        buttonFab = findViewById(R.id.floatingActionButton);
        contaner = findViewById(R.id.container);
        rv=  findViewById(R.id.points_recyclerview);
        rv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        pointAdapter = new PointAdapter(this,points);
        retrieve();
        PackageManager pm = this.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
            Toast.makeText(this,"Not granted permission to GPS location",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean  onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.download:
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://3.121.182.9/getcountpoints.php";
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) ==
                        PackageManager.PERMISSION_GRANTED) {
                JsonArrayRequest request = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("Test JSON","Response.Listener OK");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Test JSON","Error");
                            }
                });
                queue.add(request);}
                Snackbar.make(getCurrentFocus(),"Test",Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(menuItem);
            case R.id.nature:

                return true;
            case R.id.dolmen:

                return true;
            case R.id.churches:

                return true;
            default: return super.onOptionsItemSelected(menuItem);
        }
    }
    public void onClickedFab (View view) {
            callPointActivity(view,1);
        }
    private void callPointActivity (View view, int callRule) {
        Intent intent = new Intent(this, PointActivity.class);
        intent.putExtra("callRule",callRule);
        startActivity(intent);
    }
    private void retrieve() {
      DBadapter db=new DBadapter(this);
      //OPEN
      db.open();
      points.clear();
      //SELECT
      Cursor c=db.getAllPoints();
      //LOOP THRU THE DATA ADDING TO ARRAYLIST
      while (c.moveToNext())
      {
          //int id=c.getInt(0);
          String description=c.getString(1);
          //CREATE PLAYER
          byte [] blob = c.getBlob(2);
          pointBitmap = RouteDatabaseHelper.getPhoto(blob);
          Point p=new Point(description, pointBitmap);
          //ADD TO PLAYERS
          points.add(p);
      }
//COMPLETED Bitmap not showing
      //SET ADAPTER TO RV
      if(!(points.size()<1))
      {
          rv.setAdapter(pointAdapter);
      }
    db.close();
  }
}

