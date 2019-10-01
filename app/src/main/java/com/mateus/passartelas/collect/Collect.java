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
    public String date;
    public String GPS;
    public String depth;

    // Environment
    public int salinity;
    public int temperature;

    //TODO Get cup size

    // Cup
    public int diameter;
    public int height;


    public static void AddCollectToList(int salinity, int temperature, int diameter, int height){
        Collect collect = new Collect();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss", Locale.UK);
        collect.date = sdf.format(new Date());
        collect.salinity = salinity;
        collect.temperature = temperature;
        collect.diameter = diameter;
        collect.height = height;
        collect.bitmapFile = Control.lastPhoto;

        CollectData.collect_list.add(collect);
        Log.i("CollectList", CollectData.collect_list.size()+"");
    }

}
