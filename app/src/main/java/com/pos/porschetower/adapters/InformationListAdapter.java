package com.pos.porschetower.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pos.porschetower.R;
import com.pos.porschetower.datamodel.InformationItem;
import com.pos.porschetower.datamodel.ShowroomItem;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pos.porschetower.R;
import com.pos.porschetower.customview.PorscheTextView;
import com.pos.porschetower.datamodel.ShowroomItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by buddy on 10/8/2016.
 */

public class InformationListAdapter  extends BaseAdapter {

    ArrayList<InformationItem> listdata;

    public InformationListAdapter(ArrayList <InformationItem> listdata) {
        this.listdata = listdata;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return listdata.size();
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (listdata != null)
            return listdata.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        InformationListAdapter.ViewHolder holder = new InformationListAdapter.ViewHolder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (LinearLayout) inflater.inflate(R.layout.information_listitem, parent, false);
            holder.txt_from = (TextView) view.findViewById(R.id.txt_from);
            holder.txt_message = (TextView) view.findViewById(R.id.txt_message);
            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }

        InformationItem item = listdata.get(position);

        holder.txt_from.setText(item.from);
        holder.txt_message.setText(item.message);

        int backColor;
        if (position % 2 == 0)
        {
            backColor = Color.rgb(0, 0, 0);
        }
        else
        {
            backColor = Color.rgb(40, 43, 47);
        }
        view.setBackgroundColor(backColor);
        return view;
    }

    public ArrayList <InformationItem> getData() {
        return this.listdata;
    }

    public void addData(InformationItem item) {
        this.listdata.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.listdata.clear();
        notifyDataSetChanged();
    }

    class ViewHolder {
        public TextView txt_from;
        public TextView txt_message;
    }
}