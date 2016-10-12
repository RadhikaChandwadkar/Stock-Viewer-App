package com.example.radhika.demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link CurrentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView stockInfo;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
   private ImageView img;
    Bitmap bitmap = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment CurrentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentFragment newInstance(String param1) {
        CurrentFragment fragment = new CurrentFragment();
        Bundle args = new Bundle();
        args.putString("jsonData", param1);
        Log.d("data new instance", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrentFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List<Stock> data=new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_current, container, false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("jsonData");
            Log.d("data oncreate",mParam1);
            stockInfo=(ListView)rootView.findViewById(R.id.stockDetails);

            try
            {
                JSONObject stockDetails= new JSONObject(mParam1);
                if(stockDetails!=null)
                {


                    data.add(new Stock("Name",stockDetails.getString("Name")));

                    data.add(new Stock("Symbol",stockDetails.getString("Symbol")));
                    data.add(new Stock( "lastprice",stockDetails.getString("LastPrice")));
                    String cp=stockDetails.getString("ChangePercent");
                    Double cpd=Double.parseDouble(cp);
                    cp=String.format("%.2f", cpd);

                    String cp1;
                    cp1 = stockDetails.getString("Change");
                    cpd=Double.parseDouble(cp1);
                    cp1=String.format("%.2f", cpd);

                    data.add( new Stock( "change",cp1+"("+cp+"%)"));
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
                    SimpleDateFormat oldDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss 'UTC'Z yyyy");
                    String oldDate=stockDetails.getString("Timestamp");
                    Date newd=oldDateFormat.parse(oldDate);
                    String n=newDateFormat.format(newd);
                    data.add(new Stock("Timestamp",n ));

                    String marketCap=stockDetails.getString("MarketCap");
                    cpd=Double.parseDouble(marketCap);
                    cpd=cpd / 1000000;
                    cp1=String.format("%.2f", cpd);
                    cpd=Double.parseDouble(cp1);
                    String unit=null;
                    //	tdId="#tdMarketCap"+symbol;
                    if (cpd < 0.005)
                        unit = "" ; //none
                    else
                    {
                        cpd = (cpd / 1000);
                        cp1=String.format("%.2f", cpd);
                        cpd=Double.parseDouble(cp1);

                        if (cpd < 0.005)
                            unit= " Million"; //million
                        else
                           unit= " Billion"; //billion
                    }


                    data.add(new Stock( "marketcap",cpd+unit));
                    data.add(new Stock( "volume",stockDetails.getString("Volume")));

                    cp=stockDetails.getString("ChangePercentYTD");
                    cpd=Double.parseDouble(cp);
                    cp=String.format("%.2f", cpd);

                     cp1=stockDetails.getString("ChangeYTD");
                    cpd=Double.parseDouble(cp1);
                    cp1=String.format("%.2f", cpd);


                    data.add(new Stock("ChangeYTD",cp1 +"("+cp+"%)"));
                    data.add(new Stock( "high",stockDetails.getString("High")));
                    data.add(new Stock("low", stockDetails.getString("Low")));
                    data.add(new Stock("open",stockDetails.getString("Open")));

                    //stockInfo.setText(name + "\n" + symbol + "\n" + lastPrice + "\n" + change + "\n" + changePercent + "\n" + timestamp + "\n" + changeYTD + "\n" + changePercentYTD
                 //
                 //           + "\n" + marketCap + "\n" + high + "\n" + low + "\n" + open);

                }
                stockInfo.setAdapter(new CurrentStockAdapter(this.getContext(), data));
                img=(ImageView)rootView.findViewById(R.id.stockImage);
                new MyAsyncTask().execute("http://chart.finance.yahoo.com/t?s=" + stockDetails.getString("Symbol") + "&lang=en-US&width=400&height=300");

                //img.setOnTouchListener(new Touch());

                ///mAttacher = new PhotoViewAttacher(img);
                img.setOnClickListener(this);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
       return rootView;
    }



    @Override
    public void onClick(View v) {
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this.getContext());
        Context context=this.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.imagetouch, null);
        ImageView image = (ImageView) layout.findViewById(R.id.fullImage);
        image.setImageBitmap(bitmap);
        image.setOnTouchListener(new Touch());
        imageDialog.setView(layout);
        imageDialog.create();
        imageDialog.show();
    }


    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String data = postData(params[0]);
            return data;
        }

        protected void onPostExecute(String result) {

            img.setImageBitmap(bitmap);
            if (result != null) {


            }
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        public String postData(String url) {


            InputStream stream = null;

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
            HttpURLConnection httpConnection=null;
            try {

                URL l = new URL(url);

                URLConnection connection = l.openConnection();


                httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                stream = httpConnection.getInputStream();
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();




                return "";
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                httpConnection.disconnect();
            }

            return null;
        }
    }
}
