package com.example.liz.rocketfight;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {
    TextView textView,textView2,textView3,textView4;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //setting the values to the textViews
        String scoreList01 = "1."+sharedPreferences.getInt("score1",0);
        String scoreList02 = "2."+sharedPreferences.getInt("score2",0);
        String scoreList03 = "3."+sharedPreferences.getInt("score3",0);
        String scoreList04 = "4."+sharedPreferences.getInt("score4",0);
        textView.setText(scoreList01);
        textView2.setText(scoreList02);
        textView3.setText(scoreList03);
        textView4.setText(scoreList04);
    }
}
