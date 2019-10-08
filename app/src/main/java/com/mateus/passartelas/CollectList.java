package com.mateus.passartelas;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mateus.passartelas.collect.Collect;
import com.mateus.passartelas.collect.CollectData;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class CollectList extends AppCompatActivity {

    ListView rvCollectList;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_list);

        rvCollectList = findViewById(R.id.lvCollect);

        CustomAdapter customAdapter = new CustomAdapter();
        rvCollectList.setAdapter(customAdapter);
        context = getApplicationContext();
    }


}

class CustomAdapter extends BaseAdapter{

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

        ivPhoto.setImageBitmap(CollectData.collect_list.get(i).bitmapFile);
        tvTemp.setText(CollectData.collect_list.get(i).temperature + "");
        tvData.setText(CollectData.collect_list.get(i).date.split("-")[0]);
        tvSal.setText(CollectData.collect_list.get(i).salinity + "");
        tvLat.setText(CollectData.collect_list.get(i).latitude );
        tvLong.setText(CollectData.collect_list.get(i).longitude);


        return view;
    }
}