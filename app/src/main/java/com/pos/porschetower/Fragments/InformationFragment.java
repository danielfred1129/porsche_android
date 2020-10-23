package com.pos.porschetower.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.adapters.InformationListAdapter;
import com.pos.porschetower.datamodel.InformationItem;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.PorscheTowerResponseHandler;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class InformationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";

    // TODO: Rename and change types of parameters
    private String mType;

    private View rootView;
    private ListView listview_information;
    private InformationListAdapter adapter;

    private JSONArray information_array, menu_info_array;

    public InformationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InformationFragment newInstance(String type) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_information, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl() {
        listview_information = (ListView) rootView.findViewById(R.id.listview_information);
        adapter = new InformationListAdapter(new ArrayList<InformationItem>());
        listview_information.setAdapter(adapter);
        listview_information.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject object = information_array.getJSONObject(position);
                    Bundle bd = new Bundle();
                    bd.putString("type", mType);
                    bd.putString("information", object.toString());
                    InformationDetailFragment fragment = new InformationDetailFragment();
                    fragment.setArguments(bd);
                    Utils.addFragmentToBackstack(fragment, (HomeActivity)getActivity(), true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        getInformation();

    }

    private void getInformation() {
        RequestParams params = new RequestParams();
        UserObject user = UserUtils.getSession(getActivity());
        params.put("owner", user.getIndex());
        String funcName = "get_" + mType;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utils.BASE_URL + funcName, params, new PorscheTowerResponseHandler(getActivity()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        information_array = response.getJSONArray(mType);
                        adapter.clearData();
                        if (information_array.length() > 0) {
                            for (int i=0; i<information_array.length(); i++) {

                                InformationItem item = new InformationItem();
                                JSONObject object = information_array.getJSONObject(i);
                                String from = "", message = "";
                                if (mType == "personal_notifications") {
                                    from = object.getString("from");
                                    message = object.getString("subject") + " - " + object.getString("message");
                                }
                                else if (mType == "building_maintenance") {
                                    from = object.getString("from");
                                    message = object.getString("title") + " - " + object.getString("description");
                                }
                                else if (mType == "event_notifications") {
                                    from = object.getString("from");
                                    message = object.getString("brief_description") + " - " + object.getString("detailed_description");
                                }
                                item.from = from;
                                item.message = message;
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

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
