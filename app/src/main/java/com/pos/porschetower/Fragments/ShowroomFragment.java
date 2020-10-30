package com.pos.porschetower.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.adapters.ShowroomListAdapter;
import com.pos.porschetower.customview.HorizontalListView;
import com.pos.porschetower.datamodel.ShowroomItem;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.PorscheTowerResponseHandler;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ShowroomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ImageButton btnSettings;

    private String mParam1;
    private String mParam2;

    private View rootView;
    private HorizontalListView mCarListView;

    private JSONArray car_info_array, menu_info_array;
    private ShowroomListAdapter adapter;
    private String fragmentType;

    public ShowroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowroomFragment newInstance(String param1, String param2) {
        ShowroomFragment fragment = new ShowroomFragment();
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
        rootView = inflater.inflate(R.layout.fragment_showroom, container, false);
        if (getArguments() != null && getArguments().containsKey("car_request_type"))
            fragmentType = getArguments().getString("car_request_type");

        initializeControl();
        return rootView;
    }

    private void getCarInformation() {
        RequestParams params = new RequestParams();
        UserObject user = UserUtils.getSession(getActivity());
        params.put("owner_id", user.getId());
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utils.BASE_URL + "get_car_information", params, new PorscheTowerResponseHandler(getActivity()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getString("status").equals("success")) {
                            car_info_array = response.getJSONArray("car_info_list");
                            adapter.clearData();
                            if (car_info_array.length() > 0) {
                                for (int i = 0; i < car_info_array.length(); i++) {
                                    ShowroomItem item = new ShowroomItem();
                                    JSONObject object = car_info_array.getJSONObject(i);
                                    String space = object.getString("space");
                                    String car_status = object.getString("status");
                                    int btn_color = Color.argb(1, 11, 65, 8);
                                    int btn_txt_color = Color.WHITE;

                                    if (space.equals("garage")) {
                                        btn_color = Color.rgb(156, 3, 20);
                                        space = "Garage";
                                    }
                                    switch (car_status) {
                                        case "in":
                                            btn_color = Color.rgb(11, 65, 8);
                                            car_status = "IN";
                                            break;
                                        case "out":
                                            btn_color = Color.rgb(156, 3, 20);
                                            car_status = "OUT";
                                            break;
                                        case "active":
                                            btn_color = Color.YELLOW;
                                            btn_txt_color = Color.BLACK;
                                            car_status = "ACTIVE";
                                            break;
                                    }

                                    String status_description = space + " - " + car_status;
                                    item.car_status = car_status;
                                    item.img_url = object.getString("image");
                                    item.car_name = object.getString("name");
                                    item.btn_color = btn_color;
                                    item.btn_txt_color = btn_txt_color;
                                    item.status_description = status_description;
                                    adapter.addData(item);
                                }
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }

    private void initializeControl() {
        mCarListView = (HorizontalListView) rootView.findViewById(R.id.car_listview);
        adapter = new ShowroomListAdapter(new ArrayList<>());
        mCarListView.setAdapter(adapter);
        getCarInformation();
        mCarListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestParams params = new RequestParams();
                try {
                    params.put("car_index", car_info_array.getJSONObject(position).getString("index"));
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(Utils.BASE_URL + "check_car_availability", params, new PorscheTowerResponseHandler(getActivity()) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            if (response != null) {
                                try {
                                    String location = response.getString("location");
                                    String status = response.getString("status");
                                    String parkingSpaceID = response.getString("parkingSpaceID");
                                    if (status.equals("success")) {
                                        UserUtils.storeParkingSpaceID(getActivity(), parkingSpaceID);
                                        if (location.equals("1")) {
                                            ShowroomItem item = (ShowroomItem) adapter.getItem(position);
                                            switch (fragmentType) {
                                                case "request":
                                                    if (item.car_status.equals("OUT")) {
//                                                        Utils.showAlertWithTitleNoCancel(getActivity(), getString(R.string.title_car_unavailable), getString(R.string.msg_unavailable_contact_valet));
                                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                switch (which){
                                                                    case DialogInterface.BUTTON_POSITIVE:
                                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                                        break;
                                                                }
                                                            }
                                                        };
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle(getString(R.string.title_car_unavailable))
                                                                .setMessage(getString(R.string.msg_unavailable_contact_valet))
                                                                .setPositiveButton("Valet", dialogClickListener)
                                                                .setNegativeButton("Close", dialogClickListener);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                                                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
                                                        dialog.setCanceledOnTouchOutside(false);
                                                    } else if (item.car_status.equals("GARAGE")) {
                                                        Utils.showAlertWithTitleNoCancel(getActivity(), getString(R.string.title_car_in_gargage), getString(R.string.msg_parked_gargage_contact_valet));
                                                    } else {
                                                        Fragment fragment;
                                                        Bundle bd = new Bundle();
                                                        String[] titles = (String[]) getResources().getStringArray(R.array.menu_request_car_elevator);
                                                        bd.putStringArray("titles", titles);
                                                        bd.putString("menu_type", "SubMenu");
                                                        bd.putString("type", "101");
                                                        JSONObject object;
                                                        try {
                                                            object = car_info_array.getJSONObject(position);
                                                            bd.putString("SelectedCar", object.toString());
                                                            UserUtils.storeSelectedCar(getActivity(), object);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (item.car_status.equals("ACTIVE")) {
                                                            fragment = new ElevatorControlFragment();
                                                        } else {
                                                            fragment = new MenuFragment();
                                                        }
                                                        fragment.setArguments(bd);

                                                        Utils.replaceFragmentToBackStack(fragment, (HomeActivity) getActivity(), true);
                                                    }
                                                    break;
                                                case "schedule":
                                                    Fragment fragment;
                                                    Bundle bd = new Bundle();
                                                    String[] titles = (String[]) getResources().getStringArray(R.array.menu_request_car_elevator);
                                                    bd.putStringArray("titles", titles);
                                                    bd.putString("menu_type", "SubMenu");
                                                    bd.putString("type", "102");
                                                    JSONObject object;
                                                    try {
                                                        object = car_info_array.getJSONObject(position);
                                                        bd.putString("SelectedCar", object.toString());
                                                        UserUtils.storeSelectedCar(getActivity(), object);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    fragment = new MenuFragment();
                                                    fragment.setArguments(bd);
                                                    Utils.replaceFragmentToBackStack(fragment, (HomeActivity) getActivity(), true);
                                                    break;
                                                case "detailing":
                                                case "service_car":
                                                case "storage":
                                                    RequestParams params = new RequestParams();
                                                    UserObject user = UserUtils.getSession(getActivity());
                                                    params.put("owner", user);
                                                    AsyncHttpClient client = new AsyncHttpClient();
                                                    String funcName = "get_" + fragmentType;

                                                    client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                            super.onSuccess(statusCode, headers, response);

                                                            if (response != null) {
                                                                try {
                                                                    menu_info_array = response.getJSONArray(fragmentType + "_list");
                                                                    UserUtils.storeScheduleDataArray(getActivity(), menu_info_array.toString());
                                                                    String[] titles_array = new String[menu_info_array.length()];
                                                                    if (menu_info_array.length() > 0) {
                                                                        for (int i = 0; i < menu_info_array.length(); i++) {
                                                                            JSONObject object = menu_info_array.getJSONObject(i);
//                                            UserUtils.storeScheduleData(getActivity(), object.toString());
                                                                            titles_array[i] = object.getString("service");
                                                                        }
                                                                    }
                                                                    MenuFragment fragment = new MenuFragment();
                                                                    Bundle bd = new Bundle();
                                                                    bd.putString("menu_type", "SubMenu");
                                                                    bd.putStringArray("titles", titles_array);
                                                                    if (fragmentType.equals("detailing"))
                                                                        bd.putString("type", "301");
                                                                    else if (fragmentType.equals("service_car"))
                                                                        bd.putString("type", "302");
                                                                    else
                                                                        bd.putString("type", "303");
                                                                    fragment.setArguments(bd);
                                                                    Utils.replaceFragmentToBackStack(fragment, (HomeActivity) getActivity(), true);
                                                                } catch (JSONException e) {
                                                                }
                                                            }
                                                        }
                                                    });
                                                    break;
                                            }
                                        } else {
                                            Utils.showAlertWithTitleNoCancel(getActivity(), "", getString(R.string.msg_car_unavailable));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
