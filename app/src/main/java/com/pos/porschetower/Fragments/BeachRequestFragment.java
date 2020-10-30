package com.pos.porschetower.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.PorscheTowerResponseHandler;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class BeachRequestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LOCATION = "scheduleData";
    private static final String ARG_DATETIME = "Datetime";

    // TODO: Rename and change types of parameters
    private String mLocation;
    private String mDatetime;

    private View rootView;
    private TextView txt_chair_count;

    private int chairs, towels, umbrellas;


    public BeachRequestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BeachRequestFragment newInstance(String location, String datetime) {
        BeachRequestFragment fragment = new BeachRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, location);
        args.putString(ARG_DATETIME, datetime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocation = getArguments().getString(ARG_LOCATION);
            mDatetime = getArguments().getString(ARG_DATETIME);
        }
        chairs = 0;
        umbrellas = 0;
        towels = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_beach_request, container, false);
        txt_chair_count = (TextView) rootView.findViewById(R.id.txt_chair_count);

        ImageButton btn_count_plus = (ImageButton) rootView.findViewById(R.id.btn_count_plus);
        btn_count_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chairs < 99)
                {
                    chairs++;
                    txt_chair_count.setText(String.valueOf(chairs));
                }
            }
        });
        ImageButton btn_count_minus = (ImageButton) rootView.findViewById(R.id.btn_count_minus);
        btn_count_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chairs > 0)
                {
                    chairs--;
                    txt_chair_count.setText(String.valueOf(chairs));
                }
            }
        });

        Button btn_send_request = (Button) rootView.findViewById(R.id.btn_send_request);
        btn_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                UserObject owner = UserUtils.getSession(getActivity());
                params.put("owner", owner.getIndex());
                params.put("location", mLocation);
                params.put("date_time", mDatetime);
                params.put("chairs", chairs);
                params.put("towels", towels);
                params.put("umbrella", umbrellas);

                AsyncHttpClient client = new AsyncHttpClient();
                String funcName = "send_schedule_request_for_pool_beach";
                client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        Utils.showAlert(getActivity(), getResources().getString(R.string.msg_request_sent));
                        UserUtils.storeSelectedCategory(getActivity(), "100");

                        HomeFragment fragment = new HomeFragment();
                        Utils.replaceFragmentToBackStack(fragment, (HomeActivity)getActivity(), false);
                    }
                });
            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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
