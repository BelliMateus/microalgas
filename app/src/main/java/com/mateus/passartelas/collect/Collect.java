package com.mateus.passartelas.collect;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;


import com.mateus.passartelas.Control;
import com.mateus.passartelas.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Collect {

    // Image
    public Bitmap bitmapFile;
    public String imagePath;

    // Cellphone
    public String date;
    public String latitude;
    public String longitude;
    public String depth = "0";

    // Environment
    public int salinity = 0;
    public int temperature = 0;

    //TODO Get cup size

    // Cup
    public int diameter;
    public int height;


    public static void AddCollectToList(int salinity, int temperature, int diameter, int height){
        Collect collect = new Collect();

        collect.date = Control.photoDate;
        collect.salinity = salinity;
        collect.temperature = temperature;
        collect.diameter = diameter;
        collect.height = height;
        collect.bitmapFile = Control.lastPhoto;



        CollectData.collect_list.add(collect);
        Log.i("CollectList", CollectData.collect_list.size()+"");
    }

}
