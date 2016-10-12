package com.example.radhika.demoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;


import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by Radhika on 02-05-2016.
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
    private static final int MAX_RESULTS = 10;

    private List<Stock> resultList = new ArrayList<Stock>();
        Context context;

    private static LayoutInflater inflater=null;
    public AutoCompleteAdapter(Context context) {
        // TODO Auto-generated constructor stub

        this.context=context;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Stock getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


public class Holder
{
    TextView name;
    TextView symbol;
}
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.autocomplete, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.Name)).setText(getItem(position).getName());
        ((TextView) convertView.findViewById(R.id.Symbol)).setText(getItem(position).getSymbol());
        return convertView;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Log.d("in filter", "in this::" + constraint);
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Stock> stocks = findStocks(context, constraint.toString());
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
                    resultList = (List<Stock>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private List<Stock> findStocks(Context context, String sym) {
        // GoogleBooksProtocol is a wrapper for the Google Books API
        Log.d("find stock","in this::"+sym);
            List<Stock> results=new ArrayList<>();
            String url1="http://midyear-freedom-127906.appspot.com/bingSearch.php?&Symbol="+sym;
        try {
            Log.d("before URL","in this::"+sym);
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept-Charset", "iso-8859-1");
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            Log.d("after URL", "in this::" + sym);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            urlConnection.disconnect();
            String result1=result.toString();
            String json = result.toString();

            json = json.replace("\\", "");
            json = json.substring(1);
            json = json.substring(0, json.length() - 1);
            JSONArray array=new JSONArray(json);
           /* result1=result1.substring(2, result1.length() - 2);
            result1=result1.replace("\\\"", "");
            result1=result1.concat(",");
            String[] s=result1.split(Pattern.quote("},"));*/
            Log.d("1 object", "in this::" +array.toString());
            for(int i=0;i<array.length();i++)
            {
              /*  s[i]=s[i].replace("{", "");
                s[i]=s[i].replace(",","");
                String[] alldata=s[i].split(",");
                String[] symo=alldata[0].split(":");
                String[] name=alldata[1].split(":");
                Log.d("1 object", "in this::" +s[i]);*/
                JSONObject obj=array.getJSONObject(i);
                Stock stock=new Stock();
                stock.setName(obj.getString("Name")+"("+obj.getString("Exchange")+")");
                stock.setSymbol(obj.getString("Symbol"));

                Log.d("stock", "in this::" + stock.getName());
                results.add(stock);
            }
            return results;
        }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        return null;
    }

}