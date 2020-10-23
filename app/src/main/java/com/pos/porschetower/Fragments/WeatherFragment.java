package com.pos.porschetower.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.pos.porschetower.R;
import com.pos.porschetower.network.PorscheTowerResponseHandler;
import com.pos.porschetower.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class WeatherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private TextView txt_current_time_weather, txt_temperature, txt_feellike_temperature, txt_weather, txt_wind, txt_humidity, txt_uvindex, txt_visibility, txt_nexthourprecip;

    CountDownTimer currentTimer;


    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Weather.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl()
    {
        txt_current_time_weather = (TextView) rootView.findViewById(R.id.txt_current_time_weather);
        txt_temperature = (TextView) rootView.findViewById(R.id.txt_temperature);
        txt_feellike_temperature = (TextView) rootView.findViewById(R.id.txt_feellike_temperature);
        txt_weather = (TextView) rootView.findViewById(R.id.txt_weather);
        txt_wind = (TextView) rootView.findViewById(R.id.txt_wind);
        txt_humidity = (TextView) rootView.findViewById(R.id.txt_humidity);
        txt_visibility = (TextView) rootView.findViewById(R.id.txt_visibility);
        txt_uvindex = (TextView) rootView.findViewById(R.id.txt_uvindex);
        txt_nexthourprecip = (TextView) rootView.findViewById(R.id.txt_nexthourprecip);

        refreshCurrentTime();
        showWeatherInformation();
    }

    private void refreshCurrentTime() {
        currentTimer = new CountDownTimer(1000000000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                txt_current_time_weather.setText(currentDateTimeString);
            }

            @Override
            public void onFinish() {

            }
        };
        currentTimer.start();
    }

    private void showWeatherInformation() {
        RequestParams params = new RequestParams();

        AsyncHttpClient client = new AsyncHttpClient();
        String weatherAPIKey = "e0d2184799fefb0c";
        String url = "http://api.wunderground.com/api/" + weatherAPIKey + "/conditions/q/FL/Miami.json";
        client.post(url, null, new PorscheTowerResponseHandler(getActivity()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        JSONObject currentObservation = response.getJSONObject("current_observation");

                        float currentTemperature = Float.valueOf(currentObservation.getString("temp_f"));
                        int temperature = (int)currentTemperature;

                        txt_temperature.setText(String.valueOf(temperature) + "F");
                        txt_feellike_temperature.setText(currentObservation.getString("feelslike_f"));
                        txt_weather.setText(currentObservation.getString("weather"));
                        txt_wind.setText(currentObservation.getString("wind_mph") + " mph");
                        txt_humidity.setText(currentObservation.getString("relative_humidity"));
                        txt_uvindex.setText(currentObservation.getString("UV"));
                        txt_visibility.setText(currentObservation.getString("visibility_mi") + " mi");
                        txt_nexthourprecip.setText(currentObservation.getString("precip_1hr_in") + " in");

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
