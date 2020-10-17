package com.example.moviesapp.view.acitivty;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.moviesapp.R;
import com.example.moviesapp.adapter.MovieAdapter;
import com.example.moviesapp.data.local.Room.FavoriteItem;
import com.example.moviesapp.data.local.Room.RoomDao;
import com.example.moviesapp.data.model.moviesWithFilter.MoviesWithFilter;
import com.example.moviesapp.databinding.ActivityHomeCycleBinding;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.helper.sharedPreference;
import com.example.moviesapp.service.NetworkChangeReceiver;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.fragment.FavoriteFragment;
import com.example.moviesapp.view.fragment.MovieFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.data.local.Room.RoomManger.getInstance;
import static com.example.moviesapp.helper.Constant.API_KEY;
import static com.example.moviesapp.helper.HelperMethod.isNetworkConnected;

public class HomeCycleActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    String DARK_KEY = "DARK_KEY";

    private Snackbar snack;
    private NetworkChangeReceiver mNetworkReceiver;
    private RoomDao roomDao;
    private FavoriteFragment favoriteFragment = new FavoriteFragment();
    private List<FavoriteItem> favoriteItem = new ArrayList<>();

//    MovieAdapter movieAdapter;

    private ActivityHomeCycleBinding activityHomeCycleBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityHomeCycleBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_cycle);

        HelperMethod.replace(new MovieFragment(), getSupportFragmentManager(), R.id.home_cycle_activity_fl_container
                , null, "MovieFragment");

        snack = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE);
        mNetworkReceiver = new NetworkChangeReceiver();
        roomDao = getInstance(this).roomDao();

        setSupportActionBar(activityHomeCycleBinding.homeCycleActivityToolBar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityHomeCycleBinding.homeCycleActivityDrawer,
                activityHomeCycleBinding.homeCycleActivityToolBar, R.string.drawer_open, R.string.drawer_close);
        activityHomeCycleBinding.homeCycleActivityDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        activityHomeCycleBinding.homeCycleActivityNavView.setNavigationItemSelectedListener(this);

//        if (sharedPreference.LoadBoolean(this, DARK_KEY)) {
//            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dark:
//                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
//                    case Configuration.UI_MODE_NIGHT_YES:
//                        sharedPreference.SaveData(HomeCycleActivity.this, DARK_KEY, false);
//                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                        Toast.makeText(this, "DARK IS DISABLED", Toast.LENGTH_SHORT).show();
//                        break;
//                    case Configuration.UI_MODE_NIGHT_NO:
//                        sharedPreference.SaveData(HomeCycleActivity.this, DARK_KEY, true);
//                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                        Toast.makeText(this, "DARK IS ENABLED", Toast.LENGTH_SHORT).show();
//                        break;
//                }
                break;
            case R.id.nav_favourite:
                activityHomeCycleBinding.homeCycleActivityToolBar.setTitle(R.string.favorite);
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        favoriteItem = roomDao.getAll();
                        favoriteFragment.movieDataList = favoriteItem;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HelperMethod.replace(favoriteFragment, getSupportFragmentManager(),
                                        R.id.home_cycle_activity_fl_container, null, null);
                            }
                        });
                    }
                });
                break;
            case R.id.nav_home:
                displayedFragment("MovieFragment");
                activityHomeCycleBinding.homeCycleActivityToolBar.setTitle(R.string.movies);
                break;
            case R.id.nav_share:
//                activityHomeCycleBinding.homeCycleActivityToolBar.setTitle(R.string.share);
                break;
            case R.id.nav_about_app:
//                activityHomeCycleBinding.homeCycleActivityToolBar.setTitle(R.string.about);
                break;
        }
        activityHomeCycleBinding.homeCycleActivityDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayedFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment.isVisible() && fragment != null) {
            activityHomeCycleBinding.homeCycleActivityDrawer.close();
            Log.wtf("navigationSelection", "drawer closed");
        } else {
            baseFragment.onBack();
            Log.wtf("navigationSelection", "backed stack");
        }
    }

    public void cancelDrawerSwipe() {
        activityHomeCycleBinding.homeCycleActivityDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void activeDrawerSwipe() {
        activityHomeCycleBinding.homeCycleActivityDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void setVisibilityToolBar(int visibility) {
        activityHomeCycleBinding.homeCycleActivityToolBar.setVisibility(visibility);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
