package com.mateus.passartelas.ui.telas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mateus.passartelas.CameraActivity;
import com.mateus.passartelas.Control;
import com.mateus.passartelas.R;


public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int CAMERA_TAKE_PIC = 100;


    private PageViewModel pageViewModel;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        View root;

        if(index == 1) {
            root = inflater.inflate(R.layout.tutorial_1, container, false);

        }else if(index == 2) {
            root = inflater.inflate(R.layout.frag_cam, container, false);

            root.findViewById(R.id.takePicture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(view.getContext(), CameraActivity.class),
                            CAMERA_TAKE_PIC);
                }
            });

        }else{
            root = inflater.inflate(R.layout.frag_add_info, container, false);
        }

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        Control.camera_result = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            Log.d("ResultadoCamera", "Entrou");
            Control.camera_result = true;

    }
}
