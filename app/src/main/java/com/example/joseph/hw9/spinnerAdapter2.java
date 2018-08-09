package com.example.joseph.hw9;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Joseph on 11/30/17.
 */

public class spinnerAdapter2 extends ArrayAdapter {

    private LayoutInflater infalter;
    private String[] spinnerArray;
    private int resource;
    private int textViewResourceId;

    public spinnerAdapter2(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);

        this.spinnerArray = objects;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        infalter = LayoutInflater.from(context);
    }
    @Override
    public boolean isEnabled(int position) {
        return position != 0 && !getItem(position).equals("Order");
    }

    public boolean areAllItemsEnabled() {return false;}

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            Context mContext = this.getContext();
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        }

        TextView tv = (TextView) v.findViewById(android.R.id.text1);
        tv.setText(getItem(position).toString());
        tv.setHeight(100);
        if (!isEnabled(position)) tv.setTextColor(Color.GRAY);
        return v;
    }
}
