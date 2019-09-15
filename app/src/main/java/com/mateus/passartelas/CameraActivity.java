package com.mateus.passartelas;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mateus.passartelas.Classes.Collect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 2;
    Button bt;
    Bitmap bm1;
    ImageView ivPhotoTaken;
    private String pathToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ivPhotoTaken = findViewById(R.id.ivPhotoTaken);
        bt = findViewById(R.id.bt_takePicture);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Control.camera_taken = true;
                Control.camera_result = true;
                Intent intent = new Intent();
                intent.putExtra("PhotoTaken", bm1);
                finish();
            }
        });

        dispatchTakePictureIntent();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            photoFile = createPhotoFile();

            if(photoFile != null){
                pathToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.mateus.passartelas.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createPhotoFile() {
        File image = null;
        try{
            @SuppressLint("SimpleDateFormat") String name = new SimpleDateFormat(getString(R.string.date_format_pattern)).format(new Date());
            File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(name, ".jpg", storageDir);
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO){
            if(resultCode == RESULT_OK) {
                Control.camera_taken = true;
                Control.camera_result = true;
                Collect.bitmapFile = BitmapFactory.decodeFile(pathToFile);
            }

            finish();

        }
    }


}