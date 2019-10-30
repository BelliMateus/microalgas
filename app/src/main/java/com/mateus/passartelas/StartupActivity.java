package com.mateus.passartelas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mateus.passartelas.bluetooth.MainActivityBluetooth;
import com.mateus.passartelas.collect.Collect;
import com.mateus.passartelas.collect.CollectData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        if(!Control.loadedFiles){
            Control.loadedFiles = true;
            load();
        }

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

    public void load() {
        try {

            File f = getFileStreamPath("collect.txt");

            Log.i("MSG", "Lendo arquivo " + f.getAbsolutePath());

            if (f.exists()) {

                FileInputStream in = openFileInput("collect.txt");

                int tamanho = in.available();

                byte bytes[] = new byte[tamanho];

                in.read(bytes);

                String s = new String(bytes);



                String[] sSplit = s.split("\n");



                for(int i = 0; i < sSplit.length; i++){

                    String[] sAux = sSplit[i].split(";");
                    Collect collect = new Collect();
                    collect.date = sAux[0];
                    collect.latitude = sAux[1];
                    collect.longitude = sAux[2];
                    collect.depth = sAux[3];
                    collect.temperature = Integer.parseInt(sAux[4]);
                    collect.salinity = Integer.parseInt(sAux[5]);
                    collect.diameter = Integer.parseInt(sAux[6]);
                    collect.height = Integer.parseInt(sAux[7]);
                    collect.imagePath = sAux[8];
                    CollectData.collect_list.add(collect);
                    collect = null;

                }


                in.close();

            }

        } catch (Exception e) {

            Log.e("ERRO", e.getMessage());

        }
    }

}
