package com.example.radhika.demoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.text.Html;

import java.util.List;
import java.text.*;
import java.util.Date;

/**
 * Created by Radhika on 03-05-2016.
 */
public class NewsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    int layoutResourceId;
    List<News> data=null;


    public NewsAdapter(Context context,
                               List<News> data) {
        this.context = context;
        this.data = data;
        inflater=LayoutInflater.from(this.context);
    }


    public class ViewHolder
    {
        TextView col1;
        TextView col2,col3,col4;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public News getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if(view==null)
        {
            holder=new ViewHolder();
            view=inflater.inflate(R.layout.newslistitem,null);
            holder.col1=(TextView)view.findViewById(R.id.newstitle);
            holder.col2=(TextView)view.findViewById(R.id.content);
            holder.col3=(TextView)view.findViewById(R.id.publisher);
            holder.col4=(TextView)view.findViewById(R.id.date);
            view.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)view.getTag();
        }

        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM,yyyy HH:mm:ss");
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String newDate=null;
        News s=getItem(position);
        Date newd=null;
        String oldDate=s.getDate();
        String[] arr=oldDate.split("T");
        oldDate=arr[0]+" "+arr[1].substring(0,arr[0].length()-1);
     //   Log.d("for",oldDate);
        try {
            newd=oldDateFormat.parse(oldDate);

   //         Log.d("Date",":"+newd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String n=newDateFormat.format(newd);
        holder.col4.setText("Date: "+n);
        holder.col1.setText(s.getTitle());
        holder.col2.setText(s.getContent());
        holder.col3.setText(s.getPublisher());


        String text = "<u>"+s.getTitle()+"</u>";
        Log.d("text","::"+text);
        holder.col1.setText(Html.fromHtml(text));

        return view;

    }
}
