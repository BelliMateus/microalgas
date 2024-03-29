package com.mateus.passartelas;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mateus.passartelas.bluetooth.MainActivityBluetooth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 2;
    FloatingActionButton bt_photo, bt_next, bt_after;
    Bitmap bm1;
    ImageView ivPhotoTaken;
    private String pathToFile;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        ivPhotoTaken = findViewById(R.id.iv_photo);
        bt_photo = findViewById(R.id.bt_takePicture);
        bt_next = findViewById(R.id.fab_next_camera);
        bt_after = findViewById(R.id.fab_after_camera);

        if(Control.camera_taken) {
            bt_next.setVisibility(View.VISIBLE);
            ivPhotoTaken.setImageBitmap(Control.lastPhoto);
        }else
            bt_next.setVisibility(View.GONE);

        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        bt_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Intent intent = new Intent(this, MainActivityBluetooth.class);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        /*File folder = new File(Environment.getExternalStorageDirectory()+"/Microalgas");
        if(!folder.exists()){
            folder.mkdir();
        }*/

        // GPS Service
        Intent intent_service = new Intent(getApplicationContext(), GPS_service.class);
        startService(intent_service);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            photoFile = createPhotoFile();

            if(photoFile != null){
                //pathToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.mateus.passartelas.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createPhotoFile() {
        File image = null;
        try{
            Control.photoDate = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.UK).format(new Date());
            //File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            //File storageDir = getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory()+"/Microalgas");
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(Control.photoDate, ".jpg", storageDir);
            pathToFile = image.getAbsolutePath();
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            Control.camera_taken = true;
            bt_next.setVisibility(View.VISIBLE);
            Control.lastPhoto = BitmapFactory.decodeFile(pathToFile);
            Control.pathPhoto = pathToFile;
            ivPhotoTaken.setImageBitmap(Control.lastPhoto);
        }
    }


}