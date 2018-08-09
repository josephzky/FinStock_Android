package com.example.joseph.hw9;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.publishInstallAsync;

public class TabCurrent extends Fragment {

    public static int flagToLoad;
    DetailsListAdapter detailsListAdapter;
    ArrayList<DetailsRow> detailsList;
    View view;
    ListView listView;
    WebView webView;
    String todo;
    String todofb;
    Button button;
    Spinner spinner;
    ProgressBar progressBarView;
    String sym;
    double pri;
    double cng;
    double cng100;
    ImageButton fb;
    ImageButton filledButton;
    ImageButton emptyButton;
    ImageButton fbButton;
    CallbackManager callbackManager;
    TextView fail;
    String httpurl;
    String highChart_config;
    String export_url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.current_tab, container, false);
        fail =  view.findViewById(R.id.fail);
        listView = view.findViewById(R.id.detailsListView);
        progressBarView = view.findViewById(R.id.progressBar);
        filled();
        empty();

        if (flagToLoad == 0) {
            fail.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.GONE);
        } else if (flagToLoad == -1) {
            fail.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            Log.d("fail","asdfsdf");
        }
        else if (flagToLoad == 1) {
            Log.d("fail","but");

            filledButton = (ImageButton) view.findViewById(R.id.filled);
            emptyButton = (ImageButton) view.findViewById(R.id.empty);
            filledButton.setVisibility(View.GONE);
            emptyButton.setVisibility(View.VISIBLE);
            filledButton.setEnabled(false);
            emptyButton.setEnabled(false);
            for (int i = 0; i < MainActivity.favCompanys.size(); i++) {
                if (MainActivity.favCompanys.get(i).getSymbol()
                        .equals(MainActivity.searchText.toUpperCase())) {
                    filledButton.setVisibility(View.VISIBLE);
                    emptyButton.setVisibility(View.GONE);
                    break;
                }
            }

            listView.setVisibility(View.GONE);
            progressBarView.setVisibility(View.VISIBLE);
            fail.setVisibility(View.GONE);

            RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
            String url = "http://10.0.2.2:9090/?symbol=" + MainActivity.searchText + "&todo=details";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            DetailsRow stockSymbol = new DetailsRow("  Stock Symbol", response.optString("stockTickerSymbol"));
                            DetailsRow lastPrice = new DetailsRow("  Last Price", response.optString("lastPrice"));
                            DetailsRow change = new DetailsRow("  Change", response.optString("priceChange")+" ("+response.optString("changePercent")+"%)");
                            DetailsRow timestamp = new DetailsRow("  Timestamp", response.optString("timeStamps"));
                            DetailsRow open = new DetailsRow("  Open", response.optString("open"));
                            DetailsRow close = new DetailsRow("  Close", response.optString("previousClose"));
                            DetailsRow range = new DetailsRow("  Day's Range", response.optString("range1")+" - "+response.optString("range2"));
                            DetailsRow volume = new DetailsRow("  Volume", response.optString("volume"));
                            detailsList = new ArrayList<>();
                            detailsList.add(stockSymbol);
                            detailsList.add(lastPrice);
                            detailsList.add(change);
                            detailsList.add(timestamp);
                            detailsList.add(open);
                            detailsList.add(close);
                            detailsList.add(range);
                            detailsList.add(volume);
                            sym = response.optString("stockTickerSymbol").toUpperCase();
                            pri = Double.parseDouble(response.optString("lastPrice"));
                            cng = Double.parseDouble(response.optString("priceChange"));
                            cng100 = Double.parseDouble(response.optString("changePercent"));

                            filledButton.setEnabled(true);
                            emptyButton.setEnabled(true);

                            detailsListAdapter =
                                    new DetailsListAdapter(getContext(), R.layout.details_listview, detailsList);
                            listView.setAdapter(detailsListAdapter);
                            fail.setVisibility(View.GONE);
                            progressBarView.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            flagToLoad = 0;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            flagToLoad = -1;
                            fail.setVisibility(View.VISIBLE);
                            progressBarView.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);
                            Log.d("error_TabCurrent_res", error.toString());
                        }
                    }
            );
            int socketTimeout = 20000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            mRequestQueue.add(jsonObjectRequest);
        }
        listView.setAdapter(detailsListAdapter);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        button = (Button) view.findViewById(R.id.changeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setTextColor(Color.GRAY);
                button.setEnabled(false);
                todo = String.valueOf(spinner.getSelectedItem());
                Log.d("todo", todo);
                webView = view.findViewById(R.id.hcWebView);
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
                        webView.loadUrl("javascript:showGraph(\""+ MainActivity.searchText +"\", \""+ todo +"\");");
                        fb(todo);
                    }
                });

            }
        });

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {


                button.setTextColor(Color.BLACK);
                button.setEnabled(true);
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        return view;
    }

//    public void fb() {
//        fbButton = view.findViewById(R.id.fb);
//        fbButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callbackManager = CallbackManager.Factory.create();
//                httpurl = "http://export.highcharts.com/";
//
//                final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("async", "true");
//                map.put("type", "image/jpg");
//                map.put("filename", "postPhoto");
//
//
//                highChart_config = "{\n" +
//                        "    xAxis: {\n" +
//                        "        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug',\n" +
//                        "            'Sep', 'Oct', 'Nov', 'Dec']\n" +
//                        "    },\n" +
//                        "    series: [{\n" +
//                        "        data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,\n" +
//                        "            194.1, 95.6, 54.4]\n" +
//                        "    }]\n" +
//                        "}";
//
//                map.put("options", highChart_config);
//
//                JSONObject jsonObject = new JSONObject((map));
//
//                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,httpurl, jsonObject,
//                        new Response.Listener<JSONObject>() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//
//                                export_url = httpurl + response;
//                                showFBDialog(export_url);
//
//                                Log.d("POSTRESPONSE", "response -> " + response.toString());
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.e("POSTERROR", error.getMessage(), error);
//                            }
//                        })
//                {
//                    @Override
//                    public Map<String, String> getHeaders() {
//                        HashMap<String, String> headers = new HashMap<String, String>();
//                        headers.put("Accept", "application/json");
//                        headers.put("Content-Type", "application/json; charset=UTF-8");
//                        return headers;
//                    }
//                };
//                requestQueue.add(jsonRequest);
//            }
//        });
//    }



    public void fb(String todo) {
        todofb = todo;
        fbButton = view.findViewById(R.id.fb);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = CallbackManager.Factory.create();
                Log.d("ttttttdofb",todofb);
                String export_url = "https://developers.facebook.com";
                if (todofb.equals("Price")) {
                    export_url = "http://export.highcharts.com/charts/chart.db62fd3dad4d4af8aa2dfb6d817095e8.png";
                } else if (todofb.equals("SMA")) {
                    export_url = "http://export.highcharts.com/charts/chart.7d246e95af164bdf8467fab05103b6eb.png";
                }else if (todofb.equals("EMA")) {
                    export_url = "http://export.highcharts.com/charts/chart.e9b05d16ce7347a085e5ed3d24abf8b4.png";
                }else if (todofb.equals("MACD")) {
                    export_url = "http://export.highcharts.com/charts/chart.5b607506d55548a682a787c8dd836393.png";
                }else if (todofb.equals("RSI")) {
                    export_url = "http://export.highcharts.com/charts/chart.609c674e05a74ef0aa3e7f19042865cd.png";
                }else if (todofb.equals("ADX")) {
                    export_url = "http://export.highcharts.com/charts/chart.81a89ac5a6bc41a8b0b969cb275f75b8.png";
                }else if (todofb.equals("CCI")) {
                    export_url = "http://export.highcharts.com/charts/chart.2af72db3cf3547dc81c970c617b434de.png";
                }
                showFBDialog(export_url);
            }
        });
    }

    public void empty() {
        emptyButton = view.findViewById(R.id.empty);
        emptyButton.setEnabled(true);
        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavCompany favCompany = new FavCompany(sym,pri,cng,cng100,MainActivity.favCompanys.size());
                MainActivity.favCompanys.add(favCompany);
                filledButton.setVisibility(View.VISIBLE);
                emptyButton.setVisibility(View.GONE);
                Log.d("ffffbbbbb","fffffbbbbb");

            }
        });
    }

    public void filled() {
        filledButton = view.findViewById(R.id.filled);
        filledButton.setEnabled(true);
        filledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < MainActivity.favCompanys.size(); i++) {
                    if (sym.equals(MainActivity.favCompanys.get(i).getSymbol())) {
                        MainActivity.favCompanys.remove(i);
                        break;
                    }
                }
                filledButton.setVisibility(View.GONE);
                emptyButton.setVisibility(View.VISIBLE);

            }
        });
    }

    public void showFBDialog(String export_url) {
        Log.d("share url", export_url);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(export_url))
                .build();
        ShareDialog shareDialog = new ShareDialog(getActivity());
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("result", "A");
                Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                Log.d("result", "B");
                Toast.makeText(getContext(), "Not posted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "Post error", Toast.LENGTH_SHORT).show();
            }
        });
        shareDialog.show(content, ShareDialog.Mode.FEED);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

