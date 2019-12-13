package com.pos.porschetower.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pos.porschetower.R;

import org.json.JSONException;
import org.json.JSONObject;


public class InformationDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INFORMATION = "information";
    private static final String ARG_TYPE = "type";

    // TODO: Rename and change types of parameters
    private String mInformation;
    private String mType;

    private View rootView;
    private TextView txt_description_info_detail, txt_location_info, txt_date_info, txt_starttime_info, txt_endtime_info;

    public InformationDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InformationDetailFragment newInstance(String information, String type) {
        InformationDetailFragment fragment = new InformationDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INFORMATION, information);
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInformation = getArguments().getString(ARG_INFORMATION);
            mType = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_information_detail, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl() {
        txt_description_info_detail = (TextView) rootView.findViewById(R.id.txt_description_info_detail);
        txt_location_info = (TextView) rootView.findViewById(R.id.txt_location_info);
        txt_date_info = (TextView) rootView.findViewById(R.id.txt_date_info);
        txt_starttime_info = (TextView) rootView.findViewById(R.id.txt_starttime_info);
        txt_endtime_info = (TextView) rootView.findViewById(R.id.txt_endtime_info);
        try {
            JSONObject infoObject= new JSONObject(mInformation);
            if (mType == "personal_notifications") {
                txt_description_info_detail.setText(infoObject.getString("from") + "\n" + infoObject.getString("subject") + "\n" + infoObject.getString("message"));
            }
            else if (mType == "building_maintenance") {
                txt_description_info_detail.setText(infoObject.getString("from") + "\n" + infoObject.getString("title") + "\n" + infoObject.getString("description"));
                txt_location_info.setText(infoObject.getString("location"));
                txt_date_info.setText(infoObject.getString("start_date") + " ~ " + infoObject.getString("end_date"));
                txt_starttime_info.setText("Start Time: " + infoObject.getString("start_time"));
                txt_endtime_info.setText("End Time: " + infoObject.getString("end_time"));
            }
            else if (mType == "event_notifications") {
                txt_description_info_detail.setText(infoObject.getString("from") + "\n" + infoObject.getString("brief_description")
                        + "\n" + infoObject.getString("detailed_description"));
                txt_location_info.setText(infoObject.getString("location"));
                txt_date_info.setText(infoObject.getString("date"));
                txt_starttime_info.setText("Start Time: " + infoObject.getString("start"));
                txt_endtime_info.setText("End Time: " + infoObject.getString("end"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
