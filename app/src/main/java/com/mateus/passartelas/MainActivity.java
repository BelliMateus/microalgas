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
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.mateus.passartelas.ui.telas.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_CAMERA = 1;

    FloatingActionButton fb_next;
    FloatingActionButton fb_after;
    ViewPager viewPager;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

                if(i == 0){
                    fb_after.setVisibility(View.GONE);
                    fb_next.setVisibility(View.VISIBLE);
                }else if(i == 1){
                    if(Control.camera_taken) {
                        fb_after.setVisibility(View.VISIBLE);
                        fb_next.setVisibility(View.VISIBLE);
                    }else{
                        fb_after.setVisibility(View.VISIBLE);
                        fb_next.setVisibility(View.GONE);
                    }
                }

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

        fb_after.setVisibility(View.GONE);

        fb_after.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        fb_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                Log.d("Camera", Control.permission_camera + "");
                if(Control.permission_camera) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else
                    getPermissions();
            }
        });

        getPermissions();

    }


    public void getPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_CAMERA);
        } else {
            Control.permission_camera = true;
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
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && (
                    grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Vai funcionar!!!", Toast.LENGTH_LONG).show();
                Control.permission_camera = true;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){
            if(Control.camera_result){
                Log.d("Intent", "Camera Result: True");
                Control.camera_result = false;
                viewPager.setCurrentItem(2);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if(hasFocus && Control.camera_result){
            if(viewPager.getCurrentItem() == 1){
                if(Control.camera_taken) {
                    fb_after.setVisibility(View.VISIBLE);
                    fb_next.setVisibility(View.VISIBLE);
                }else{
                    fb_after.setVisibility(View.VISIBLE);
                    fb_next.setVisibility(View.GONE);
                }
            }
        }

    }
}
