package com.unlimitec.porschetower.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.unlimitec.porschetower.HomeActivity;
import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.porschetower.network.PorscheTowerResponseHandler;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class DiningFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TYPE = "type";
    private static final String INDEX = "index";

    // TODO: Rename and change types of parameters
    private String mType;
    private int mIndex;

    private View rootView;
    private ImageButton btn_phone_dining;
    private Button btn_makeAnOrder, btn_viewMenu, btn_makeReservation;
    private TextView txt_dining_restaurant_description;

    String phoneNumber;


    public DiningFragment() {
        // Required empty public constructor
    }

    public static DiningFragment newInstance(String type, int index) {
        DiningFragment fragment = new DiningFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putInt(INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
            mIndex = getArguments().getInt(INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_dining, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl() {
        txt_dining_restaurant_description = (TextView) rootView.findViewById(R.id.txt_dining_restaurant_description);
        txt_dining_restaurant_description.setMovementMethod(new ScrollingMovementMethod());
        btn_makeAnOrder = (Button) rootView.findViewById(R.id.btn_makeAnOrder);
        btn_makeAnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserObject owner = UserUtils.getSession(getActivity());

                String scheduleString = UserUtils.getScheduleData(getActivity());
                RequestParams params = new RequestParams();
                Calendar c = Calendar.getInstance();
                int currentHour = c.get(Calendar.HOUR);
                int currentMinute = c.get(Calendar.MINUTE);
                int currentAMPM= c.get(Calendar.AM_PM);
                if (currentAMPM > 0)
                    currentHour = currentHour + 12;
                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH) + 1;
                int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                try {
                    JSONArray scheduleArray = new JSONArray(scheduleString);
                    JSONObject object = scheduleArray.getJSONObject(mIndex);
                    params.put("type", "restaurants_in_house");
                    params.put("index", object.getString("index"));
                    params.put("owner", owner.getIndex());
                    params.put("date_time",currentYear + "-" + currentMonth + "-" + currentDayOfMonth + " " + currentHour + ":" + currentMinute + ":" + "00");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AsyncHttpClient client = new AsyncHttpClient();
                String functName = "send_schedule_request";
                client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Utils.showAlert(getActivity(), getResources().getString(R.string.msg_request_sent));
                    }
                });
            }
        });
        btn_makeReservation = (Button) rootView.findViewById(R.id.btn_makeReservation);
        btn_makeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = null;
                try {
                    String descriptionStr = UserUtils.getScheduleDataArray(getActivity());
                    JSONArray scheduleArray = new JSONArray(descriptionStr);
                    object = scheduleArray.getJSONObject(mIndex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UserUtils.storeScheduleData(getActivity(), object.toString());
                CalendarFragment fragment = new CalendarFragment();
                Bundle bd = new Bundle();
                bd.putString(TYPE, mType);
                fragment.setArguments(bd);
                Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);

            }
        });
        btn_viewMenu = (Button) rootView.findViewById(R.id.btn_viewMenu);
        btn_viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bd = new Bundle();
                bd.putInt(INDEX, mIndex);
                DiningMenuFragment fragment = new DiningMenuFragment();
                fragment.setArguments(bd);
                Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
            }
        });

        btn_phone_dining = (ImageButton) rootView.findViewById(R.id.btn_phone_dining);
        btn_phone_dining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isCallActive(getActivity())){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }
                else
                {
                    Utils.showAlert(getActivity(), getResources().getString(R.string.msg_call_not_available));
                    return;
                }
            }
        });

        String descriptionStr = UserUtils.getScheduleDataArray(getActivity());
        try {
            JSONArray scheduleArray = new JSONArray(descriptionStr);
            JSONObject object = scheduleArray.getJSONObject(mIndex);
            phoneNumber = object.getString("phone");
            txt_dining_restaurant_description.setText(object.getString("description"));
        }
        catch (JSONException e)
        {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}