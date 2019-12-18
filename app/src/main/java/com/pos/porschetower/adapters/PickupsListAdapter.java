package com.pos.porschetower.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.datamodel.PickupsItem;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.PorscheTowerResponseHandler;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by buddy on 10/24/2016.
 */

public class PickupsListAdapter  extends BaseAdapter {
    ArrayList<PickupsItem> listdata;
    private Context mContext;

    public PickupsListAdapter(Context context, ArrayList <PickupsItem> listdata) {
        mContext = context;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        PickupsListAdapter.ViewHolder holder = new PickupsListAdapter.ViewHolder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (LinearLayout) inflater.inflate(R.layout.schedule_pickups_item, parent, false);
            holder.txt_car_name = (TextView) view.findViewById(R.id.txt_car_name);
            holder.txt_pickup_date = (TextView) view.findViewById(R.id.txt_pickup_date);
            holder.txt_pickup_time = (TextView) view.findViewById(R.id.txt_pickup_time);
            holder.btn_pickup_cancel = (Button) view.findViewById(R.id.btn_pickup_cancel);
            view.setTag(holder);
        } else {

            holder = (PickupsListAdapter.ViewHolder) view.getTag();
        }

        PickupsItem item = listdata.get(position);

        holder.txt_car_name.setText(item.car_name);
        holder.txt_pickup_date.setText(item.date);
        holder.txt_pickup_time.setText(item.time);
        final ViewHolder finalHolder = holder;
        holder.btn_pickup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                UserObject user = UserUtils.getSession(mContext);
                params.put("owner", user.getIndex());
                String scheduled_pickups = UserUtils.getScheduleData(mContext);
                String index = "";
                JSONArray pickupsArray = null;
                try {
                    pickupsArray = new JSONArray(scheduled_pickups);
                    index = pickupsArray.getJSONObject(position).getString("index");

                    JSONArray changedArray = new JSONArray();
                    int len = pickupsArray.length();
                    if (pickupsArray != null) {
                        for (int i=0;i<len;i++)
                        {
                            //Excluding the item at position
                            if (i != position)
                            {
                                changedArray.put(pickupsArray.get(i));
                            }
                        }
                    }
                    UserUtils.storeScheduleData(mContext, changedArray.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                params.put("pickup", index);
                String funcName = "cancel_scheduled_car_elevator";
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler((HomeActivity)mContext) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        listdata.remove(position);
                        notifyDataSetChanged();
                        if (response != null) {
                        }
                    }

                });
            }
        });


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

    public ArrayList <PickupsItem> getData() {
        return this.listdata;
    }

    public void addData(PickupsItem item) {
        this.listdata.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.listdata.clear();
        notifyDataSetChanged();
    }

    class ViewHolder {
        public TextView txt_car_name;
        public TextView txt_pickup_date;
        public TextView txt_pickup_time;
        public Button btn_pickup_cancel;
    }
}
