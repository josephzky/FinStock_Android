package com.example.joseph.hw9;

/**
 * Created by Joseph on 11/25/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TabNews extends Fragment {
    NewsListAdapter newsListAdapter;
    ListView listView;
    TextView textView;
    ProgressBar progressBar;
    ArrayList<NewsColumn> newsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.news_tab, container, false);
        textView = view.findViewById(R.id.newsfail);
        listView = view.findViewById(R.id.newsListView);
        progressBar = view.findViewById(R.id.progressBar);
        Log.d("FirstVISHhoisdfhoiasdhf","VISBsadfhs");

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newsList = new ArrayList<>();
                    String urlString = "https://seekingalpha.com/api/sa/combined/"+ MainActivity.searchText +".xml";
                    URL url = new URL(urlString);
                    HttpURLConnection connect = (HttpURLConnection)url.openConnection();
                    connect.setReadTimeout(10000);
                    connect.setConnectTimeout(15000);
                    connect.setRequestMethod("GET");
                    connect.setDoInput(true);
                    connect.connect();

                    InputStream is = connect.getInputStream();
                    XMLPullParserHandler parser = new XMLPullParserHandler();
                    newsList = parser.parse(is);
                    is.close();

                    Activity activity = (Activity) listView.getContext();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("VISHhoisdfhoiasdhf","VISBsadfhs");

                            listView.setVisibility(View.VISIBLE);
                        }
                    });

                    activity = (Activity) textView.getContext();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setVisibility(View.GONE);
                        }
                    });

                    activity = (Activity) progressBar.getContext();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
                catch (Exception e) {
                    Activity activity = (Activity) listView.getContext();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setVisibility(View.GONE);
                        }
                    });

                    activity = (Activity) textView.getContext();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setVisibility(View.VISIBLE);
                        }
                    });

                    activity = (Activity) progressBar.getContext();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                    e.printStackTrace();

                }
                Activity activity = (Activity) listView.getContext();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsListAdapter = new NewsListAdapter(getContext(), R.layout.news_listview, newsList);
                        listView.setAdapter(newsListAdapter);
                        newsListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String urlString = newsList.get(position).getLink();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(urlString);
                intent.setData(content_url);
                startActivity(intent);
            }
        });

        return view;
    }

}
