package com.example.joseph.hw9;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<FavCompany> favCompanys = new ArrayList<>();
    public static String searchText;
    String[] spinnerArray1;
    String[] spinnerArray2;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter adapter;
    FavListAdapter favListAdapter;
    ArrayList<String> stocks;
    Spinner spinner1;
    Spinner spinner2;
    Button getQuote;
    ImageButton refresh;
    Switch autoRefresh;
    Handler handler;
    Runnable ref;
    ListView listView;
    int delPos;
    ProgressBar mainProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainProgressBar = findViewById(R.id.mainProgressBar);
        mainProgressBar.setVisibility(View.INVISIBLE);
        toTabPage();
        clear();
        refresh();
        autoRefresh();

        favListAdapter = new FavListAdapter(this, R.layout.fav_listview, favCompanys);

        spinnerArray1 = new String[]{"Sort By", "Default", "Symbol", "Price", "Change"};
        spinnerArray2 = new String[]{"Order", "Ascending", "Descending"};
        spinner1 = (Spinner) findViewById(R.id.sortBy);
        spinner1.setAdapter(new spinnerAdapter1(this, android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, spinnerArray1));
        spinner2 = (Spinner) findViewById(R.id.order);
        spinner2.setAdapter(new spinnerAdapter2(this, android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, spinnerArray2));

        listView = findViewById(R.id.favListView);

        registerForContextMenu(listView);

        listView.setAdapter(favListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TabCurrent.flagToLoad = 1;
                searchText = favCompanys.get(position).getSymbol();
                Intent slide = new Intent(MainActivity.this, TabActivity.class);
                startActivity(slide);
            }
        });
        listView.setLongClickable(true);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    delPos = pos;
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, listView);
                    popupMenu.getMenuInflater().inflate(R.menu.delmenu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Log.d("mmmmmmm", menuItem.toString());
                            if (menuItem.toString().equals("Yes")) {
                                Log.d("aaaaaarg", delPos+"");
                                favCompanys.remove(delPos);
                                favListAdapter.notifyDataSetChanged();
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });

        autoCompleteTextView = findViewById(R.id.autoComplete);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mainProgressBar.setVisibility(View.VISIBLE);
                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                stocks = new ArrayList<String>();
                String url = "http://10.0.2.2:9090/?symbol=" + autoCompleteTextView.getText().toString() + "&todo=auto";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response.optString("autoComp"));
                                    int count = 0;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        stocks.add(i, jsonArray.getJSONObject(i).optString("display"));
                                        if (count >=4) {
                                            break;
                                        }
                                        count++;
                                    }

                                } catch (JSONException e) {
                                    Log.e("Transforming", "There was an error packaging JSON", e);
                                }
                                adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.select_dialog_item, stocks);
                                autoCompleteTextView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                mainProgressBar.setVisibility(View.INVISIBLE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                mainProgressBar.setVisibility(View.INVISIBLE);
                                Log.d("errorOnResponse", error.toString());
                            }
                        }
                );
                mRequestQueue.add(jsonObjectRequest);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        autoCompleteTextView.setThreshold(1);

        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                favSorting();
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                Log.d("sssssselect", spinner2.getSelectedItem().toString());
                favSorting();
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void refresh() {
        Log.d("refresh","asd");
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < favCompanys.size(); i++) {
                    refreshSingle(i);
                    Log.d("rrrrrrefresh",""+i);

                }
                favSorting();
            }
        });
    }

    public void refreshSingle(int i) {
        mainProgressBar.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = "http://10.0.2.2:9090/?symbol=" + favCompanys.get(i).getSymbol() + "&todo=details";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for (int j = 0; j < favCompanys.size(); j++) {
                            if (favCompanys.get(j).getSymbol().equals(response.optString("stockTickerSymbol").toUpperCase())) {
                                favCompanys.get(j).setPrice(Double.parseDouble(response.optString("lastPrice")));
                                favCompanys.get(j).setChange(Double.parseDouble(response.optString("priceChange")));
                                favCompanys.get(j).setChangePercent(Double.parseDouble(response.optString("changePercent")));
                                mainProgressBar.setVisibility(View.INVISIBLE);
                                break;
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        mainProgressBar.setVisibility(View.INVISIBLE);
                        Log.d("errorOnResponse", error.toString());
                    }
                }
        );
        int socketTimeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        mRequestQueue.add(jsonObjectRequest);
    }

    public void autoRefresh() {
        autoRefresh = findViewById(R.id.autoRefresh);
        autoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    handler = new Handler();
                    ref = new Runnable() {
                        public void run() {
                            refresh();
                            handler.postDelayed(ref, 8000);
                        }
                    };
                    handler.post(ref);
                } else {
                    handler.removeCallbacks(ref);
                }
            }
        });
    }

    public void toTabPage() {
        getQuote = (Button) findViewById(R.id.getQuote);
        getQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((autoCompleteTextView.getText().toString().trim()).equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter a stock name or symbol", Toast.LENGTH_LONG).show();
                } else {
                    TabCurrent.flagToLoad = 1;

                    if (autoCompleteTextView.getText().toString().indexOf("-") == -1) {
                        searchText = autoCompleteTextView.getText().toString();
                    } else {
                        searchText = autoCompleteTextView.getText().toString().
                                substring(0, autoCompleteTextView.getText().toString().indexOf("-") - 1);
                    }
                    Intent slide = new Intent(MainActivity.this, TabActivity.class);
                    startActivity(slide);
                }
            }
        });
    }

    public void clear() {
        getQuote = (Button) findViewById(R.id.clear);
        getQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });
    }

    public void favSorting() {
        String sortby = spinner1.getSelectedItem().toString();
        String order = spinner2.getSelectedItem().toString();

        if (sortby.equals("Default")) {
            for (int i = 1; i < favCompanys.size(); i++) {
                for (int j = 0; j < i; j++) {
                    if (favCompanys.get(i).getAddOrder() < favCompanys.get(j).getAddOrder()) {
                        favCompanys.add(j, favCompanys.get(i));
                        favCompanys.remove(i + 1);
                        break;
                    }
                }
            }
        } else if (sortby.equals("Symbol")) {
            if (order.equals("Ascending")) {
                for (int i = 1; i < favCompanys.size(); i++) {
                    for (int j = 0; j < i; j++) {
                        if (favCompanys.get(i).getSymbol().compareTo(favCompanys.get(j).getSymbol()) < 0 ) {
                            favCompanys.add(j, favCompanys.get(i));
                            favCompanys.remove(i + 1);
                            break;
                        }
                    }
                }
            } else if (order.equals("Descending")) {
                for (int i = 1; i < favCompanys.size(); i++) {
                    for (int j = 0; j < i; j++) {
                        if (favCompanys.get(i).getSymbol().compareTo(favCompanys.get(j).getSymbol()) > 0 ) {
                            favCompanys.add(j, favCompanys.get(i));
                            favCompanys.remove(i + 1);
                            break;
                        }
                    }
                }
            }
        } else if (sortby.equals("Price")) {
            if (order.equals("Ascending")) {
                for (int i = 1; i < favCompanys.size(); i++) {
                    for (int j = 0; j < i; j++) {
                        if (favCompanys.get(i).getPrice() < favCompanys.get(j).getPrice()) {
                            favCompanys.add(j, favCompanys.get(i));
                            favCompanys.remove(i + 1);
                            break;
                        }
                    }
                }
            } else if (order.equals("Descending")) {
                for (int i = 1; i < favCompanys.size(); i++) {
                    for (int j = 0; j < i; j++) {
                        if (favCompanys.get(i).getPrice() > favCompanys.get(j).getPrice()) {
                            favCompanys.add(j, favCompanys.get(i));
                            favCompanys.remove(i + 1);
                            break;
                        }
                    }
                }

            }
        } else if (sortby.equals("Change")) {
            if (order.equals("Ascending")) {
                for (int i = 1; i < favCompanys.size(); i++) {
                    for (int j = 0; j < i; j++) {
                        if (favCompanys.get(i).getChange() < favCompanys.get(j).getChange()) {
                            favCompanys.add(j, favCompanys.get(i));
                            favCompanys.remove(i + 1);
                            break;
                        }
                    }
                }

            } else if (order.equals("Descending")) {
                for (int i = 1; i < favCompanys.size(); i++) {
                    for (int j = 0; j < i; j++) {
                        if (favCompanys.get(i).getChange() > favCompanys.get(j).getChange()) {
                            favCompanys.add(j, favCompanys.get(i));
                            favCompanys.remove(i + 1);
                            break;
                        }
                    }
                }

            }
        }
        favListAdapter.notifyDataSetChanged();

    }


}
