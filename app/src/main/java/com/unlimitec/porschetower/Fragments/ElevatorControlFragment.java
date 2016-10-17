package com.unlimitec.porschetower.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.unlimitec.porschetower.HomeActivity;
import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.customview.PorscheTextView;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.porschetower.network.PorscheTowerResponseHandler;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.utils.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ElevatorControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElevatorControlFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SELECTED_CAR = "SelectedCar";
    private static final String VALET = "valet";
    private static final String DELAYTIME = "delayTime";

    // Variables to manage arguments
    private String selectedCar,strCarIndex, strCarStatus, strFlightTime, strOwnerElevator, valet;
    private JSONObject object_selectedCar;
    private String indexSelectedCar;

    // member variables
    private int totalMinute, totalSecond, currentMinute, currentSecond, delayTime;
    private boolean state;

    UserObject owner;
    String pickup, sendTime;
    Timer timer;
    CountDownTimer newtimer, currentTimer;
    // View Variables
    private View rootView;
    private ImageButton btn_start_queue;
    private PorscheTextView txt_arrival_time,txt_countdowntime, txt_cars_in_queue, txt_current_time;
    private TextView txt_elevator_description;

    public ElevatorControlFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ElevatorControlFragment newInstance(String selectedCar, String valet, int delayTime) {
        ElevatorControlFragment fragment = new ElevatorControlFragment();
        Bundle args = new Bundle();
        args.putString(SELECTED_CAR, selectedCar);
        args.putString(VALET, valet);
        args.putInt(DELAYTIME, delayTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            owner = UserUtils.getSession(getActivity());
            try {
                strOwnerElevator = owner.getUnit().getString("elevator1");
                strFlightTime = owner.getUnit().getString("flight_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (getArguments().containsKey(SELECTED_CAR)) {
                selectedCar = getArguments().getString(SELECTED_CAR);
            }
            if (getArguments().containsKey(DELAYTIME)) {
                delayTime = getArguments().getInt(DELAYTIME);
            }
            if (getArguments().containsKey(VALET))
                valet = getArguments().getString(VALET);
            try {
                object_selectedCar = new JSONObject(selectedCar);
                strCarIndex = object_selectedCar.getString("index");
                strCarStatus = object_selectedCar.getString("status");
                indexSelectedCar = object_selectedCar.getString("index");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_elevator_control, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl()
    {
        btn_start_queue = (ImageButton) rootView.findViewById(R.id.btn_start_queue);

        btn_start_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnStart();
            }
        });
        txt_arrival_time = (PorscheTextView) rootView.findViewById(R.id.txt_arrival_time);
        txt_cars_in_queue = (PorscheTextView) rootView.findViewById(R.id.txt_cars_in_queue);
        txt_countdowntime = (PorscheTextView) rootView.findViewById(R.id.txt_countdowntime);
        txt_elevator_description = (TextView) rootView.findViewById(R.id.txt_elevator_description);
        txt_current_time = (PorscheTextView) getActivity().findViewById(R.id.txt_current_time);

        showCurrentTime();
        resetArrivalTime(0, 0);
        resetCountdown(0, 0);


        if (getArguments().containsKey(VALET))
        {
            if (valet.equals("ridedown"))
                txt_elevator_description.setText(R.string.txt_ridedown_description);
            else if (valet.equals("valet"))
                txt_elevator_description.setText(R.string.txt_valet_description);
        }
        if (strCarStatus.equals("active"))
        {
            activeStatus();
            RequestParams params = new RequestParams();
            params.put("car", strCarIndex);
            AsyncHttpClient client = new AsyncHttpClient();
            String functName = "get_pickup";
            client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (response != null) {
                        try {
                            pickup = response.getString("index");
                            delayTime = response.getInt("delay");
                            sendTime = response.getString("send_time");
                            int queueSize = response.getInt("queue_size");
                            txt_cars_in_queue.setText(String.valueOf(queueSize) + "\n");

                            int countdown = response.getInt("countdown");

                            resetArrivalTime(countdown / 60, countdown % 60);
                            resetCountdown(countdown / 60, countdown % 60);
                            startCountdown(countdown / 60, countdown % 60);
                        } catch (JSONException e) {
                        }
                    }
                }
            });
        }
        else
        {
            RequestParams params = new RequestParams();
            params.put("elevator", strOwnerElevator);
            AsyncHttpClient client = new AsyncHttpClient();
            String functName = "get_elevator_queue_size";
            client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (response != null) {
                        try {
                            int queueSize = response.getInt("queue_size");
                            int queueTime = response.getInt("queue_time");
                            txt_cars_in_queue.setText(String.valueOf(queueSize) + "\n");

                            int totalDelay = calculateTotalDelayBySecond(queueTime);
                            totalMinute = totalDelay / 60;
                            totalSecond = totalDelay % 60;

                            resetArrivalTime(totalMinute, totalSecond);
                            resetCountdown(totalMinute, totalSecond);
                        } catch (JSONException e) {
                        }
                    }
                }
            });
        }
    }

    private int calculateTotalDelayBySecond(int queueTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(strFlightTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int minutes = convertedDate.getMinutes();
        int seconds = convertedDate.getSeconds();

        int delaySeconds = minutes * 60 + seconds + queueTime + delayTime;
        return delaySeconds;
    }
    private void activeStatus() {
        state = true;
        btn_start_queue.setImageResource(R.drawable.elevator_control_start_btn_active);
    }
    private void deactiveStatus() {
        state = false;
        btn_start_queue.setImageResource(R.drawable.elevator_control_start_btn_normal);
        if(newtimer != null) {
            newtimer.cancel();
            newtimer = null;
        }
        if(currentTimer != null) {
            currentTimer.cancel();
            currentTimer = null;
        }
        txt_current_time.setVisibility(View.GONE);
        HomeFragment fragment = new HomeFragment();
        Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), false);
    }
    private void showCurrentTime() {
        txt_current_time.setVisibility(View.VISIBLE);
        refreshCurrentTime();
    }
    private void refreshCurrentTime() {
        currentTimer = new CountDownTimer(1000000000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                int ampm = c.get(Calendar.AM_PM);

                String ap;
                if (ampm == 1)
                    ap = "PM";
                else
                    ap = "AM";
                String strZero;
                if (c.get(Calendar.MINUTE) < 10)
                    strZero = "0";
                else
                    strZero = "";
                txt_current_time.setText("\n" + (c.get(Calendar.HOUR) % 12)+":"+ strZero + c.get(Calendar.MINUTE)+" "+ ap);
            }

            @Override
            public void onFinish() {

            }
        };
        currentTimer.start();
    }
    private void resetArrivalTime(int minutes, int seconds) {
        Calendar c = Calendar.getInstance();
        int arrivalHour = c.get(Calendar.HOUR);
        int arrivalMinute = c.get(Calendar.MINUTE);
        int arrivalSecond = c.get(Calendar.SECOND);

        arrivalSecond = arrivalSecond + seconds;
        arrivalMinute = arrivalMinute + minutes + arrivalSecond / 60;
        if (arrivalMinute >= 60) {
            arrivalHour = arrivalHour + arrivalMinute / 60;
            arrivalMinute = arrivalMinute % 60;
        }
        if (arrivalHour == 12)
            arrivalHour = 12;
        else
            arrivalHour = arrivalHour % 12;
        String strZeroMinute;
        if (c.get(Calendar.MINUTE) < 10)
            strZeroMinute = "0";
        else
            strZeroMinute = "";
        String strZeroSecond;
        if (c.get(Calendar.SECOND) < 10)
            strZeroSecond = "0";
        else
            strZeroSecond = "";
        txt_arrival_time.setText(String.valueOf(arrivalHour) + ":" + strZeroMinute + String.valueOf(arrivalMinute) + "\n");
    }
    private void resetCountdown(int minutes, int seconds){
        String strZeroSecond;
        if (seconds < 10)
            strZeroSecond = "0";
        else
            strZeroSecond = "";
        txt_countdowntime.setText(String.valueOf(minutes) + ":" + strZeroSecond + String.valueOf(seconds));
    }
    private void startCountdown(int minutes, int seconds) {
        currentMinute = minutes;
        currentSecond = seconds;
        updateCountdown();
        newtimer = new CountDownTimer(1000000000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
               if ((currentMinute > 0 || currentMinute >= 0) && currentMinute >=0) {
                   if (currentSecond == 0) {
                       currentMinute -= 1;
                       currentSecond = 59;
                   } else if (currentSecond > 0) {
                       currentSecond -= 1;
                   }
                   if (currentMinute > -1) {
                       String strZeroSecond;
                       if (currentSecond < 10)
                           strZeroSecond = "0";
                       else
                            strZeroSecond = "";
                       txt_countdowntime.setText(String.valueOf(currentMinute) + ":" + strZeroSecond + String.valueOf(currentSecond));
                   }
               }
                else {
                   if (newtimer != null) {
                       deactiveStatus();
                       successElevator();
                       Utils.showAlertWithTitleNoCancel((HomeActivity)getActivity(), getString(R.string.title_car_ready_pickup), getString(R.string.msg_car_delivered_ready_to_pickup));
                   }
               }
            }

            @Override
            public void onFinish() {

            }
        };
        newtimer.start();
    }
    private void updateCountdown() {
        RequestParams params = new RequestParams();
        params.put("pickup", pickup);

        AsyncHttpClient client = new AsyncHttpClient();
        String functName = "get_time_increase";
        client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        int timeIncrease = Integer.parseInt(response.getString("time_increase"));
                        String send_time = response.getString("send_time");
                        if (timeIncrease > 0 && !send_time.equals(sendTime) && state == true) {
                            sendTime = send_time;
                            int countdown = currentMinute * 60 + currentSecond + timeIncrease;
                            currentMinute = countdown / 60;
                            currentSecond = countdown % 60;

                            resetArrivalTime(totalMinute + (totalSecond + timeIncrease) / 60, (totalSecond + timeIncrease) % 60);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    private void sendRequest() {
        RequestParams params = new RequestParams();
        params.put("car", indexSelectedCar);
        params.put("valet", valet);
        params.put("elevator", strOwnerElevator);
        params.put("delay", delayTime);

        AsyncHttpClient client = new AsyncHttpClient();
        String functName = "request_car_elevator";
        client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        pickup = response.getString("pickup");
                        resetArrivalTime(totalMinute, totalSecond);
                        resetCountdown(totalMinute, totalSecond);
                        startCountdown(totalMinute, totalSecond);
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }
    private void cancelRequest(){
        RequestParams params = new RequestParams();
        params.put("pickup", pickup);

        AsyncHttpClient client = new AsyncHttpClient();
        String functName = "cancel_car_elevator";
        client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                }
            }
        });
    }
    private void successElevator() {
        RequestParams params = new RequestParams();
        params.put("pickup", pickup);

        AsyncHttpClient client = new AsyncHttpClient();
        String functName = "success_car_elevator";
        client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                }
            }
        });
    }
    private void onBtnStart() {
        if (state == false) {
            activeStatus();
            sendRequest();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.title_elevator_activated));
            builder.setMessage(getString(R.string.msg_sure_to_cancel_elevator));
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelRequest();
                    deactiveStatus();
                }
            });
            builder.show();
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
