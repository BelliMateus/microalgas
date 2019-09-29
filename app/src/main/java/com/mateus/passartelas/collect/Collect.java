package com.mateus.passartelas.collect;

import android.graphics.Bitmap;
import android.util.Log;


import com.mateus.passartelas.Control;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Collect {

    // Image
    public Bitmap bitmapFile;

    // Cellphone
    private String date;
    public String GPS;

    // Environment
    private int salinity;
    private int temperature;

    //TODO Get cup size

    // Cup
    private int diameter;
    private int height;


    public static void AddCollectToList(int salinity, int temperature, int diameter, int height){
        Collect collect = new Collect();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.UK);
        collect.date = sdf.format(new Date());
        collect.salinity = salinity;
        collect.temperature = temperature;
        collect.diameter = diameter;
        collect.height = height;
        collect.bitmapFile = Control.lastPhoto;

        CollectData.collect_list.add(collect);
    }

}
