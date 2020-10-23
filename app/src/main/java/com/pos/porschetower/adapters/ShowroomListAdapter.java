package com.pos.porschetower.adapters;

import android.content.Context;
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
import com.pos.porschetower.datamodel.ShowroomItem;

import java.util.ArrayList;

/**
 * Created by coala on 10/19/2020.
 */

public class ShowroomListAdapter  extends BaseAdapter {

    ArrayList <ShowroomItem> listdata;

    public ShowroomListAdapter(ArrayList <ShowroomItem> listdata) {
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
        ViewHolder holder = new ViewHolder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (LinearLayout) inflater.inflate(R.layout.booking_room_listitem, parent, false);
            holder.imgView = (ImageView) view.findViewById(R.id.img_car_showroom);
            holder.btnTitle = (Button) view.findViewById(R.id.btn_cartitle_showroom);
            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }

        ShowroomItem item = listdata.get(position);
        final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(item.img_url, holder.imgView, options);

        String description = item.car_name + "\n" + item.status_description;

        holder.btnTitle.setText(description);
        holder.btnTitle.setBackgroundColor(item.btn_color);
        holder.btnTitle.setTextColor(item.btn_txt_color);

        return view;
    }

    public ArrayList <ShowroomItem> getData() {
        return this.listdata;
    }

    public void addData(ShowroomItem item) {
        this.listdata.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.listdata.clear();
        notifyDataSetChanged();
    }

    class ViewHolder {
        public ImageView imgView;
        public Button btnTitle;
    }
}