package com.ashrafcoder.astrofacts;

import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SlidingPaneLayoutActivity extends AppCompatActivity {


    private SlidingPaneLayout slidingPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_pane_layout);


        slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.activity_sliding_pane_layout);

        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d("Panel", "Panel slided. " + slideOffset);
            }

            @Override
            public void onPanelOpened(View panel) {
                if (panel.getId() == R.id.detailFragment){
                    //getFragmentManager().findFragmentById(R.id.detailFragment).setHasOptionsMenu(false);
                    //getFragmentManager().findFragmentById(R.id.sideFragment).setHasOptionsMenu(true);
                }
                Log.d("Panel", "Panel opened.");

            }

            @Override
            public void onPanelClosed(View panel) {
                if (panel.getId() == R.id.sideFragment){
                    //getFragmentManager().findFragmentById(R.id.sideFragment).setHasOptionsMenu(false);
                    //getFragmentManager().findFragmentById(R.id.detailFragment).setHasOptionsMenu(true);
                }
                Log.d("Panel", "Panel closed.");
            }
        });
    }
}
