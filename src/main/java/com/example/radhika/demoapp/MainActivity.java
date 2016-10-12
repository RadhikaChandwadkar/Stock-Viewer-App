package com.example.radhika.demoapp;

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.AdapterView;
/*import org.apache.http.client.ClientProtocolException;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;*/
import  android.text.TextWatcher;
import  android.text.Editable;
import android.support.annotation.NonNull;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements OnClickListener{
    private AutoCompleteTextView symbolName;
    private Button getQuoteBtn, clearBtn;
    private DynamicListView favlist;
    private static List<Stock> favs=new ArrayList<>();
    private Switch autorefresh;
    private ImageButton refresh;
    final Handler handler = new Handler();
    int pos=0;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        c=getApplicationContext();
        setContentView(R.layout.activity_main);
        symbolName=(AutoCompleteTextView)findViewById(R.id.stockName);
        favlist=(DynamicListView)findViewById(R.id.favlist);
        getQuoteBtn = (Button) findViewById(R.id.getQuote);
        clearBtn=(Button)findViewById(R.id.clear);
        autorefresh=(Switch)findViewById(R.id.autoR);
        refresh=(ImageButton)findViewById(R.id.refresh);
        getQuoteBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.stm2);
        final MyRun task = new MyRun();
        autorefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("in auto refreshed", "enter");
                ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();


                if (isChecked) {
                    Log.d("in active", "enter");
                    handler.postDelayed(task, 10000);
                    Log.d("auto refreshed", "leave");
                } else {
                    Log.d("refreshaed", "EFFEW");
                    handler.removeCallbacks(task);
                    Log.d("refreshed", "EFFEW");
                }
            }
        });
        refresh.setOnClickListener(this);

        Log.d("datatosend", "::" + "Inside create");
        String datatosend=loadFavourites();
        Log.d("after datatosend", "::" + datatosend);
        final FavouriteAdapter fa=new FavouriteAdapter(this);

        favlist.setVisibility(View.VISIBLE);
        favlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Stock) {
                    new MyAsyncTask().execute(((Stock) item).getSymbol());
                }
            }
        });




        final AutoCompleteAdapter adapter = new AutoCompleteAdapter(this);


        favlist.setAdapter(fa);
        favlist.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                                pos=position;
                        //    MainActivity.this.runOnUiThread(new Runnable() {
                              //  public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                    alertDialog.setMessage("Do you want to delete the stock from favourites");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                       /*            SharedPreferences sharedpreferences = getSharedPreferences("Symbols", Context.MODE_APPEND);
                                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                */
                                                    Stock s=fa.getItem(pos);


                                                        PreferencesManager.removeFav(c,s.getSymbol());
                                             /*       if(sharedpreferences.contains("Stocks")) {
                                                        String dat = sharedpreferences.getString("Stocks", "");
                                                        editor.remove("Stocks");
                                                        if (dat.contains(s.getSymbol()))
                                                        {
                                                            dat = dat.replace(s.getSymbol(), "");
                                                            dat = dat.replace(";;", ";");
                                                            if(dat.length()==1)
                                                            {

                                                            }
                                                            else {
                                                                if (dat.endsWith(";"))
                                                                    dat = dat.substring(0, dat.length() - 1);
                                                                editor.putString("Stocks", dat);
                                                            }

                                                        }

                                                    }
                                                    editor.commit();*/
                                                    fa.removeItem(s);
                                                    String datatosend = loadFavourites();
                                                    fa.getFilter().filter(datatosend);
                                                    Log.d("Inside", "After swipe");
                                                }
                                            });
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                alertDialog.show();
                            Log.d("Inside", "After swipe");
                        }
                    }
                }
        );
        fa.getFilter().filter(datatosend);

        symbolName.setThreshold(3);
        symbolName.setAdapter(adapter);
        symbolName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Stock) {
                    Stock student = (Stock) item;
                    symbolName.getText().clear();
                    symbolName.append(student.getSymbol());
                }
            }
        });// 'this' is Activity instance
        TextWatcher searchTextWatcher = new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // ignore
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

                    Log.d("Main create", "in this::" + s);
                    adapter.getFilter().filter(s.toString());
            }
        };
    }

    private String loadFavourites()
    {

    //    SharedPreferences sharedPreferences = getSharedPreferences("Symbols", Context.MODE_APPEND);
    //    SharedPreferences.Editor editor = sharedPreferences.edit();

//        String stockData = sharedPreferences.getString("Stocks","");
        List<String> array=PreferencesManager.getFav(this);
        Log.d("prefs::",array.toString());
        favs=new ArrayList<>();
        String datatosend="";
        Log.d("size",""+array.size());
        if(array.isEmpty() || (array.size()==1 && array.get(0)=="")) {
            Log.d("inside if","empty");
            return null;
        }
        for(String stock:array)
        {
            Log.d("going to ansync", "222");
           datatosend+=stock+";";
            Log.d("back from ansync", "222");
        }

        datatosend=datatosend.substring(0,datatosend.length()-1);
        return datatosend;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onClick(View v)
    {
        // TODO Auto-generated method stub

        switch (v.getId())
        {
            case R.id.getQuote:

              ///  Toast.makeText(getApplicationContext(),"In on click function", Toast.LENGTH_LONG).show();
                if(symbolName.getText().toString().length()<1){

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("Please enter a Stock Name/Symbol");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    // out of range
                  //  Toast.makeText(this, "please enter something", Toast.LENGTH_LONG).show();
                }

                else{
         //           Toast.makeText(this, "in else", Toast.LENGTH_LONG).show();
                    new MyAsyncTask().execute(symbolName.getText().toString());
                }

                break;
            case R.id.clear:
                // do something else
                symbolName.getText().clear();
                break;


            case R.id.autoR:

                break;

            case R.id.refresh:
                        String datatosend=loadFavourites();
                        ((FavouriteAdapter)favlist.getAdapter()).getFilter().filter(datatosend);
                        Log.d("refreshed","EFFEW");
                break;
        }


    }


    private  class MyRun implements Runnable
    {
        public void run() {
            Log.d("run","into");
            String datatosend=loadFavourites();
            ((FavouriteAdapter)favlist.getAdapter()).getFilter().filter(datatosend);
            handler.postDelayed(this, 10000);
            Log.d("run", "out");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {



            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    private class MyAsyncTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String data=postData(params[0]);
            return data;
        }

        protected void onPostExecute(String result){

            if(result!=null) {
           //     Log.d("on Post", "result: " + result.toString());
                Intent startApp = new Intent(MainActivity.this, ResultActivity.class);
                startApp.putExtra("json", result.toString());
                startActivity(startApp);
                finish();
         //       Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
            }
        }
        protected void onProgressUpdate(Integer... progress){

        }

        public String  postData(String valueIWantToSend) {

            HttpURLConnection urlConnection=null;
            String URLString = "http://midyear-freedom-127906.appspot.com/bingSearch.php?&lookup="+valueIWantToSend;
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


                JSONObject s=new JSONObject(result.toString());
             //   Log.d("result",s.toString());
                String stockValue=s.getString("stockValue");
                s=new JSONObject(stockValue);
                String name=s.getString("Name");
                String status=s.getString("Status");

                if(status.equals("Failure|APP_SPECIFIC_ERROR"))
                {
                    throw new Exception();
                }
              //  Log.d("JSON Parser", "result: " + result.toString());
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setMessage("Invalid Symbol");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                });
                              return null;

            } finally {
                    urlConnection.disconnect();
                }

            return null;
        }

    }










}
