package com.example.radhika.demoapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link HistoricalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoricalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private WebView historical;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment HistoricalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoricalFragment newInstance(String param1) {
        HistoricalFragment fragment = new HistoricalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    public HistoricalFragment() {
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
        View rootView=inflater.inflate(R.layout.fragment_historical, container, false);
        historical=(WebView)rootView.findViewById(R.id.historicalChart);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            WebSettings webSettings = historical.getSettings();
            webSettings.setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                webSettings.setAllowUniversalAccessFromFileURLs(true);
            historical.loadUrl("file:///android_asset/hc.html?symbol="+mParam1);
        }
        return rootView;
    }
}
