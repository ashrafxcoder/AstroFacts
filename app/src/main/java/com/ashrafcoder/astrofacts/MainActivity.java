package com.ashrafcoder.astrofacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class MainActivity extends AppCompatActivity {

    private static final String NASA_APOD_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=2017-01-13";//&date=2017-01-13
    private ProgressBar progressBar;
    private TextView progressStatus;
    private TextView title;
    private TextView desc;
    private ImageData imageData;
    private ImageView imageView;
    private String fileName;
    String imageUri;
    private boolean isHdImage;
    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected()) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    downloadData(NASA_APOD_URL);
                }
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        //DisplayManager displayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);

        //DownloadManager downloadManager =

        progressStatus = (TextView) findViewById(R.id.progresStatus);
        title = (TextView) findViewById(R.id.imageTitle);
        desc = (TextView) findViewById(R.id.imageDesc);


        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(false);

        imageView = (ImageView) findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(imageUri)) {
                    Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                    intent.putExtra("fileName", imageUri);
                    intent.putExtra("title", imageData.title);
                    //Log.d("NASA imageURi:", imageUri);
                    startActivity(intent);
                }
            }
        });




        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });




        String s = getPreferences();
        if (s != null && !TextUtils.isEmpty(s)){
            imageView.setImageURI(Uri.parse(s));
            imageUri = s;
        }else {
            if (isConnected()){
                downloadData(NASA_APOD_URL);
            }else{
                Toast.makeText(this, "Network connection not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadData(String url) {

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressStatus.setText("Downloading image...");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject root = new JSONObject(result);
                    String url = root.getString("url");
                    imageData = new ImageData();
                    imageData.title = root.getString("title");
                    imageData.desc = root.getString("explanation");//explanation

                    Log.d("NASA Url:", url);
                    String lastSegment = Uri.parse(url).getLastPathSegment();
                    Log.d("NASA Uri Las Segment:", lastSegment);

                    fileName = lastSegment;
                    downloadUrl = url;
                    downloadImage(url);

                } catch (JSONException e) {
                    Log.d("JSON Error:", e.getMessage());
                }
                Log.d("NASA:", result);
            }
        });

        File dir = getFilesDir();
        Toast.makeText(this, dir.toString(), Toast.LENGTH_LONG).show();
    }


    public File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("", "Directory not created");
        }
        return file;
    }



    private void downloadImage(String url) {
        DownloadTask task = new DownloadTask();
        task.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id){
            default:
                return super.onOptionsItemSelected(item);
            case R.id.action_image_hd:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_save_image:
                saveImage();
                return true;
            case R.id.action_settings:
                return true;

        }
    }



    private class DownloadTask extends AsyncTask<String, Integer, Bitmap> {

        protected Bitmap doInBackground(String... urls) {

            String urlStr = urls[0];

            try {


                if (!URLUtil.isHttpsUrl(urlStr)) {
                    urlStr = urlStr.replace("http://", "https://");
                }
                URL url = new URL(urlStr);
                final HttpsURLConnection httpCon = (HttpsURLConnection) url.openConnection();

                //httpCon.setInstanceFollowRedirects(true);
                //httpCon.connect();
//                if(httpCon.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP){
//                    Log.d("Redirect:", httpCon.getHeaderFields() + "");
//                    String redirect =  httpCon.getHeaderField("Location");
//                    if (URLUtil.isValidUrl(redirect) && URLUtil.isHttpsUrl(redirect)){
//                        urlStr = redirect;
//                    }
//                    Log.d("Locatiom:", redirect + "");
//                    //httpCon.setInstanceFollowRedirects(true);
//                }


                //allowCrossDomainRedirects

                InputStream is = httpCon.getInputStream();
                int fileLength = httpCon.getContentLength();

                //mDialog.setMax(fileLength);
                Log.d("NASA file length:", fileLength + "");
                Log.d("NASA file length:", urlStr + "");

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead, totalBytesRead = 0;
                byte[] data = new byte[2048];

                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                    totalBytesRead += nRead;

                    Log.d("NASA loading:", ((totalBytesRead / (double) fileLength) * 100) + "");
                    publishProgress((int) ((totalBytesRead / (double) fileLength) * 100));
                }

                byte[] image = buffer.toByteArray();
                Log.d("NASA image length:", image.length + "");
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageData.bitmap = bitmap;

                return bitmap;
            } catch (Exception e) {
                Log.d("NASA exception:", e.getMessage());
                e.printStackTrace();

                if (e instanceof SSLException) {
                    downloadImage(downloadUrl);
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
            //Log.d("NASA:", progress[0] +  " bytes downloaded");
            progressBar.setProgress(progress[0]);
            progressStatus.setText(progress[0] + " % downloaded...");

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //show dialog
            //progressBar.setVisibility(View.INVISIBLE);
            progressBar.setProgress(100);
            progressStatus.setText("Download completed...");


            imageView.setImageBitmap(bitmap);
            Log.d("NASA:", "Image downloaded");
            title.setText(imageData.title);
            //desc.setText(imageData.desc);

        }
    }

    private void saveToPreferences(String file) {
        SharedPreferences preferences = getSharedPreferences("files", MODE_PRIVATE);
        SharedPreferences.Editor editor =  preferences.edit();
        editor.putString("todayFile", file);
        editor.commit();
    }

    private String getPreferences() {
        SharedPreferences preferences = getSharedPreferences("files", MODE_PRIVATE);
        String s = preferences.getString("todayFile", null);
        return s;
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    private void saveImage(){
       imageUri = MediaStore.Images.Media.insertImage(getContentResolver(), imageData.bitmap, imageData.title , imageData.title);
        //Bitmap bitmap = imageData.bitmap;
        saveToPreferences(imageUri);
    }

    private class ImageData{
        String title;
        String desc;
        Bitmap bitmap;
    }

    private boolean isConnected(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =  manager.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isConnected = true;
        }
        return isConnected;
    }
}


