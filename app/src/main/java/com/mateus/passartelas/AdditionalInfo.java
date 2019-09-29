package com.mateus.passartelas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdditionalInfo extends AppCompatActivity {

    EditText etDepth;
    Button btEndCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        etDepth = findViewById(R.id.etDepth);
        btEndCollect = findViewById(R.id.btEndCollect);

        btEndCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
