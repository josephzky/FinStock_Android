package com.example.joseph.hw9;

/**
 * Created by Joseph on 11/25/17.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class TabHistorical extends Fragment {
    WebView webView;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historical_tab, container, false);
        textView = view.findViewById(R.id.hisfail);
        webView = view.findViewById(R.id.hsWebView);
        textView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl("file:///android_asset/androidGraph.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            public void onPageFinished(WebView view, String url)
            {
                webView.loadUrl("javascript:showGraph(\""+ MainActivity.searchText +"\", \"HS\");");
            }
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                textView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }
        });
        return view;
    }
}