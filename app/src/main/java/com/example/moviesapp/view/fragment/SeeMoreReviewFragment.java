package com.example.moviesapp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.moviesapp.R;
import com.example.moviesapp.data.model.moviesReview.MovieReviewData;
import com.example.moviesapp.databinding.FragmentSeeMoreReviewBinding;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.view.acitivty.base.BaseFragments;


public class SeeMoreReviewFragment extends BaseFragments {
    public MovieReviewData movieReviewData;
    private FragmentSeeMoreReviewBinding fragmentSeeMoreReviewBinding;

    public SeeMoreReviewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentSeeMoreReviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_see_more_review, container, false);
        View view = fragmentSeeMoreReviewBinding.getRoot();

        startWebView(movieReviewData.getUrl());
        return view;
    }

    private void startWebView(String url) {

        WebSettings settings = fragmentSeeMoreReviewBinding.seeMoreReviewFragmentWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        fragmentSeeMoreReviewBinding.seeMoreReviewFragmentWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        fragmentSeeMoreReviewBinding.seeMoreReviewFragmentWebView.getSettings().setBuiltInZoomControls(true);
        fragmentSeeMoreReviewBinding.seeMoreReviewFragmentWebView.getSettings().setUseWideViewPort(true);
        fragmentSeeMoreReviewBinding.seeMoreReviewFragmentWebView.getSettings().setLoadWithOverviewMode(true);

        HelperMethod.showProgressDialog(getActivity(), "");
        fragmentSeeMoreReviewBinding.seeMoreReviewFragmentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                HelperMethod.dismissProgressDialog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Error:" + description, Toast.LENGTH_SHORT).show();
            }
        });
        fragmentSeeMoreReviewBinding.seeMoreReviewFragmentWebView.loadUrl(url);
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
