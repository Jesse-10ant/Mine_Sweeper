package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ResultsActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle time_elapsed){
        super.onCreate(time_elapsed);
        setContentView(R.layout.results_activity);

        TextView results_tv = findViewById(R.id.top_message);

        int seconds_used = getIntent().getIntExtra("time_elapsed",0);
        String time_used_str = "Used " + seconds_used + " seconds";
        results_tv.setText(time_used_str);

    }
}
