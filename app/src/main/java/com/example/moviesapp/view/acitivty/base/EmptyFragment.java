package com.example.moviesapp.view.acitivty.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moviesapp.view.acitivty.base.BaseFragments;


import static com.example.moviesapp.data.api.ApiClient.getClient;


public class EmptyFragment extends BaseFragments {

    public EmptyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        setUpActivity();
//        getClient();

//        View view = inflater.inflate(R.layout.fragment_empty, container, false);
//        ButterKnife.bind(this, view);


//        return view;
//    }


    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
