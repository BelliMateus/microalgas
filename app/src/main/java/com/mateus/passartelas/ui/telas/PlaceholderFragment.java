package com.mateus.passartelas.ui.telas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mateus.passartelas.CameraActivity;
import com.mateus.passartelas.Control;
import com.mateus.passartelas.collect.CollectData;
import com.mateus.passartelas.R;

import java.util.Objects;


public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int CAMERA_TAKE_PIC = 100;
    private PageViewModel pageViewModel;
    ImageView ivTakePhoto;

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

        }else if(index == 2){

            root = inflater.inflate(R.layout.tutorial_2, container, false);

        }else if(index == 3){

            root = inflater.inflate(R.layout.tutorial_3, container, false);

        }

        else{

            root = inflater.inflate(R.layout.frag_cam, container, false);

            ImageView iv = root.findViewById(R.id.ivPhotoTaken);

            try {
                iv.setImageBitmap(Control.lastPhoto);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            root.findViewById(R.id.takePicture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(getActivity(), CameraActivity.class), CAMERA_TAKE_PIC);
                }
            });

        }

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}

