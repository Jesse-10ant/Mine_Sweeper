package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 10;
    private static final int ROW_COUNT = 12;
    private static final int FLAGS = 4;

    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;

    private TextView flagCountTextView;
    private TextView timerDisplay;
    private TextView shovelToggle;
    private Handler timer_handler;
    private Runnable timer_runner;
    private int second_elapsed = 0;
    private int flag_count = FLAGS;
    private boolean isFlagMode = false;


    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        flagCountTextView = findViewById(R.id.flagCount);
        timerDisplay = findViewById(R.id.timerLabel);
        TextView flagIcon = findViewById(R.id.flagIcon);
        TextView timerIcon = findViewById(R.id.clock_view);
        shovelToggle = findViewById(R.id.modeToggle);

        flag_count = FLAGS;
        updateFlagCount();


        timer_handler = new Handler();
        startTimer();

        cell_tvs = new ArrayList<TextView>();
        // add 120 dynamically created cells with LayoutInflater
        // 10 columns x  12 rows

        GridLayout grid = findViewById(R.id.gridLayout01);
        grid.setUseDefaultMargins(true);

        //Testing
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        grid.setBackgroundColor(Color.DKGRAY); // Ensure GridLayout is visible

        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i< ROW_COUNT; i++) {
            for (int j=0; j<COLUMN_COUNT; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);

                tv.setText(""); // Start with no text
                tv.setTextColor(Color.BLACK);
                tv.setOnClickListener(this::onClickTV);
              //tv.setTag(R.id.cell_position, i * COLUMN_COUNT + j );

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.rowSpec = GridLayout.spec(i, 1f);
                lp.columnSpec = GridLayout.spec(j, 1f);
                lp.width = 0;
                lp.height = 0;
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));

                grid.addView(tv, lp);
                cell_tvs.add(tv);
            }
        }
    }

    private void shovelSwitch(){
        isFlagMode = !isFlagMode;
        if(isFlagMode){
            shovelToggle.setText(getString(R.string.flag));
            shovelToggle.setTextColor(Color.RED);
        }else{
            shovelToggle.setText(getString(R.string.pick));
            shovelToggle.setTextColor(Color.BLACK);
      }
    }

    public void updateFlagCount(){
        flagCountTextView.setText(String.valueOf(flag_count));
    }

    private void startTimer(){
        timer_runner = new Runnable() {
            @Override
            public void run() {
                second_elapsed++;
                int seconds = second_elapsed;
                timerDisplay.setText(String.valueOf(seconds));
                timer_handler.postDelayed(this,1000);
            }
        };
        timer_handler.postDelayed(timer_runner,1000);
    }

    private void stopTimer(){
        timer_handler.removeCallbacks(timer_runner);
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;

        if(isFlagMode) {
            if (tv.getText().equals(getString(R.string.flag))) {
                tv.setText("");
                flag_count++;
            } else if (flag_count > 0) {
                tv.setText(getString(R.string.flag));
                flag_count--;
            }
            updateFlagCount();
        }else{
            tv.setBackgroundColor(Color.GRAY);
            tv.setText(String.valueOf(i + j));
        }

    }
}