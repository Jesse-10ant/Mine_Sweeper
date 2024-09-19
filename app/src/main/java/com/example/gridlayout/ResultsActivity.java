package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;


public class ResultsActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle time_elapsed){
        super.onCreate(time_elapsed);
        setContentView(R.layout.results_activity);


        TextView top_result = findViewById(R.id.top_message);
        TextView center_result = findViewById(R.id.center_message);
        TextView bottom_result = findViewById(R.id.bottom_message);
        Button restart_button = findViewById(R.id.restart_button);

        int seconds_used = getIntent().getIntExtra("time_elapsed",0);
        String result = getIntent().getStringExtra("result");


        String time_used_str = "Used " + seconds_used + " seconds";
        top_result.setText(time_used_str);

        if("win".equals(result)){
            center_result.setText("You Won");
            bottom_result.setText("Good Job!");
        }else{
            center_result.setText("You Blew Up");
            bottom_result.setText("Try Again!");
        }

        restart_button.setOnClickListener(this::onRestartClick);
    }

    public void onRestartClick(View view){
        restart();
    }
    private void restart(){
        Intent restart = new Intent(this,MainActivity.class);
        startActivity(restart);
        finish();
    }
}
