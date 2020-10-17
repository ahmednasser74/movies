package com.example.moviesapp.view.acitivty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.moviesapp.R;
import com.example.moviesapp.databinding.ActivitySplashCycleBinding;
import com.example.moviesapp.helper.sharedPreference;
import com.example.moviesapp.view.acitivty.base.BaseActivity;

public class SplashCycleActivity extends BaseActivity {

    private ActivitySplashCycleBinding activitySplashCycleBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashCycleBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_cycle);

        activitySplashCycleBinding.splashCycleImg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_down_to_up));
        activitySplashCycleBinding.splashCycleTv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_down_to_up));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashCycleActivity.this, HomeCycleActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);

//        if (sharedPreference.LoadBoolean(this, "DARK_KEY")) {
//            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            splashCycleRelMain.setBackgroundColor(Color.BLACK);
//        } else {
//            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            splashCycleRelMain.setBackgroundColor(Color.WHITE);
//        }
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
