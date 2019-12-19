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

import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.APIClient;
import com.pos.porschetower.network.CustomCall;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


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
        rootView = inflater.inflate(R.layout.fragment_dining, container, false);
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
//                RequestParams params = new RequestParams();
                Map<String, String> requestParam = new HashMap<>();

                Calendar c = Calendar.getInstance();
                int currentHour = c.get(Calendar.HOUR);
                int currentMinute = c.get(Calendar.MINUTE);
                int currentAMPM = c.get(Calendar.AM_PM);
                if (currentAMPM > 0)
                    currentHour = currentHour + 12;
                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH) + 1;
                int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                try {
                    JSONArray scheduleArray = new JSONArray(scheduleString);
                    JSONObject object = scheduleArray.getJSONObject(mIndex);
                    requestParam.put("type", "restaurants_in_house");
                    requestParam.put("index", object.getString("index"));
                    requestParam.put("owner", owner.getIndex() + "");
                    requestParam.put("date_time", currentYear + "-" + currentMonth + "-" + currentDayOfMonth + " " + currentHour + ":" + currentMinute + ":" + "00");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                APIClient.get().send_schedule_request(requestParam).enqueue(new CustomCall<ResponseBody>(DiningFragment.this.getActivity()) {
                    @Override
                    public void handleResponse(Call<ResponseBody> call, Response<ResponseBody> responsebody) {
                        Utils.showAlert(getActivity(), getResources().getString(R.string.msg_request_sent));
                    }
                });


//                AsyncHttpClient client = new AsyncHttpClient();
//                String functName = "send_schedule_request";
//                client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//                        Utils.showAlert(getActivity(), getResources().getString(R.string.msg_request_sent));
//                    }
//                });
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
                UserUtils.storeScheduleData((HomeActivity) getActivity(), object.toString());
                CalendarFragment fragment = new CalendarFragment();
                Bundle bd = new Bundle();
                bd.putString(TYPE, mType);
                fragment.setArguments(bd);
                Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);

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
                Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);
            }
        });

        btn_phone_dining = (ImageButton) rootView.findViewById(R.id.btn_phone_dining);
        // add PhoneStateListener
        DiningFragment.PhoneCallListener phoneListener = new DiningFragment.PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        btn_phone_dining.setOnClickListener(new View.OnClickListener() {
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

        String descriptionStr = UserUtils.getScheduleDataArray(getActivity());
        try {
            JSONArray scheduleArray = new JSONArray(descriptionStr);
            JSONObject object = scheduleArray.getJSONObject(mIndex);
            phoneNumber = object.getString("phone");
            txt_dining_restaurant_description.setText(object.getString("description"));
        } catch (JSONException e) {
        }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}