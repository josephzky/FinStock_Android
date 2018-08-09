
package com.example.joseph.hw9;

        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;

public class FavListAdapter extends ArrayAdapter<FavCompany> {
    private static final String TAG = "FavListAdapter";
    private Context mContext;
    int mResource;

    public FavListAdapter(Context context, int resource, ArrayList<FavCompany> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String symbol = getItem(position).getSymbol();
        double price = getItem(position).getPrice();
        double change = getItem(position).getChange();
        double changePercent = getItem(position).getChangePercent();
        int addOrder = getItem(position).getAddOrder();

//        String price = getItem(position).getPrice() + "";
//        String change = "" + getItem(position).getChange() +
//                "(" + getItem(position).getChangePercent() + "%)";

        FavCompany favCompany = new FavCompany(symbol, price, change, changePercent, addOrder);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (MainActivity.favCompanys.get(position).getChange() < 0) {
            convertView = inflater.inflate(R.layout.fav_listview_minus, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.fav_listview, parent, false);

        }

        TextView tvSymbol = (TextView) convertView.findViewById(R.id.symbolText);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.priceText);
        TextView tvChange = (TextView) convertView.findViewById(R.id.changeText);

        tvSymbol.setText(symbol);
        tvPrice.setText("" + price);
        tvChange.setText(change + "(" + changePercent + "%)");

        return convertView;
    }
}

