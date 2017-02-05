package com.ashrafcoder.astrofacts;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.effect.Effect;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SpeechActivity extends AppCompatActivity implements View.OnClickListener{

    private TextToSpeech textToSpeech;

    private EditText textToSpeak;
    private Button pronounceBtn;
    private Button clearTextBtn;
    private SeekBar seekBar;

    private SQLiteDatabase mDatabase;
    private File mDatabaseFile;
    private static final int CURRENT_DATABASE_VERSION = 42;

    private static final String sString1 = "this is a test";
    private static final String sString2 = "and yet another test";
    private static final String sString3 = "this string is a little longer, but still a test";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        textToSpeak = (EditText) findViewById(R.id.textToSpeak);
        pronounceBtn = (Button) findViewById(R.id.pronounceBtn);
        clearTextBtn = (Button) findViewById(R.id.clearTextBtn);
        seekBar = (SeekBar) findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                /*
                Speech rate. 1.0 is the normal speech rate,
                lower values slow down the speech (0.5 is half the normal speech rate),
                greater values accelerate it (2.0 is twice the normal speech rate).
                * */

                switch (progress){
                    case 0:
                        textToSpeech.setSpeechRate(0.1f); //slower
                        break;
                    case 1:
                        textToSpeech.setSpeechRate(0.5f); //slow
                        break;
                    case 2:
                        textToSpeech.setSpeechRate(1.0f); //Normal
                        break;
                    case 3:
                        textToSpeech.setSpeechRate(2.0f); //fase
                        break;
                    case 4:
                        textToSpeech.setSpeechRate(3.0f); //faster
                        break;
                    default:
                        textToSpeech.setSpeechRate(1.0f);
                        break;
                }

                //textToSpeak.setText(progress);
                Toast.makeText(SpeechActivity.this, progress + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //seekBar.setProgress(2);




        File dbDir = getDir("tests", Context.MODE_PRIVATE);



            mDatabaseFile = new File(dbDir, "database_test.db");

            if (mDatabaseFile.exists()) {
                mDatabaseFile.delete();
            }
            mDatabase = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile.getPath(), null);
            mDatabase.setVersion(CURRENT_DATABASE_VERSION);

        pronounceBtn.setOnClickListener(this);
        clearTextBtn.setOnClickListener(this);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);


                }
            }
        });


    }

    private void populateDefaultTable() {
        mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, data TEXT);");

        mDatabase.execSQL("INSERT INTO test (data) VALUES ('" + sString1 + "');");
        mDatabase.execSQL("INSERT INTO test (data) VALUES ('" + sString2 + "');");
        mDatabase.execSQL("INSERT INTO test (data) VALUES ('" + sString3 + "');");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.pronounceBtn){
            if (!TextUtils.isEmpty(textToSpeak.getText())){
                //textToSpeech.speak()
                textToSpeech.speak(textToSpeak.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }





            if (Build.VERSION.SDK_INT >= 21) {
//                Set<Voice> voices = textToSpeech.getVoices();
//                StringBuilder builder = new StringBuilder();
//                for(Voice vc : voices){
//                    if (vc.getName().contains("US") || vc.getName().contains("UK") || true) {
//                        builder.append(vc.getName());
//                        builder.append(System.lineSeparator());
//                    }
//                }
                //List<TextToSpeech.EngineInfo> engines = textToSpeech.getEngines();

//                for(TextToSpeech.EngineInfo engine : engines){
//                    builder.append(engine.name);
//                }



                //Voice voice = new Voice("en-gb-x-rjs#male_2-local", Locale.US, Voice.QUALITY_HIGH, Voice.LATENCY_HIGH, false, null);
                //textToSpeech.setVoice(voice);
                //Toast.makeText(SpeechActivity.this, builder.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else if(v.getId() == R.id.clearTextBtn){
            textToSpeak.setText("");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_speech, menu);
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
            case R.id.action_us_locale_male:

                return true;
            case R.id.action_uk_male:
                return true;

        }
    }


    @Override
    protected void onPause() {

        if (textToSpeech != null){
            textToSpeech.shutdown();
            textToSpeech.stop();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    //textToSpeech.setVoice()


                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        super.onResume();
    }
}
