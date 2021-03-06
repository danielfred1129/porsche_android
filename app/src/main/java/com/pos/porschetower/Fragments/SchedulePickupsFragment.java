package com.pos.porschetower.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.adapters.PickupsListAdapter;
import com.pos.porschetower.datamodel.PickupsItem;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.PorscheTowerResponseHandler;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SchedulePickupsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ImageButton btnHome, btnPlus;
    private LinearLayout bottom_buttons_layout;
    private String mParam1;
    private String mParam2;

    private View rootView;
    private ListView listview_pickups;
    private PickupsListAdapter adapter;

    private JSONArray information_array, menu_info_array;

    public SchedulePickupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulePickupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SchedulePickupsFragment newInstance(String param1, String param2) {
        SchedulePickupsFragment fragment = new SchedulePickupsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_schedule_pickups, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl() {
        listview_pickups = (ListView) rootView.findViewById(R.id.listview_pickups);
        adapter = new PickupsListAdapter((HomeActivity)getActivity(),new ArrayList<PickupsItem>());
        listview_pickups.setAdapter(adapter);
        getInformation();
    }

    private void getInformation() {
        RequestParams params = new RequestParams();
        UserObject user = UserUtils.getSession(getActivity());
        params.put("owner", user.getIndex());
        String funcName = "get_scheduled_pickups";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        information_array = response.getJSONArray("scheduled_pickups");
                        UserUtils.storeScheduleData((HomeActivity)getActivity(), information_array.toString());
                        adapter.clearData();
                        if (information_array.length() > 0) {
                            for (int i = 0; i < information_array.length(); i++) {
                                PickupsItem item = new PickupsItem();
                                JSONObject object = information_array.getJSONObject(i);
                                JSONObject car_object = object.getJSONObject("car");
                                String date_time = object.getString("request_time");
                                String[] splited = date_time.split("\\s+");

                                String car_name = car_object.getString("name");
                                item.car_name = car_name;
                                item.date = splited[0];
                                item.time = splited[1];
                                adapter.addData(item);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        btnHome = (ImageButton) getActivity().findViewById(R.id.activity_home_home_button);
        btnPlus = (ImageButton) getActivity().findViewById(R.id.activity_home_plus_button);
        bottom_buttons_layout = (LinearLayout) getActivity().findViewById(R.id.bottom_buttons_layout);
        btnHome.setVisibility(View.GONE);
        btnPlus.setVisibility(View.GONE);
        bottom_buttons_layout.setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        btnHome.setVisibility(View.VISIBLE);
        btnPlus.setVisibility(View.VISIBLE);
        bottom_buttons_layout.setVisibility(View.VISIBLE);
    }
}
