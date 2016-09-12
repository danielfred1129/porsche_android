package com.unlimitec.porschetower.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        initializeControl();
        return rootView;
    }

    private void initializeControl()
    {
        Button btn_logout = (Button) rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout(v);
            }
        });
        Button btn_reset_password = (Button) rootView.findViewById(R.id.btn_reset_password);
        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPassword(v);
            }
        });
        Button btn_touchid = (Button) rootView.findViewById(R.id.btn_touchid);
        btn_touchid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTouchID(v);
            }
        });
        Button btn_onoff = (Button) rootView.findViewById(R.id.btn_onoff);
        btn_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOnOff(v);
            }
        });
        Button btn_english = (Button) rootView.findViewById(R.id.btn_english);
        btn_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnglish(v);
            }
        });
        btn_english.setBackgroundResource(R.mipmap.btn_language_first_selected);
        Button btn_german = (Button) rootView.findViewById(R.id.btn_german);
        btn_german.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGerman(v);
            }
        });
        Button btn_italian = (Button) rootView.findViewById(R.id.btn_italian);
        btn_italian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItalian(v);
            }
        });
        Button btn_spanish = (Button) rootView.findViewById(R.id.btn_spanish);
        btn_spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpanish(v);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public void onLogout(View v)
    {
        Log.d("onLogout", "Logout button clicked");
        UserUtils.storeSession(getActivity(), null);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
    public void onResetPassword(View v)
    {
        Log.d("onResetPassword", "ResetPassword button clicked");
    }
    public void onTouchID(View v)
    {
        Log.d("onTouchID", "TouchID button clicked");
    }
    public void onOnOff(View v)
    {
        Log.d("onOnOff", "OnOff button clicked");
    }
    public void onEnglish(View v)
    {
        Log.d("onEnglish", "English button clicked");
    }
    public void onGerman(View v)
    {
        Log.d("onGerman", "German button clicked");
    }
    public void onItalian(View v)
    {
        Log.d("onItalian", "Italian button clicked");
    }
    public void onSpanish(View v)
    {
        Log.d("onSpanish", "Spanish button clicked");
    }
}
