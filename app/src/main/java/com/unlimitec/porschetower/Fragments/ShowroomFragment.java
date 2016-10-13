package com.unlimitec.porschetower.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.unlimitec.porschetower.HomeActivity;
import com.unlimitec.porschetower.LoginActivity;
import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.adapters.ShowroomListAdapter;
import com.unlimitec.porschetower.customview.HorizontalListView;
import com.unlimitec.porschetower.datamodel.ShowroomItem;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.porschetower.network.PorscheTowerResponseHandler;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowroomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowroomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private HorizontalListView mCarListView;

    private OnFragmentInteractionListener mListener;
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
                        car_info_array = response.getJSONArray("car_info_list");
                        adapter.clearData();
                        if (car_info_array.length() > 0) {
                            for (int i=0; i<car_info_array.length(); i++) {

                                ShowroomItem item = new ShowroomItem();
                                JSONObject object = car_info_array.getJSONObject(i);
                                String space = object.getString("space");
                                String car_status = object.getString("status");
                                int btn_color = Color.argb(1, 11, 65, 8);
                                int btn_txt_color = Color.WHITE;

                                if (space.equals("garage"))
                                {
                                    btn_color = Color.rgb(156, 3, 20);
                                    space = "Garage";
                                }
                                if (car_status.equals("in"))
                                {
                                    btn_color = Color.rgb(11, 65, 8);
                                    car_status = "IN";
                                }
                                else if (car_status.equals("out"))
                                {
                                    btn_color = Color.rgb(156, 3, 20);
                                    car_status = "OUT";
                                }
                                else if (car_status.equals("active"))
                                {
                                    btn_color = Color.YELLOW;
                                    btn_txt_color = Color.BLACK;
                                    car_status = "ACTIVE";
                                }

                                String status_description;

                                status_description = space + " - " + car_status;
                                item.car_status = car_status;
                                item.img_url = object.getString("image");
                                item.car_name = object.getString("name");
                                item.btn_color = btn_color;
                                item.btn_txt_color = btn_txt_color;
                                item.status_description = status_description;
                                adapter.addData(item);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }

        });

    }

    private void initializeControl()
    {
        mCarListView = (HorizontalListView) rootView.findViewById(R.id.car_listview);
        adapter = new ShowroomListAdapter(new ArrayList<ShowroomItem>());
        mCarListView.setAdapter(adapter);
        mCarListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowroomItem item = (ShowroomItem) adapter.getItem(position);
                if (fragmentType.equals("request")){
                    if (item.car_status.equals("OUT"))
                    {
                        Utils.showAlertWithTitleNoCancel(getActivity(), getString(R.string.title_car_unavailable), getString(R.string.msg_unavailable_contact_valet));
                    }
                    else if (item.car_status.equals("GARAGE")){
                        Utils.showAlertWithTitleNoCancel(getActivity(), getString(R.string.title_car_in_gargage), getString(R.string.msg_parked_gargage_contact_valet));
                    }
                    else
                    {
                        Fragment fragment;
                        Bundle bd = new Bundle();
                        String[] titles = (String[]) getResources().getStringArray(R.array.menu_request_car_elevator);
                        bd.putStringArray("titles", titles);
                        bd.putString("menu_type", "SubMenu");
                        bd.putString("type", "101");
                        JSONObject object = null;
                        try {
                            object = car_info_array.getJSONObject(position);
                            bd.putString("SelectedCar", object.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (item.car_status.equals("ACTIVE")){
                            fragment = new ElevatorControlFragment();
                        }
                        else
                        {
                            fragment = new MenuFragment();
                        }
                        fragment.setArguments(bd);
                        Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                    }
                }
                else if (fragmentType.equals("schedule")){
                    Fragment fragment;
                    Bundle bd = new Bundle();
                    String[] titles = (String[]) getResources().getStringArray(R.array.menu_request_car_elevator);
                    bd.putStringArray("titles", titles);
                    bd.putString("menu_type", "SubMenu");
                    bd.putString("type", "102");
                    JSONObject object = null;
                    try {
                        object = car_info_array.getJSONObject(position);
                        bd.putString("SelectedCar", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    fragment = new MenuFragment();
                    Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                }
                else if (fragmentType.equals("detailing") || fragmentType.equals("service_car") || fragmentType.equals("storage")){
                    RequestParams params = new RequestParams();
                    UserObject user = UserUtils.getSession(getActivity());
                    params.put("owner", user);
                    AsyncHttpClient client = new AsyncHttpClient();
                    String functName = "get_" + fragmentType;

                    client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            if (response != null) {
                                try {
                                    menu_info_array = response.getJSONArray(fragmentType + "_list");
                                    String[] titles_array = new String[menu_info_array.length()];
                                    if (menu_info_array.length() > 0) {
                                        for (int i = 0; i < menu_info_array.length(); i++) {
                                            JSONObject object = menu_info_array.getJSONObject(i);
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
                                    Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                                } catch (JSONException e) {
                                }
                            }
                        }

                    });
                }
            }
        });
        getCarInformation();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
