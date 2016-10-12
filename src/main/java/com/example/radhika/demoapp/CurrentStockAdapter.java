package com.example.radhika.demoapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.*;
/**
 * Created by Radhika on 03-05-2016.
 */
public class CurrentStockAdapter  extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    int layoutResourceId;
    List<Stock> data=null;


    public CurrentStockAdapter(Context context,
                         List<Stock> data) {
        this.context = context;
        this.data = data;
        inflater=LayoutInflater.from(this.context);
    }


    public class ViewHolder
    {
        TextView col1;
        TextView col2;
    }

    @Override
    public int getCount() {
        return data.size();
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
            view=inflater.inflate(R.layout.currentstockitem,null);
            holder.col1=(TextView)view.findViewById(R.id.col1);
            holder.col2=(TextView)view.findViewById(R.id.col2);

            view.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)view.getTag();
        }

        Stock s=getItem(position);
        SpannableStringBuilder builder = null;
        if(s.getName().equals("change") || s.getName().equals("ChangeYTD")  )
        {
            String[] d=(s.getSymbol()).split("\\(");
            d[1]=d[1].substring(0,d[1].length()-2);
            Double c=Double.parseDouble(d[1]);
            if(c>0) {
                 builder = new SpannableStringBuilder();
                builder.append(s.getSymbol())
                        .append(" ", new ImageSpan(context, R.drawable.up), 0);
            }
            else
            {
                 builder = new SpannableStringBuilder();
                builder.append(s.getSymbol())
                        .append(" ", new ImageSpan(context, R.drawable.down), 0);
            }
            holder.col2.setText(builder);
        }
        else
        {
            holder.col2.setText(s.getSymbol());
        }
        holder.col1.setText(s.getName());

        return view;

    }
}
