package com.pos.porschetower.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TYPE = "type";
    private static final String INDEX = "index";
    private static final String HAS_CALL = "hasCall";

    // TODO: Rename and change types of parameters
    private String mType;
    private int mIndex;
    private boolean mHasCall;

    private View rootView;
    private TextView txt_description;
    private Button btn_select, btn_close;

    public DescriptionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DescriptionFragment newInstance(String type, int index, boolean hasCall) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putInt(INDEX, index);
        args.putBoolean(HAS_CALL, hasCall);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
            mIndex = getArguments().getInt(INDEX);
            mHasCall = getArguments().getBoolean(HAS_CALL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_description, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl()
    {
        btn_close = rootView.findViewById(R.id.btn_close);
        btn_select = rootView.findViewById(R.id.btn_description_select);
        txt_description = rootView.findViewById(R.id.txt_description);
        txt_description.setMovementMethod(new ScrollingMovementMethod());
        String descriptionStr = UserUtils.getScheduleDataArray(Objects.requireNonNull(getActivity()));
        try {
            JSONArray scheduleArray = new JSONArray(descriptionStr);
            JSONObject object = scheduleArray.getJSONObject(mIndex);
            txt_description.setText(object.getString("description"));
        } catch (JSONException e) {
        }

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = null;
                try {
                    String descriptionStr = UserUtils.getScheduleDataArray(getActivity());
                    JSONArray scheduleArray = new JSONArray(descriptionStr);
                    object = scheduleArray.getJSONObject(mIndex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UserUtils.storeScheduleData(getActivity(), object.toString());
                CalendarFragment fragment = new CalendarFragment();
                Bundle bd = new Bundle();
                bd.putString(TYPE, mType);
                fragment.setArguments(bd);
                Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);
//                Utils.replaceFragmentToBackStack(fragment, (HomeActivity)getActivity(), true);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fm.popBackStack();
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
