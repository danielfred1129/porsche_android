package com.pos.porschetower.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.adapters.PorscheListAdapter;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.PorscheTowerResponseHandler;
import com.pos.porschetower.utils.FitFragment;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class MenuFragment extends FitFragment {
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
    private String tempReqType;

    private View rootView;
    private ImageButton btnSettings;


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
        btnSettings = getActivity().findViewById(R.id.activity_home_settings_btn);

        final ListView listView ;
        listView = rootView.findViewById(R.id.menu_list_view);

        ImageView img_menuback = (ImageView) rootView.findViewById(R.id.img_menuback);
        if (mMenuType.equals("MainMenu"))
            img_menuback.setAlpha(0.85f);
        else if (mMenuType.equals("SubMenu"))
            img_menuback.setAlpha(1.0f);

        PorscheListAdapter adapter = new PorscheListAdapter(mTitlesArray);
        // Assign adapter to ListView
        listView.setAdapter(adapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                final int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                String mType = getArguments().getString("type");
                int type = Integer.parseInt(mType);

                Fragment fragment;
                Bundle mTypeBundle;

//                Toast.makeText(getActivity().getApplicationContext(), "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
                boolean addToBackStack = true;

                if (type == 101) // Car Elevator - Request Car Elevator
                {
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
                        // This is on review
                        addToBackStack = true;
//                        ((FitFragment)getParentFragment()).addFragment(fragment, R.id.home_fragment, 0);
                        Utils.replaceFragmentToBackStack(fragment, (HomeActivity) getActivity(), addToBackStack);
                    }

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
                    String carIndex = "";
                    String elevator = "";
                    try {
                        carIndex = objectCar.getString("index");
                        elevator = owner.getUnit().getString("elevator1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String valet = UserUtils.getValet(getActivity());

                    String datetimeString = "";
                    String repeat = "";

                    if (getArguments().containsKey("Datetime"))
                        datetimeString = getArguments().getString("Datetime");

                    switch (itemPosition) {
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
                    String funcName = "schedule_car_elevator";
                    client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            if (response != null) {
                                Utils.showAlert(getActivity(), getResources().getString(R.string.msg_has_been_pickup));
                            }
                        }
                    });
                    fragment = new HomeFragment();
                    UserUtils.storeSelectedCategory(getActivity(), "100");
                    Utils.replaceFragmentToBackStack(fragment, (HomeActivity) getActivity(), addToBackStack);
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
                else if (type == 1) // Car Elevator
                {
                    switch(position) {
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
                            fragment = new SchedulePickupsFragment();
                            break;
                        default:
                            fragment = new Fragment();
                            break;
                    }
                    hideSettingsButton();
                    Utils.replaceFragmentToBackStack(fragment, (HomeActivity) getActivity(), addToBackStack);
                }
                else if (type == 2) // In-Unit
                {
                    switch (position) { //Request Maintenance
                        case 0:
                            Calendar c = Calendar.getInstance();
                            int ampm = c.get(Calendar.AM_PM);

                            int year, month, day, hour, minute,second;

                            year = c.get(Calendar.YEAR);
                            month = c.get(Calendar.MONTH) + 1;
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
                            String funcName = "send_schedule_request";
                            client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    if (response != null) {
                                        Utils.showAlert(getActivity(), getResources().getString(R.string.msg_request_sent));
                                    }
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
                else if (type == 3) // Car Concierge
                {
                    fragment = new ShowroomFragment();
                    mTypeBundle = new Bundle();
                    switch (position){
                        case 0:
                            mTypeBundle.putString("car_request_type", "detailing");
                            break;
                        case 1:
                            mTypeBundle.putString("car_request_type", "service_car");
                            break;
                        case 2:
                            mTypeBundle.putString("car_request_type", "storage");
                            break;
                    }
                    hideSettingsButton();
                    fragment.setArguments(mTypeBundle);
                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), addToBackStack);
                }
                else if (type == 4) //Pool-Beach Requests
                {
                    Bundle bd = new Bundle();
                    bd.putString(SCHEDULEDATA, "pool_beach");
                    if (itemPosition == 0)
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
                    int currentDay = c.get(Calendar.DAY_OF_MONTH);
                    bd.putInt(YEAR, currentYear);
                    bd.putInt(MONTH, currentMonth);
                    bd.putInt(DAYOFMONTH, currentDay);

                    select_timefragment.setArguments(bd);
                    Utils.addFragmentToBackstack(select_timefragment, (HomeActivity)getActivity(), true);
                }
                else if (type == 5) //Wellness
                {
                    Bundle bd = new Bundle();
                    String[] menuTitles;

                    if (itemPosition == 1)
                    {
                        menuTitles = getResources().getStringArray(R.array.gym_titles);
                        bd.putString(MENU_TYPE, "SubMenu");
                        bd.putStringArray(TITLES, menuTitles);
                        bd.putString("type", "502");
                        fragment = new MenuFragment();
                        fragment.setArguments(bd);
                        Utils.addFragmentToBackstack(fragment, (HomeActivity) Objects.requireNonNull(getActivity()), true);
                    }
                    else if (itemPosition == 0)
                    {
                        RequestParams params = new RequestParams();
                        UserObject user = UserUtils.getSession(Objects.requireNonNull(getActivity()));
                        params.put("owner", user);
                        AsyncHttpClient client = new AsyncHttpClient();
                        String funcName = "get_spa";

                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);

                                if (response != null) {
                                    try {
                                        JSONArray menu_info_array = response.getJSONArray("spa_list");
                                        UserUtils.storeScheduleDataArray(getActivity(), menu_info_array.toString());
                                        String[] menuTitles = new String[menu_info_array.length()];
                                        if (menu_info_array.length() > 0) {
                                            for (int i = 0; i < menu_info_array.length(); i++) {
                                                JSONObject object = menu_info_array.getJSONObject(i);
                                                menuTitles[i] = object.getString("service");
                                            }
                                        }
                                        MenuFragment fragment = new MenuFragment();
                                        Bundle bd = new Bundle();
                                        bd.putString("menu_type", "SubMenu");
                                        bd.putStringArray("titles", menuTitles);
                                        bd.putString("type", "501");
                                        fragment.setArguments(bd);
                                        Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                                    } catch (JSONException ignored) {
                                    }
                                }
                            }
                        });
                    }
                }
                else if (type == 501 || type == 502) // Wellness -> Salon Spa || Fitness
                {
                    if (type == 502) {
                        RequestParams params = new RequestParams();
                        UserObject user = UserUtils.getSession(Objects.requireNonNull(getActivity()));
                        params.put("owner", user);
                        AsyncHttpClient client = new AsyncHttpClient();
                        if (itemPosition == 0)
                            tempReqType = "gym_trainers";
                        else if (itemPosition == 1)
                            tempReqType = "gym_classes";

                        String funcName = "get_" + tempReqType;

                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    try {
                                        JSONArray menu_info_array = response.getJSONArray(tempReqType + "_list");
                                        UserUtils.storeScheduleDataArray(getActivity(), menu_info_array.toString());
                                        String[] menuTitles = new String[menu_info_array.length()];
                                        if (menu_info_array.length() > 0) {
                                            for (int i = 0; i < menu_info_array.length(); i++) {
                                                JSONObject object = menu_info_array.getJSONObject(i);
                                                if (itemPosition == 0)
                                                    menuTitles[i] = object.getString("staff_name");
                                                else //itemposition == 1
                                                    menuTitles[i] = object.getString("gym_class_name");
                                            }
                                        }
                                        MenuFragment fragment = new MenuFragment();
                                        Bundle bd = new Bundle();
                                        bd.putString("menu_type", "SubMenu");
                                        bd.putStringArray("titles", menuTitles);
                                        bd.putString("type", "502" + String.valueOf(itemPosition + 1));
                                        fragment.setArguments(bd);
                                        Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                                    } catch (JSONException e) {
                                    }
                                }
                            }
                        });
                    }
                    else if (type == 501)
                    {
                        Bundle bd = new Bundle();
                        bd.putString("type", "spa");
                        bd.putInt("index", itemPosition);
                        bd.putBoolean("hasCall", false);
                        openDescriptionFragment(bd);
                    }
                }
                else if (type == 5021 || type == 5022)
                {
                    Bundle bd = new Bundle();
                    if (type == 5021) { // Personal Trainers
                        bd.putString("type", "gym_trainers");
                    }
                    else if (type == 5022) // Classes
                    {
                        bd.putString("type", "gym_trainers");
                    }
                    bd.putInt("index", itemPosition);
                    fragment = new GymserviceFragment();
                    fragment.setArguments(bd);
                    Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                }
                else if (type == 6) { //Activities
                    fragment = new MenuFragment();
                    Bundle bd = new Bundle();
                    bd.putString("menu_type", "SubMenu");
                    bd.putString("type", "60" + String.valueOf(itemPosition + 1));

                    if (itemPosition == 2)
                    {
                        String[] menuTitles = getResources().getStringArray(R.array.theatre_titles);
                        bd.putStringArray("titles", menuTitles);
                        fragment.setArguments(bd);
                        Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);
                    }
                    else {
                        RequestParams params = new RequestParams();
                        UserObject user = UserUtils.getSession(getActivity());
                        params.put("owner", user);
                        AsyncHttpClient client = new AsyncHttpClient();
                        final String strType;
                        if (itemPosition == 0)
                            strType = "golf_sim";
                        else if (itemPosition == 1)
                            strType = "racing_sim";
                        else
                            strType = "community_room";

                        String funcName = "get_" + strType;

                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    try {
                                        JSONArray menu_info_array = response.getJSONArray(strType + "_list");
                                        UserUtils.storeScheduleDataArray(getActivity(), menu_info_array.toString());
                                        String[] menuTitles = new String[menu_info_array.length()];
                                        if (menu_info_array.length() > 0) {
                                            for (int i = 0; i < menu_info_array.length(); i++) {
                                                JSONObject object = menu_info_array.getJSONObject(i);
                                                menuTitles[i] = object.getString("service");
                                            }
                                        }
                                        MenuFragment fragment = new MenuFragment();
                                        Bundle bd = new Bundle();
                                        bd.putString("menu_type", "SubMenu");
                                        bd.putStringArray("titles", menuTitles);
                                        bd.putString("type", "60" + String.valueOf(itemPosition + 1));
                                        fragment.setArguments(bd);
                                        Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);
                                    } catch (JSONException e) {
                                    }
                                }
                            }

                        });
                    }
                }
                else if (type == 603) { //theatre

                }
                else if (type == 601 || type == 602 || type == 604){ // Golfsim / racingsim / community room
                    Bundle bd = new Bundle();
                    String strType;
                    if (type == 601)
                        strType = "golf_sim";
                    else if (type == 602)
                        strType = "racing_sim";
                    else
                        strType = "community_room";
                    bd.putString("type", strType);
                    bd.putInt("index", itemPosition);
                    bd.putBoolean("hasCall", false);
                    openDescriptionFragment(bd);
                }
                else if (type == 7) { //Dining
                    if (itemPosition == 0) { // In House Dining
                        fragment = new MenuFragment();
                        Bundle bd = new Bundle();
                        RequestParams params = new RequestParams();
                        UserObject user = UserUtils.getSession(getActivity());
                        params.put("owner", user);
                        AsyncHttpClient client = new AsyncHttpClient();
                        final String strType = "restaurants_in_house";

                        String funcName = "get_" + strType;

                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);

                                if (response != null) {
                                    try {
                                        JSONArray menu_info_array = response.getJSONArray(strType + "_list");
                                        UserUtils.storeScheduleDataArray(getActivity(), menu_info_array.toString());
                                        String[] menuTitles = new String[menu_info_array.length()];
                                        if (menu_info_array.length() > 0) {
                                            for (int i = 0; i < menu_info_array.length(); i++) {
                                                JSONObject object = menu_info_array.getJSONObject(i);
                                                menuTitles[i] = object.getString("name");
                                            }
                                        }
                                        MenuFragment fragment = new MenuFragment();
                                        Bundle bd = new Bundle();
                                        bd.putString("menu_type", "SubMenu");
                                        bd.putStringArray("titles", menuTitles);
                                        bd.putString("type", "701");
                                        fragment.setArguments(bd);
                                        Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);
                                    } catch (JSONException e) {
                                    }
                                }
                            }

                        });
                    }
                    else if (itemPosition == 1){ // Request a call
                        Utils.showAlertWithTitle(getActivity(), getResources().getString(R.string.msg_order_req_confirmed), getResources().getString(R.string.msg_req_sent_staff_member));
                    }
                }
                else if (type == 701) { // In House Dining
                    Bundle bd = new Bundle();
                    bd.putString("type", "restaurants_in_house");
                    bd.putInt("index", itemPosition);
                    fragment = new DiningFragment();
                    fragment.setArguments(bd);
                    Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                    return;
                }
                else if (type == 8) { //Documents
                    if (itemPosition == 0) //Documents
                    {
                        fragment = new MenuFragment();
                        Bundle bd = new Bundle();
                        RequestParams params = new RequestParams();
                        UserObject user = UserUtils.getSession(getActivity());
                        params.put("owner", user);
                        AsyncHttpClient client = new AsyncHttpClient();
                        final String strType = "document";

                        String funcName = "get_" + strType;

                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);

                                if (response != null) {
                                    try {
                                        JSONArray menu_info_array = response.getJSONArray(strType + "_list");
                                        UserUtils.storeScheduleDataArray(getActivity(), menu_info_array.toString());
                                        String[] menuTitles = new String[menu_info_array.length()];
                                        if (menu_info_array.length() > 0) {
                                            for (int i = 0; i < menu_info_array.length(); i++) {
                                                JSONObject object = menu_info_array.getJSONObject(i);
                                                menuTitles[i] = object.getString("name");
                                            }
                                        }
                                        MenuFragment fragment = new MenuFragment();
                                        Bundle bd = new Bundle();
                                        bd.putString("menu_type", "SubMenu");
                                        bd.putStringArray("titles", menuTitles);
                                        bd.putString("type", "801");
                                        fragment.setArguments(bd);
                                        Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);
                                    } catch (JSONException e) {
                                    }
                                }
                            }

                        });
                    }
                    else if (itemPosition == 1)//Documents -> Unit Manual
                    {
                        RequestParams params = new RequestParams();
                        UserObject user = UserUtils.getSession(getActivity());
                        params.put("owner", user.getIndex());

                        AsyncHttpClient client = new AsyncHttpClient();
                        String funcName = "get_unit_manual";
                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);

                                if (response != null) {
                                    try{
                                        JSONArray menu_info_array = response.getJSONArray("unit_manual");
                                        JSONObject objectDocument = menu_info_array.getJSONObject(0);
                                        String docURL = objectDocument.getString("doc_url");
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(docURL));
                                        startActivity(browserIntent);
                                    } catch (JSONException e) {
                                    }
                                }
                            }
                        });
                    }
                }
                else if (type == 801) { ////Documents -> Documents ->select Item
                    String scheduleString = UserUtils.getScheduleData(getActivity());
                    try {
                        JSONArray scheduleArray = new JSONArray(scheduleString);
                        JSONObject object = scheduleArray.getJSONObject(itemPosition);
                        String strFileURL = object.getString("file");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strFileURL));
                        startActivity(browserIntent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else if (type == 9) { // Information Board
                    String infoType = "";
                    if (itemPosition == 1)
                    {
                        infoType = "personal_notifications";
                    }
                    else if (itemPosition == 2)
                    {
                        infoType = "building_maintenance";
                    }
                    else if (itemPosition == 3)
                    {
                        infoType = "event_notifications";
                    }
                    Bundle bd = new Bundle();
                    bd.putString("type", infoType);
                    fragment = new InformationFragment();
                    fragment.setArguments(bd);
                    Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                    return;
                }

                else if (type == 10) // Local Info
                {
                    if (itemPosition == 0) //Local Info -> Weather
                    {
                        fragment = new WeatherFragment();
                        Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                    }
                    else if (itemPosition == 1) // Local Info -> View Weather
                    {
                        String weatherURL = "https://www.wunderground.com/webcams/zafer/7/show.html";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(weatherURL));
                        startActivity(browserIntent);
                    }
                }
                else if (type == 0) { // Concierge
                    if (itemPosition == 0) // Call Concierge
                    {
                        RequestParams params = new RequestParams();
                        params.put("name", "Concierge");

                        AsyncHttpClient client = new AsyncHttpClient();
                        String funcName = "get_staff";
                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    try {
                                        JSONObject staffObj = response.getJSONObject("staff");
                                        String phoneNumber = staffObj.getString("phone");
                                        String strPhone = phoneNumber.replace(" ", "");
                                        Log.d("strPhone", strPhone);
                                        if (response.getString("status").equals("success")) {
                                            String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
                                            Pattern p = Pattern.compile(URL_REGEX);
                                            Matcher m = p.matcher(strPhone);//replace with string to compare
                                            if(m.find()) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strPhone));
                                                startActivity(browserIntent);
                                            } else {
                                                Utils.showAlertWithTitle(getActivity(), "Alert", getResources().getString(R.string.msg_call_not_available));
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Log.d("my exception", String.valueOf(e));
                                    }
                                }
                            }
                        });
                    }
                    else if (itemPosition == 1) // Request Transportation
                    {
                        UserObject user = UserUtils.getSession(getActivity());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                        String datetimeString = dateFormat.format(new Date());
                        String typeStr = "transportation";

                        RequestParams params = new RequestParams();
                        params.put("type", typeStr);
                        params.put("index", typeStr);
                        params.put("owner", user.getIndex());
                        params.put("date_time", datetimeString);

                        AsyncHttpClient client = new AsyncHttpClient();
                        String funcName = "send_schedule_request";
                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Log.d("send_schedule_request", String.valueOf(response));
                                if (response != null) {
                                    try {
                                        if (response.getString("status").equals("success")) {
                                            Utils.showAlertWithTitle(getActivity(), getResources().getString(R.string.msg_transportation_req_confirmed),
                                                    getResources().getString(R.string.msg_req_sent_staff_member));
                                        }
                                    } catch (JSONException e) {
                                    }
                                }
                            }
                        });
                    }
                    else if (itemPosition == 2) // Dry Cleaning
                    {
                        UserObject user = UserUtils.getSession(getActivity());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                        String datetimeString = dateFormat.format(new Date());
                        String typeStr = "dry_cleaning";

                        RequestParams params = new RequestParams();
                        params.put("type", typeStr);
                        params.put("index", typeStr);
                        params.put("owner", user.getIndex());
                        params.put("date_time", datetimeString);

                        AsyncHttpClient client = new AsyncHttpClient();
                        String funcName = "send_schedule_request";
                        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Log.d("send_schedule_request", String.valueOf(response));
                                if (response != null) {
                                    try {
                                        if (response.getString("status").equals("success")) {
                                            Utils.showAlertWithTitle(getActivity(), "Dry Cleaning Request Confirmed",
                                                    "Your request was sent. A staff member will call you shortly.");
                                        }
                                    } catch (JSONException e) {
                                    }
                                }
                            }
                        });
                    }
                }
                else if (type ==  1003) //Concierge -> Dry Cleaning
                {
                    Bundle bd = new Bundle();
                    bd.putString("type", "dry_cleaning");
                    bd.putInt("index", itemPosition);
                    bd.putBoolean("hasCall", false);
                    openDescriptionFragment(bd);
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
        Utils.addFragmentToBackstack(fragment, (HomeActivity) Objects.requireNonNull(getActivity()), true);
    }

    void hideSettingsButton() {
        btnSettings.setVisibility(View.GONE);
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