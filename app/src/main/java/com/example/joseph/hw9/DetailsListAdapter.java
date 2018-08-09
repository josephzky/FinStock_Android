package com.example.joseph.hw9;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joseph on 11/25/17.
 */

public class DetailsListAdapter extends ArrayAdapter<DetailsRow> {
    private static final String TAG = "DetailsListAdapter";
    private Context mContext;
    int mResource;

    public DetailsListAdapter(Context context, int resource, ArrayList<DetailsRow> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItem(position).getTitle();
        String value = getItem(position).getValue();

        DetailsRow detailsRow = new DetailsRow(title, value);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (position == 2) {
            if (value.substring(0, 1).equals("-")) {
                convertView = inflater.inflate(R.layout.details_listview_down, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.details_listview_up, parent, false);
            }
        } else {
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.titleText);
        TextView tvValue = (TextView) convertView.findViewById(R.id.valueText);

        tvTitle.setText(title);
        tvValue.setText(value);

        return convertView;
    }
}

