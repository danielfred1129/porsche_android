package com.unlimitec.porschetower.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.unlimitec.porschetower.HomeActivity;
import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.adapters.PorscheListAdapter;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.porschetower.network.PorscheTowerResponseHandler;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MENU_TYPE = "menu_type";
    private static final String TITLES = "titles";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAYOFMONTH = "dayOfMonth";
    private static final String SCHEDULEDATA = "scheduleData";

    // TODO: Rename and change types of parameters
    private String mMenuType;
    private String[] mTitlesArray;


    private View rootView;


    public MenuFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String menuType, String titlesArray) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(MENU_TYPE, menuType);
        args.putString(TITLES, titlesArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(MENU_TYPE)) {
                mMenuType = getArguments().getString(MENU_TYPE);
            }

            //  Get Titles Arguments from PickerFragment
            if (getArguments().containsKey("titles")) {
                mTitlesArray = getArguments().getStringArray("titles");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        final ListView listView ;
        listView = (ListView) rootView.findViewById(R.id.menu_list_view);

        ImageView img_memuback = (ImageView) rootView.findViewById(R.id.img_memuback);
        if (mMenuType.equals("MainMenu"))
            img_memuback.setAlpha(0.85f);
        else if (mMenuType.equals("SubMenu"))
            img_memuback.setAlpha(1.0f);

        PorscheListAdapter adapter = new PorscheListAdapter(mTitlesArray);
        // Assign adapter to ListView
        listView.setAdapter(adapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                String mType = getArguments().getString("type");
                int type = Integer.parseInt(mType);

                Fragment fragment;
                Bundle mTypeBundle;

//                Toast.makeText(getActivity().getApplicationContext(), "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
                boolean addToBackStack = true;

                if (type == 101) // Car Elevator - Request Car Elevator
                {
                    fragment = new ElevatorControlFragment();
                    if (getArguments().containsKey("SelectedCar"))
                    {
                        String selectedCar=getArguments().getString("SelectedCar");
                        fragment = new ElevatorControlFragment();
                        Bundle bd = new Bundle();
                        bd.putString("SelectedCar", selectedCar);
                        if (position == 0) {
                            bd.putString("valet", "ridedown");
                            bd.putInt("delayTime", 0);
                        }
                        else {
                            bd.putString("valet", "valet");
                            bd.putInt("delayTime", 95);
                        }
                        fragment.setArguments(bd);
                    }
                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), addToBackStack);
                    // This is on review
//                    addToBackStack = false;
                }
                else if (type == 102) // Car Elevator - Schedule
                {
                    fragment = new CalendarFragment();
                    Bundle bd = new Bundle();
                    bd.putString("type", "showroom_booking");
//                    bd.putString("valet", mTitlesArray[itemPosition]);
                    UserUtils.storeValet(getActivity(), mTitlesArray[itemPosition]);
                    fragment.setArguments(bd);
                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), addToBackStack);
                }
                else if (type == 1021) // Car Elevator - Repeat Schedule - Selecting Car -> RideDown /Valet -> Select DateTime ->
                {
                    UserObject owner = UserUtils.getSession(getActivity());
                    JSONObject objectCar = UserUtils.getSelectedCar(getActivity());
                    String carIndex = new String();
                    String elevator = new String();
                    try {
                        carIndex = objectCar.getString("index");
                        elevator = owner.getUnit().getString("elevator1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String valet = UserUtils.getValet(getActivity());

                    String datetimeString = new String();
                    String repeat = "";

                    if (getArguments().containsKey("Datetime"))
                        datetimeString = getArguments().getString("Datetime");

                    switch (itemPosition) {
                        case 0:
                            repeat = "none";
                            break;
                        case 1:
                            repeat = "daily";
                            break;
                        case 2:
                            repeat = "weekly";
                            break;
                        case 3:
                            repeat = "monthlyByDay";
                            break;
                        case 4:
                            repeat = "monthlyByDate";
                            break;
                        default:
                            repeat = "none";
                            break;
                    }

                    RequestParams params = new RequestParams();
                    params.put("car", carIndex);
                    params.put("valet", valet);
                    params.put("elevator", elevator);
                    params.put("request_time", datetimeString);
                    params.put("repeat", repeat);

                    AsyncHttpClient client = new AsyncHttpClient();
                    String functName = "schedule_car_elevator";
                    client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            if (response != null) {
                                Utils.showAlert(getActivity(), getResources().getString(R.string.msg_has_been_pickup));
                            }
                        }
                    });
                    fragment = new HomeFragment();
                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), addToBackStack);
                }
                else if (type == 301 || type == 302 || type == 303) // Car Concierge - Detailing / Service / Storage
                {
                    String serviceType = null;
                    if (type == 301)
                        serviceType = "detailing";
                    else if (type == 302)
                        serviceType = "service_car";
                    else if (type == 303)
                        serviceType = "storage";

                    Bundle bd = new Bundle();
                    bd.putString("type", serviceType);
                    bd.putInt("index", itemPosition);
                    bd.putBoolean("hasCall", false);
                    openDescriptionFragment(bd);
                }
                else if (type  == 1) // Car Elevator
                {
                    switch(position){
                        case 0:
                            fragment = new ShowroomFragment();
                            mTypeBundle = new Bundle();
                            mTypeBundle.putString("car_request_type", "request");
                            fragment.setArguments(mTypeBundle);
                            break;
                        case 1:
                            fragment = new ShowroomFragment();
                            mTypeBundle = new Bundle();
                            mTypeBundle.putString("car_request_type", "schedule");
                            fragment.setArguments(mTypeBundle);
                            break;
                        case 2:
                            fragment = new ShowroomFragment();
                            break;
                        default:
                            fragment = new Fragment();
                            break;
                    }
                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), addToBackStack);
                }
                else if (type == 2)
                {
                    switch (position) {
                        case 0:
                            Calendar c = Calendar.getInstance();
                            int ampm = c.get(Calendar.AM_PM);

                            int year, month, day, hour, minute,second;

                            year = c.get(Calendar.YEAR);
                            month = c.get(Calendar.MONTH);
                            day = c.get(Calendar.DAY_OF_MONTH);
                            hour = c.get(Calendar.HOUR);
                            minute = c.get(Calendar.MINUTE);
                            second = c.get(Calendar.SECOND);
                            if (ampm == 1)
                                hour = hour + 12;

                            String dateTimeString =  year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00";
                            String index_num = "5";
                            String request_type = "request_maintenance";

                            RequestParams params = new RequestParams();
                            params.put("type", request_type);
                            params.put("index", index_num);
                            UserObject object = UserUtils.getSession((HomeActivity)getActivity());
                            params.put("owner", object.getIndex());
                            params.put("date_time", dateTimeString);


                            AsyncHttpClient client = new AsyncHttpClient();
                            String functName = "send_schedule_request";
                            client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    Utils.showAlert(getActivity(), getResources().getString(R.string.msg_request_sent));
                                }
                            });
                            break;
                        case 1:
                            Utils.showAlertWithTitleNoCancel(getActivity(), getResources().getString(R.string.msg_front_desk_req_confirmed),
                                    getResources().getString(R.string.msg_req_sent_front_desk));
                            break;
                        case 2:
                            break;
                        case 3:
                            Utils.showAlertWithTitleNoCancel(getActivity(), getResources().getString(R.string.msg_security_req_confirmed),
                                    getResources().getString(R.string.msg_req_sent_security));
                            break;
                    }
//                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), addToBackStack);
                }
                else if (type == 3)
                {
                    switch (position){
                        case 0:
                            fragment = new ShowroomFragment();
                            mTypeBundle = new Bundle();
                            mTypeBundle.putString("car_request_type", "detailing");
                            fragment.setArguments(mTypeBundle);
                            break;
                        case 1:
                            fragment = new ShowroomFragment();
                            mTypeBundle = new Bundle();
                            mTypeBundle.putString("car_request_type", "service_car");
                            fragment.setArguments(mTypeBundle);
                            break;
                        case 2:
                            fragment = new ShowroomFragment();
                            mTypeBundle = new Bundle();
                            mTypeBundle.putString("car_request_type", "storage");
                            fragment.setArguments(mTypeBundle);
                            break;
                        default:
                            fragment = new Fragment();
                    }
                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), addToBackStack);
                }
                else if (type ==4)
                {
                    Bundle bd = new Bundle();
                    bd.putString(SCHEDULEDATA, "pool_beach");
                    if (position == 0)
                    {
                        bd.putString("Location", "Pool");
                    }
                    else
                    {
                        bd.putString("Location", "Beach");
                    }
                    SelectTimeFragment select_timefragment = new SelectTimeFragment();
                    Calendar c = Calendar.getInstance();
                    int currentYear = c.get(Calendar.YEAR);
                    int currentMonth = c.get(Calendar.MONTH) + 1;
                    int currentDay = c.get(Calendar.DAY_OF_MONTH) + 1;
                    bd.putInt(YEAR, currentYear);
                    bd.putInt(MONTH, currentMonth);
                    bd.putInt(DAYOFMONTH, currentDay);

                    select_timefragment.setArguments(bd);
                    Utils.addFragmentToBackstack(select_timefragment, (HomeActivity)getActivity(), true);
                }
                else {
                    fragment = new MenuFragment();
//                    String[] mTitlesString = {"abc", "def", "wer"};
//                    Bundle bundle = new Bundle();
//                    bundle.putStringArray("titles", mTitlesString);
//                    bundle.putString("menu_type", "SubMenu");
//                    fragment.setArguments(bundle);
                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), addToBackStack);
                }
            }
        });

        return rootView;
    }

    private void openDescriptionFragment(Bundle bd)
    {
        DescriptionFragment fragment = new DescriptionFragment();
        fragment.setArguments(bd);
        Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);
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