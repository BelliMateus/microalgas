package com.mateus.passartelas;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mateus.passartelas.collect.CollectData;

public class AdditionalInfo extends AppCompatActivity {

    EditText etDepth;
    FloatingActionButton fabEndCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        etDepth = findViewById(R.id.etDepth);
        fabEndCollect = findViewById(R.id.fabEndCollect);

        fabEndCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectCompleted();
            }
        });

    }

    void collectCompleted(){

        if(etDepth.getText().toString().equals("")){
            return;
        }else{
            CollectData.collect_list.get(CollectData.collect_list.size()-1).depth = etDepth.getText().toString();
        }

        Intent newIntent = new Intent(AdditionalInfo.this,StartupActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }

}
