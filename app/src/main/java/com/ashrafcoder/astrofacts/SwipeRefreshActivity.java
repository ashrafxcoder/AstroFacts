package com.ashrafcoder.astrofacts;

import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ashrafcoder.astrofacts.dialog.HiDialogFragment;

import java.util.Random;

public class SwipeRefreshActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView monthsListView;
    private ArrayAdapter<String> adpater;
    private HiDialogFragment dialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);

        dialogFragment = new HiDialogFragment();
        dialogFragment.setOnHiDialogActionListener(new HiDialogFragment.OnHiDialogActionListener() {
            @Override
            public void sayHi(String s) {
                Toast.makeText(SwipeRefreshActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        monthsListView = (ListView) findViewById(R.id.swipeList);

        String[] months = getResources().getStringArray(R.array.months);
        adpater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, months);

        monthsListView.setAdapter(adpater);
        monthsListView.setVerticalScrollbarPosition(ListView.SCROLLBAR_POSITION_DEFAULT);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.green), getResources().getColor(R.color.blue), getResources().getColor(R.color.orange));
    }

    private void refreshContent() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                adpater = new ArrayAdapter<String>(SwipeRefreshActivity.this, android.R.layout.simple_list_item_1, getMonths());
                monthsListView.setAdapter(adpater);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 4000);
    }

    public String[] getMonths() {
        String[] months = getResources().getStringArray(R.array.months);
        String[] randomMonths = new String[months.length];
        for (int i=0; i< months.length;i++){
            int randIndex = new Random().nextInt(months.length -1);
            randomMonths[i] = months[randIndex];
        }

        return randomMonths;
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
                dialogFragment.show(getFragmentManager(), "TAG");

                return true;

        }
    }
}
