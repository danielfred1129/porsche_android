package com.pos.porschetower.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.PorscheTowerResponseHandler;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class DiningMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INDEX = "index";

    // TODO: Rename and change types of parameters
    private int mIndex;

    private View rootView;
    private TextView txt_dining_menu_description;
    private Button btn_makeAnOrder_menu;
    private ImageButton btn_phone_dining_menu;

    private String phoneNumber;

    public DiningMenuFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DiningMenuFragment newInstance(int index) {
        DiningMenuFragment fragment = new DiningMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dining_menu, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl() {
        String descriptionStr = UserUtils.getScheduleDataArray(getActivity());
        try {
            JSONArray scheduleArray = new JSONArray(descriptionStr);
            JSONObject object = scheduleArray.getJSONObject(mIndex);
            phoneNumber = object.getString("phone");
        }
        catch (JSONException e)
        {
        }

        txt_dining_menu_description = (TextView) rootView.findViewById(R.id.txt_dining_menu_description);
        txt_dining_menu_description.setMovementMethod(new ScrollingMovementMethod());
        btn_phone_dining_menu = (ImageButton) rootView.findViewById(R.id.btn_phone_dining_menu);
        // add PhoneStateListener
        DiningMenuFragment.PhoneCallListener phoneListener = new DiningMenuFragment.PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        btn_phone_dining_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Utils.isCallActive(getActivity())){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
//                }
//                else
//                {
//                    Utils.showAlert(getActivity(), getResources().getString(R.string.msg_call_not_available));
//                    return;
//                }
            }
        });

        btn_makeAnOrder_menu = (Button) rootView.findViewById(R.id.btn_makeAnOrder_menu);
        btn_makeAnOrder_menu.setOnClickListener(new View.OnClickListener() {
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
                    params.put("type", "order");
                    params.put("restaurant", object.getString("index"));
                    params.put("owner", owner.getIndex());
                    params.put("date_time",currentYear + "-" + currentMonth + "-" + currentDayOfMonth + " " + currentHour + ":" + currentMinute + ":" + "00");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AsyncHttpClient client = new AsyncHttpClient();
                String functName = "send_restaurant_request";
                client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Utils.showAlertWithTitleNoCancel((HomeActivity)getActivity(),
                                getResources().getString(R.string.msg_order_req_confirmed),
                                getResources().getString(R.string.msg_req_sent_staff_member));
                        return;
                    }
                });
            }
        });
        getRestaurantMenu();
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
//                    Intent i = getActivity().getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getActivity().getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

    private void getRestaurantMenu() {
        String scheduleString = UserUtils.getScheduleData(getActivity());
        RequestParams params = new RequestParams();
        try {
            JSONArray scheduleArray = new JSONArray(scheduleString);
            JSONObject object = scheduleArray.getJSONObject(mIndex);
            params.put("restaurant", object.getString("index"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String functName = "get_restaurant_menu";
        client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null)
                {
                    try {
                        String strResponse = response.getString("restaurant_menu");
                        String menuString = "";
                        if (response != null) {
                            JSONArray menus = new JSONArray(strResponse);
                            for (int i = 0; i < menus.length(); i++)
                            {
                                JSONObject menu = menus.getJSONObject(i);
                                menuString = menuString + menu.getString("name") + "\n" + menu.getString("description") + "\n\n";
                            }
                        }
                        txt_dining_menu_description.setText(menuString);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
