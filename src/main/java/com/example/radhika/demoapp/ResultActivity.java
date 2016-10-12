package com.example.radhika.demoapp;
import com.facebook.FacebookCallback;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText symbolName;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String stockValue;
    private String historicalChart;
    private String newsFeed,symb;
    private ImageButton fbshare;
    private ImageButton favorites;
    private SharedPreferences sharedpreferences;
    private ShareDialog shareDialog;
    private Stock currentStock;
    CallbackManager callbackManager;

    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c=this;

        setContentView(R.layout.activity_result);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fbshare = (ImageButton) findViewById(R.id.fb_share);
        favorites = (ImageButton) findViewById(R.id.fav);

        currentStock=new Stock();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager,
                new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setMessage("Posted successfully on FB")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                //        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onCancel() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setMessage("Not posted on FB")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                //        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onError(FacebookException e) {

                    }
                });
        int id;
        sharedpreferences = getSharedPreferences("Symbols", Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        fbshare.setOnClickListener(this);
        favorites.setOnClickListener(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent myIntent = getIntent(); // gets the previously created intent
        String symbolParam = myIntent.getStringExtra("json");
      //  symbolName=(EditText)findViewById(R.id.symbol);

        try {
            stockValue=(new JSONObject(symbolParam)).getString("stockValue");
            JSONObject s=new JSONObject(stockValue);
            symb=s.getString("Symbol");
            currentStock.setSymbol(symb);
            currentStock.setName(s.getString("Name"));
            currentStock.setLastPrice(s.getString("LastPrice"));

            String cp=s.getString("ChangePercent");
            Double cpd=Double.parseDouble(cp);
            cp=String.format("%.2f", cpd);

            String cp1;
            cp1 = s.getString("Change");
            cpd=Double.parseDouble(cp1);
            cp1=String.format("%.2f", cpd);

            currentStock.setChange(cp1 + "(" + cp + "%)");


            if(PreferencesManager.isFav(this,symb))
            {
               // String dat=sharedpreferences.getString("Stocks","");
              //  if(dat.contains(symb)) {
                    id = getResources().getIdentifier("@android:drawable/btn_star_big_on", null, null);
              /*  }
                else
                {
                    id = getResources().getIdentifier("@android:drawable/btn_star_big_off", null, null);
                }*/
            }
            else
            {
                id = getResources().getIdentifier("@android:drawable/btn_star_big_off", null, null);
            }
            favorites.setImageResource(id);

            getSupportActionBar().setTitle(s.getString("Name"));

            historicalChart=new JSONObject(symbolParam).getString("chart");
            newsFeed=new JSONObject(symbolParam).getString("search");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Log.d("data setup",stockValue);
        adapter.addFragment(CurrentFragment.newInstance(stockValue), "Current");
        adapter.addFragment(HistoricalFragment.newInstance(symb), "Historical");
        adapter.addFragment(NewsFragment.newInstance(newsFeed), "News");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fav:
                int id;

                if(PreferencesManager.isFav(this,symb)) {
                    id = getResources().getIdentifier("@android:drawable/btn_star_big_off", null, null);
                    PreferencesManager.removeFav(this, symb);
                    Toast.makeText(getApplicationContext(), symb + " removed from favorites", Toast.LENGTH_LONG).show();
                }
                else
                {
                    id = getResources().getIdentifier("@android:drawable/btn_star_big_on", null, null);
                    PreferencesManager.addFav(this,symb);
                    Toast.makeText(getApplicationContext(), symb + " added to favorites", Toast.LENGTH_LONG).show();
                }
               /* SharedPreferences.Editor editor = sharedpreferences.edit();
                if(sharedpreferences.contains("Stocks"))
                {
                    String dat=sharedpreferences.getString("Stocks","");
                    editor.remove("Stocks");
                    if(dat.contains(symb))
                    {
                        dat=dat.replace(symb,"");
                        dat=dat.replace(";;",";");
                        if(dat.length()==1 || dat.length()==0)
                        {

                        }
                        else {
                            if (dat.endsWith(";"))
                                dat = dat.substring(0, dat.length() - 1);
                            editor.putString("Stocks", dat);
                        }
                        if(dat.endsWith(";"))
                            dat=dat.substring(0,dat.length()-1);

                    }
                    else
                    {
                        dat+=";"+symb;
                        id = getResources().getIdentifier("@android:drawable/btn_star_big_on", null, null);
                    }
                    editor.putString("Stocks",dat);
                }
                else
                {
                    editor.putString("Stocks",symb);


                }
                editor.commit();*/
                favorites.setImageResource(id);
                break;


            case R.id.fb_share:


                if (ShareDialog.canShow(ShareLinkContent.class))
                {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Current Stock Price of "+currentStock.getName() +" is "+currentStock.getLastPrice())
                            .setContentDescription(
                                    "Stock Information of " + currentStock.getName() + " (" +currentStock.getSymbol() + ")")
                            .setContentUrl(Uri.parse("http://finance.yahoo.com/q?s="+ currentStock.getSymbol()))
                            .setImageUrl(Uri.parse("http://chart.finance.yahoo.com/t?s=" + currentStock.getSymbol() + "&lang=en-US&width=200&height=200"))
                            .build();
                    shareDialog.show(linkContent);
                }
                break;
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case android.R.id.home :
            {

                Log.d("Leaving this", " entering main");
                Intent startApp = new Intent(this,MainActivity.class);
                startActivity(startApp);
                    finish();
                break;
            }
        }
        //noinspection SimplifiableIfStatement

        return true;
    }
}
