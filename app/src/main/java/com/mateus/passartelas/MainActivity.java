package com.mateus.passartelas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mateus.passartelas.bluetooth.MainActivityBluetooth;
import com.mateus.passartelas.ui.telas.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_CAMERA = 1;

    FloatingActionButton fb_next;
    FloatingActionButton fb_after;
    ViewPager viewPager;
    ImageView ivTakePhoto;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager);


        ivTakePhoto = findViewById(R.id.ivPhotoTaken);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

                if(i < 3) {
                    fb_after.setVisibility(View.VISIBLE);
                    fb_next.setVisibility(View.VISIBLE);
                }else{
                    fb_after.setVisibility(View.VISIBLE);
                    fb_next.setVisibility(View.GONE);
                    if(Control.camera_taken) {
                        fb_after.setVisibility(View.VISIBLE);
                        fb_next.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onPageSelected(int i) {}

            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        viewPager.setAdapter(sectionsPagerAdapter);

        fb_next = findViewById(R.id.fab_next);
        fb_after = findViewById(R.id.fab_last);

        fb_after.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                if(viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }else{
                    finish();
                }
            }
        });

        fb_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                Log.d("Camera", Control.permission_camera + "");
                if(Control.permission_camera) {
                    if(viewPager.getCurrentItem() != 2)
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    else
                        startActivity(new Intent(getApplication(), CameraActivity.class));

                }else
                    getPermissions();
            }
        });

        if(Control.camera_taken) viewPager.setCurrentItem(3);

        getPermissions();

    }


    public void getPermissions(){

        if(Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH},
                        REQUEST_CODE_CAMERA);
            } else {
                Control.permission_camera = true;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //TODO Open Settings
            return true;
        }else if(id == R.id.action_about){
            //TODO Open About
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && (
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                grantResults[4] == PackageManager.PERMISSION_GRANTED
            )) {

                Control.permission_camera = true;

            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if(hasFocus){


            viewPager.setCurrentItem(0);

            if(Control.camera_result) {

                Control.camera_result = false;

                if (viewPager.getCurrentItem() == 4) {
                    if (Control.camera_taken) {
                        fb_after.setVisibility(View.VISIBLE);
                        fb_next.setVisibility(View.VISIBLE);

                    } else {
                        fb_after.setVisibility(View.VISIBLE);
                        fb_next.setVisibility(View.GONE);
                    }
                }

                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }
        }

    }
}
