package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalproject.covidtracker.CovidInfoTrackerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.cardProjectCovid19InfoTracker).setOnClickListener(v->{
            startActivity(new Intent(this, CovidInfoTrackerActivity.class));
        });
    }
}