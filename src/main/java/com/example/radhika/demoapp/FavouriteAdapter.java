package com.example.radhika.demoapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radhika on 04-05-2016.
 */
public class FavouriteAdapter extends BaseAdapter implements Filterable{
    Context context;
    LayoutInflater inflater;
    int layoutResourceId;
    List<Stock> data=null;


    public FavouriteAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public class ViewHolder
    {
        TextView col1;
        TextView col2;
        TextView col3;
        TextView col4;
        TextView col5;
    }


    public void removeItem(Stock key)
    {
        data.remove(key);
    }

    @Override
    public int getCount() {
        if(data!=null)
        return data.size();
        return 0;
    }

    @Override
    public Stock getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if(view==null)
        {
            holder=new ViewHolder();
            view=inflater.inflate(R.layout.favlistitem,null);
            holder.col1=(TextView)view.findViewById(R.id.compSymbol);
            holder.col2=(TextView)view.findViewById(R.id.complp);
            holder.col3=(TextView)view.findViewById(R.id.compchange);
            holder.col4=(TextView)view.findViewById(R.id.compName);
            holder.col5=(TextView)view.findViewById(R.id.compMp);
            view.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)view.getTag();
        }

        Stock s=getItem(position);
        SpannableStringBuilder builder = null;
        String d=s.getChange();
            d=d.substring(0,d.length()-1);
            Double c=Double.parseDouble(d);
            if(c>0) {
              holder.col3.setBackgroundColor(Color.GREEN);
            }
            else {
                holder.col3.setBackgroundColor(Color.RED);
            }
        holder.col1.setText(s.getSymbol());
        holder.col2.setText(s.getLastPrice());
        holder.col3.setText(s.getChange());
        holder.col4.setText(s.getName());
        holder.col5.setText(s.getMarketCap());
        return view;
    }



    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Log.d("in filter", "in this::" + constraint);
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Stock> stocks =new ArrayList<>();
                    Log.d("constraint","::"+constraint);
                    String[] sym=(constraint.toString()).split(";");

                    for(int i=0;i<sym.length;i++)
                    {
                        Stock sss=findStocks(context, sym[i].toString());
                        stocks.add(sss);
                    }
                    Log.d("in if after findStocks","in this::"+constraint);
                    // Assign the data to the FilterResults

                    filterResults.values = stocks;
                    filterResults.count = stocks.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                if (results != null && results.count > 0) {
                    Log.d("notify","in this::"+constraint);
                    data = (List<Stock>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private Stock findStocks(Context context, String sym) {
        // GoogleBooksProtocol is a wrapper for the Google Books API
        Log.d("find stock", "in this::" + sym);

        HttpURLConnection urlConnection = null;
        String URLString = "http://midyear-freedom-127906.appspot.com/bingSearch.php?&lookup=" + sym;
        try {
            URL url = new URL(URLString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(false);

            urlConnection.setRequestMethod("GET");

            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            urlConnection.disconnect();

            JSONObject s = new JSONObject(result.toString());
            Log.d("result", s.toString());
            String stockValue = s.getString("stockValue");
            s = new JSONObject(stockValue);


            Stock stock = new Stock();
            stock.setName(s.getString("Name"));
            stock.setSymbol(s.getString("Symbol"));

            String marketCap = s.getString("MarketCap");
            Double cpd = Double.parseDouble(marketCap);
            cpd = cpd / 1000000;
            String cp1 = String.format("%.2f", cpd);
            cpd = Double.parseDouble(cp1);
            String unit = null;
            //	tdId="#tdMarketCap"+symbol;
            if (cpd < 0.005)
                unit = ""; //none
            else {
                cpd = (cpd / 1000);
                cp1 = String.format("%.2f", cpd);
                cpd = Double.parseDouble(cp1);

                if (cpd < 0.005)
                    unit = " Million"; //million
                else
                    unit = " Billion"; //billion
            }

            stock.setMarketCap("Market Cap: "+cpd + unit);

            stock.setLastPrice("$"+s.getString("LastPrice"));
            String cp = s.getString("ChangePercent");
            cpd = Double.parseDouble(cp);
            cp = String.format("%.2f", cpd);




            stock.setChange( cp + "%");
            Log.d("stock add", stock.toString());
            return stock;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

