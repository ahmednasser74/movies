package com.example.moviesapp.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.R;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class HelperMethod {
    private HelperMethod() {
        //to close access on this class to can't make objects
    }

    private static ProgressDialog checkDialog;
    public static AlertDialog alertDialog;
    private String path;
    BaseActivity baseActivity;

    public static void replace(Fragment fragment, FragmentManager supportFragmentManager, int id, String addToBackStack, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(id, fragment,tag);
        transaction.addToBackStack(addToBackStack);
        transaction.commit();
    }

    public static void replaceFragmentWithAnimation(Fragment fragment, FragmentManager getChildFragmentManager, int id, String fromWhere) {
        FragmentTransaction transaction = getChildFragmentManager.beginTransaction();

        if (fromWhere == "l") {
            transaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
        if (fromWhere == "r") {
            transaction.setCustomAnimations(R.anim.enter_from_right,
                    R.anim.exit_to_left);
        }
        if (fromWhere == "t") {
            transaction.setCustomAnimations(R.anim.slide_out_down,
                    R.anim.slide_in_down);
        }
        if (fromWhere == "b") {
            transaction.setCustomAnimations(R.anim.slide_in_up,
                    R.anim.slide_out_up);
        }
        if (fromWhere == "rr") {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
//        if(fromWhere=="t"){
//            transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);}
//        if(fromWhere=="b"){
//            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);}
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void hideKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if ((((Activity) context).getCurrentFocus() != null) && (((Activity) context).getCurrentFocus().getWindowToken() != null)) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    //show when enter fragment auto focus on edit text
    public static void showKeyboard(Context context, View view) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void imageBtnHidden(Context context, RecyclerView recyclerView, ImageButton imageButton) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                    w ana bnzl yzhr w ana btl3 ykhtfy
                if (dy > 0) {
                    imageButton.setVisibility(View.VISIBLE);
//                    imageButton.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_down_to_up));
                    hideKeyboard(context);
                } else {
                    imageButton.setVisibility(View.GONE);
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        imageButton.setVisibility(View.VISIBLE);

    }

    public static void floatBtnHidden(Context context, RecyclerView recyclerView, FloatingActionButton floatingActionButton) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.setVisibility(View.GONE);
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_down_to_up));
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        floatingActionButton.setVisibility(View.VISIBLE);

    }

    public static void floatMenuBtnHidden(Context context, RecyclerView recyclerView, FabSpeedDial floatingActionButton) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.setVisibility(View.GONE);
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_down_to_up));
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        floatingActionButton.setVisibility(View.VISIBLE);

    }

    public static boolean isNetworkConnected(Activity context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void showProgressDialog(Activity activity, String title) {
        try {
            checkDialog = new ProgressDialog(activity);
            checkDialog.setMessage(String.valueOf(title));
            checkDialog.show();
            checkDialog.setContentView(R.layout.dialog_progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(checkDialog.getWindow()).setBackgroundDrawableResource(R.color.albumTransparent);
            }
            checkDialog.setIndeterminate(false);
            checkDialog.setCancelable(false);
        } catch (Exception e) {
        }
    }

    public static void onLoadImageFromUrl(ImageView imageView, String URl, Context context) {
        Glide.with(context)
                .load(URl)
                .into(imageView);
    }

    public static void onLoadImageFromUrl(CircleImageView circleImageView, String URl, Context context) {
        Glide.with(context)
                .load(URl)
                .into(circleImageView);
    }

    public static void dismissProgressDialog() {
        try {
            checkDialog.dismiss();
        } catch (Exception e) {

        }
    }

    public static void showSnackBar(Activity activity, String text) {
        Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show();
    }

}