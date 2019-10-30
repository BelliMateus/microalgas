package com.mateus.passartelas;


import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.mateus.passartelas.collect.Collect;
import com.mateus.passartelas.collect.CollectData;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;


public class CollectList extends AppCompatActivity {

    ListView rvCollectList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_list);


        rvCollectList = findViewById(R.id.lvCollect);
        CustomAdapter customAdapter = new CustomAdapter();
        rvCollectList.setAdapter(customAdapter);
    }


}

class CustomAdapter extends  BaseAdapter {


    @Override
    public int getCount() {
        return CollectData.collect_list.size();
    }

    @Override
    public Object getItem(int i) {
        return CollectData.collect_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = View.inflate(viewGroup.getContext(), R.layout.collect_list_item, null);

        ImageView ivPhoto = view.findViewById(R.id.ivAlgaeList);
        ImageView ivStatus = view.findViewById(R.id.tvSatus);
        TextView tvLat = view.findViewById(R.id.tvLat);
        TextView tvLong = view.findViewById(R.id.tvLong);
        TextView tvData = view.findViewById(R.id.tvData);
        TextView tvTemp = view.findViewById(R.id.tvTemp);
        TextView tvSal = view.findViewById(R.id.tvSal);


        File image = new File(CollectData.collect_list.get(i).imagePath);
        if (image.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(CollectData.collect_list.get(i).imagePath);
            ivPhoto.setImageBitmap(bitmap);
            Log.d("LEITURA2", CollectData.collect_list.size() + "");
        }

        tvTemp.setText(CollectData.collect_list.get(i).temperature + "");

        String aux = CollectData.collect_list.get(i).date.split("_")[0];

        tvData.setText(aux.substring(0, 2) + "/" + aux.substring(2, 4) + "/" + aux.substring(4, 8));
        tvSal.setText(CollectData.collect_list.get(i).salinity + "");
        tvLat.setText(CollectData.collect_list.get(i).latitude);
        tvLong.setText(CollectData.collect_list.get(i).longitude);


        return view;
    }
}