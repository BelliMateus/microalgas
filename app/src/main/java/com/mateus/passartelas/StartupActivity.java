package com.mateus.passartelas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.mateus.passartelas.bluetooth.MainActivityBluetooth;

public class StartupActivity extends AppCompatActivity {

    Button btGuided, btNotGuided, btGather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btGuided = findViewById(R.id.btGuided);
        btNotGuided = findViewById(R.id.btNotGuided);
        btGather = findViewById(R.id.btGather);

        btGuided.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGathering(true);
            }
        });

        btNotGuided.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGathering(false);
            }
        });

        btGather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectList();
            }
        });
    }


    private void startGathering(boolean guided){
        if(guided) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }
    }

    private void collectList(){
        Intent intent = new Intent(this, CollectList.class);
        startActivity(intent);
    }

}
