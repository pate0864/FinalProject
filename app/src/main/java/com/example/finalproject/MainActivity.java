package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.finalproject.owlbotdictionary.OwlbotDictionaryActivity;

import com.example.finalproject.pexels.PexelsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CardView) findViewById(R.id.cardProjectOwlbotDictionary)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OwlbotDictionaryActivity.class));
            }
        });

        findViewById(R.id.cardProjectPexels).setOnClickListener(v->{
            startActivity(new Intent(this, PexelsActivity.class));
        });

    }
}