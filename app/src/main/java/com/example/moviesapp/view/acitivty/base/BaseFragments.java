package com.example.moviesapp.view.acitivty.base;

import androidx.fragment.app.Fragment;


public class BaseFragments extends Fragment {

    public BaseActivity baseActivity;
    public BaseFragments baseFragment;

    public void setUpActivity() {
        baseActivity = (BaseActivity) getActivity();
        baseActivity.baseFragment = this;
    }

    public void onBack() {// el on back de 3shan ab2a 3mlt on back fe fragmentmesh fe activity
        baseActivity.superBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpActivity();
    }

}
