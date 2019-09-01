package com.mateus.passartelas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mateus.passartelas.ui.telas.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERM_WRITE_STORAGE = 100;

    FloatingActionButton fb_next;
    FloatingActionButton fb_after;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if(i == 1 && !Control.camera_taken){
                    fb_next.setVisibility(View.GONE);
                }
                else{
                    fb_after.setVisibility(View.VISIBLE);
                    fb_next.setVisibility(View.VISIBLE);
                }

                //if(Control.camera_result) viewPager.setCurrentItem(i+1);
            }


            @Override
            public void onPageSelected(int i) {



            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        viewPager.setAdapter(sectionsPagerAdapter);
        fb_next = findViewById(R.id.fab_next);
        fb_after = findViewById(R.id.fab_last);

        fb_after.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
            }

        });


        fb_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

            }
        });


        // Camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERM_WRITE_STORAGE);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "Permissão", Toast.LENGTH_LONG).show();
                }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d("Intent", "Camera Result: " + Control.camera_result);
        if(Control.camera_result){
            Control.camera_result = false;
            viewPager.setCurrentItem(2);
        }

    }
}
