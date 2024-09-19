package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 10;
    private static final int ROW_COUNT = 12;
    private static final int BOMBS = 4;
    private static final int FLAGS = BOMBS;

    private ArrayList<Block> game_blocks;

    private TextView flagCountTextView;
    private TextView timerDisplay;
    private TextView shovelToggle;

    private Handler timer_handler;

    private Runnable timer_runner;

    private int second_elapsed = 0;
    private int flag_count = FLAGS;
    private boolean isFlagMode = false;
    private boolean isGameActive = true;

    private int dpToPixel() {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(2 * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flagCountTextView = findViewById(R.id.flagCount);
        timerDisplay = findViewById(R.id.timerLabel);
        shovelToggle = findViewById(R.id.modeToggle);

        shovelToggle.setOnClickListener(this::onShovelToggleClick);
        flag_count = FLAGS;
        updateFlagCount();


        timer_handler = new Handler();
        startTimer();

        game_blocks = new ArrayList<>();
        // add 120 dynamically created cells with LayoutInflater
        // 10 columns x  12 rows

        GridLayout grid = findViewById(R.id.gridLayout01);
        grid.setUseDefaultMargins(true);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        grid.setBackgroundColor(Color.GRAY);

        int total_cell = ROW_COUNT * COLUMN_COUNT;
        LayoutInflater li = LayoutInflater.from(this);

        List<TextView> block_text_view = new ArrayList<>();
        for (int i = 0; i< ROW_COUNT; i++) {
            for (int j=0; j<COLUMN_COUNT; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
                tv.setText(""); // Start with no text
                tv.setTextColor(Color.BLACK);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.rowSpec = GridLayout.spec(i, 1f);
                lp.columnSpec = GridLayout.spec(j, 1f);
                lp.width = 0;
                lp.height = 0;
                lp.setMargins(dpToPixel(), dpToPixel(), dpToPixel(), dpToPixel());

                grid.addView(tv, lp);
                block_text_view.add(tv);
            }
        }

        for(int i =0; i < total_cell; i++){
            Block block = new Block(i, block_text_view.get(i), this);
            game_blocks.add(block);
        }

        deployBombs();
    }

    private void deployBombs(){
        List<Integer> index = new ArrayList<>();
        for(int i = 0; i < game_blocks.size(); i++){
            index.add(i);
        }
        Collections.shuffle(index);
        for(int i = 0; i < BOMBS; i++){
           Block block =  game_blocks.get(index.get(i));
           block.giveBomb();
            TextView tv = game_blocks.get(index.get(i)).getTextView();
            tv.setBackgroundColor(Color.CYAN);

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

    public void startTimer(){
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

    public void stopTimer(){
        timer_handler.removeCallbacks(timer_runner);
        isGameActive = false;
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<game_blocks.size(); n++) {
            if (game_blocks.get(n).getTextView() == tv)
                return n;
        }
        return -1;
    }

    public int countAdjacentBombs(int index){
        int count =0;
        int row = index / COLUMN_COUNT;
        int col = index % COLUMN_COUNT;

        for(int i = row - 1; i <= row + 1; i++){
            for (int j = col - 1; j <= col + 1; j++){
                if(i>=0 && i < ROW_COUNT && j >= 0 && j < COLUMN_COUNT){
                    if(i == row && j == col)continue;
                    int adjacent_blocks = i * COLUMN_COUNT + j;
                    if(adjacent_blocks < game_blocks.size()){
                        if(game_blocks.get(adjacent_blocks).isBomb()){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public void revelSafeBlocks(int index){
        int row = index / COLUMN_COUNT;
        int col = index % COLUMN_COUNT;

        for(int i = row - 1; i <= (row + 1); i++){
            for(int j = col - 1; j <= col + 1; j++){
                if(i >= 0 && i < ROW_COUNT && j>=0 && j < COLUMN_COUNT){
                    int adjacent_index = i * COLUMN_COUNT + j;
                    Block adjacent_block = game_blocks.get((adjacent_index));
                    TextView tv = adjacent_block.getTextView();
                    if(!adjacent_block.isBomb() && tv.isEnabled()){
                        int bomb_count = countAdjacentBombs(adjacent_index);
                        tv.setText(String.valueOf(bomb_count));
                        tv.setBackgroundColor(Color.GRAY);
                        tv.setEnabled(false);
                        if(bomb_count == 0){
                            tv.setText("");
                            revelSafeBlocks(adjacent_index);
                        }
                    }
                }
            }
        }
    }

    public void onShovelToggleClick(View view){
        shovelSwitch();
    }

    public void explodeAllBombs(){
        for(int i = 0; i < game_blocks.size(); i++){
           Block block =  game_blocks.get(i);
           TextView text_view = block.getTextView();
            if(block.isBomb()){
              text_view.setText(R.string.mine);
              text_view.setBackgroundColor(Color.RED);
            }
            //text_view.setEnabled(false);
        }
    }

    public void onClickTV(View view){
       if(!isGameActive){
            Intent intent = new Intent(this , ResultsActivity.class);
            Bundle time_elapsed = getIntent().getExtras();
            if (time_elapsed != null) {
                time_elapsed.putInt("time_elapsed",second_elapsed);
            }
            if (time_elapsed != null) {
                intent.putExtras(time_elapsed);
            }
            startActivity(intent);
            finish();
            return;
        }
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        Block block = game_blocks.get(n);
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
            if (tv.getText().equals(getString(R.string.flag))) {
                return;
            }else{
                block.onClick();
            }
            tv.setBackgroundColor(Color.GRAY);
        }

    }
}
