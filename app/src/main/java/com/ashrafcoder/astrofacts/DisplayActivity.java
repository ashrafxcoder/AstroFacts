package com.ashrafcoder.astrofacts;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.system.Os;
import android.system.StructUtsname;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DisplayActivity extends AppCompatActivity {


    private Bitmap bitmap;
    ImageView apodImage;
    TextView apodTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        apodImage = (ImageView) findViewById(R.id.apod_photo);
        apodTitle = (TextView) findViewById(R.id.apod_title);
        Intent intent = getIntent();
        //Uri uri = intent.getData();
        String fileName = intent.getStringExtra("fileName");
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(fileName));
            apodImage.setImageBitmap(bitmap);
            apodTitle.setText(intent.getStringExtra("title"));

        } catch (IOException e)
        {
            Log.d("NASA ", e.getMessage());
        }
        //DatabaseUtils.dumpCursor(cursor);
        //setContentView(imageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            MidiManager m = (MidiManager) getSystemService(Context.MIDI_SERVICE);
            MidiDeviceInfo[] infos = m.getDevices();
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//           StructUtsname utsname = Os.uname();
//        }
//        new CountDownTimer(30000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
//                mTextField.setText("done!");
//            }
//        }.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            shouldShowRequestPermissionRationale(Manifest.permission.SET_WALLPAPER);
        }
    }


    private void showAlertWithText(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", null);
        builder.setTitle(title);
        builder.setMessage(text);
        AlertDialog alertDialog = builder.create();
        //showDialog(1);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.set_wallpaper)
        {
            try
            {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


