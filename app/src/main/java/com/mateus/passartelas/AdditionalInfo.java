package com.mateus.passartelas;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mateus.passartelas.collect.Collect;
import com.mateus.passartelas.collect.CollectData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AdditionalInfo extends AppCompatActivity {

    EditText etDepth;
    FloatingActionButton fabEndCollect;

    TextView tvLatitude, tvLongitude;

    private BroadcastReceiver broadcastReceiver;


    String lat, lon;

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    lat = intent.getExtras().get("latitude").toString();
                    lon = intent.getExtras().get("longitude").toString();

                    tvLatitude.setText(lat);
                    tvLongitude.setText(lon);
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        etDepth = findViewById(R.id.etDepth);
        fabEndCollect = findViewById(R.id.fabEndCollect);

        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);


        fabEndCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectCompleted();
            }
        });


    }

    void collectCompleted(){

        if(etDepth.getText().toString().equals("")){
            return;
        }else{
            CollectData.collect_list.get(CollectData.collect_list.size()-1).depth = etDepth.getText().toString();
            CollectData.collect_list.get(CollectData.collect_list.size()-1).latitude = lat;
            CollectData.collect_list.get(CollectData.collect_list.size()-1).longitude = lon;
            CollectData.collect_list.get(CollectData.collect_list.size()-1).imagePath = Control.pathPhoto;

            Collect collect = CollectData.collect_list.get(CollectData.collect_list.size()-1);

            String data =
                    collect.date + ";" +
                    collect.latitude + ";" +
                    collect.longitude + ";" +
                    collect.depth + ";" +
                    collect.temperature  + ";" +
                    collect.salinity  + ";" +
                    collect.diameter  + ";" +
                    collect.height  + ";" +
                    collect.imagePath + "\n";




            save(data);



            Intent intent_service = new Intent(getApplicationContext(), GPS_service.class);
            stopService(intent_service);

            Control.camera_taken = false;
            Control.lastPhoto = null;
            Control.photoDate = null;



            Intent newIntent = new Intent(AdditionalInfo.this, StartupActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        }
    }

    public void save(String text) {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput("collect.txt", MODE_APPEND);
            fos.write(text.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
