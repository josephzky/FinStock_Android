package com.example.joseph.hw9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsListAdapter extends ArrayAdapter<NewsColumn> {
    private static final String TAG = "NewsListAdapter";
    private Context mContext;
    int mResource;

    public NewsListAdapter(Context context, int resource, ArrayList<NewsColumn> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItem(position).getTitle();
        String author = getItem(position).getAuthor();
        String date = getItem(position).getDate();
        String link = getItem(position).getLink();

        NewsColumn newsColumn = new NewsColumn(title, author, date, link);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.newsTitleText);
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.newsAuthorText);
        TextView tvDate = (TextView) convertView.findViewById(R.id.newsDateText);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvDate.setText(date);

        return convertView;
    }
}

