package com.example.radhika.demoapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;

    private ListView newsFeed;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        List<News> data = new ArrayList<>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

            newsFeed = (ListView) rootView.findViewById(R.id.newFeed);

            try {
                JSONObject search = new JSONObject(mParam1);
                JSONObject d = new JSONObject(search.getString("d"));
                Log.d("d","::"+d.toString());
                JSONArray results = new JSONArray(d.getString("results"));
               // Log.d("results","::"+results.toString());
               if(results!=null) {

                   for(int i=0;i<results.length();i++)
                   {
                       JSONObject obj= (JSONObject) results.get(i);
              //         Log.d("object","::"+obj.toString());
                       String title = obj.getString("Title");
                       String publisher="Publisher: "+obj.getString("Source");
                       String content=obj.getString("Description");
                       String date=obj.getString("Date");
                       String url=obj.getString("Url");
                       News newsAd=new News(title,content,publisher,date,url);
                       data.add(newsAd);
                   }
               }
                newsFeed.setAdapter(new NewsAdapter(this.getContext(), data));
                newsFeed.setOnItemClickListener(this);

            } catch (JSONException e) {
                e.printStackTrace();
            }

       }
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Object item = parent.getItemAtPosition(position);
        if (item instanceof News){
            Log.d("News::",((News) item).getUrl());
            News student = (News) item;
            Uri uri = Uri.parse( student.getUrl() );
            startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
        }
    }
}
