package com.unlimitec.porschetower.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.LoginActivity;

import java.util.Locale;

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

    private Button btn_logout, btn_reset_password, btn_english, btn_german, btn_italian, btn_spanish, btn_1min, btn_2min, btn_3min, btn_4min;


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
        btn_logout = (Button) rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout(v);
            }
        });
        btn_reset_password = (Button) rootView.findViewById(R.id.btn_reset_password);
        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPassword(v);
            }
        });
        btn_english = (Button) rootView.findViewById(R.id.btn_english);
        btn_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnglish(v);
            }
        });
        btn_german = (Button) rootView.findViewById(R.id.btn_german);
        btn_german.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGerman(v);
            }
        });
        btn_italian = (Button) rootView.findViewById(R.id.btn_italian);
        btn_italian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItalian(v);
            }
        });
        btn_spanish = (Button) rootView.findViewById(R.id.btn_spanish);
        btn_spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpanish(v);
            }
        });
        btn_1min = (Button) rootView.findViewById(R.id.btn_1min);
        btn_1min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                on1Min(v);
            }
        });
        btn_2min = (Button) rootView.findViewById(R.id.btn_2min);
        btn_2min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                on2Min(v);
            }
        });
        btn_3min = (Button) rootView.findViewById(R.id.btn_3min);
        btn_3min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                on3Min(v);
            }
        });
        btn_4min = (Button) rootView.findViewById(R.id.btn_4min);
        btn_4min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                on4Min(v);
            }
        });
        updateSetting();
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

    private void onLogout(View v)
    {
        Log.d("onLogout", "Logout button clicked");
        UserUtils.storeSession(getActivity(), null);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
    private void onResetPassword(View v)
    {
        Log.d("onResetPassword", "ResetPassword button clicked");
    }
    private void onEnglish(View v)
    {
        Log.d("onEnglish", "English button clicked");
        UserObject user = UserUtils.getSession(getContext());
        Locale locale = new Locale("en");
        user.setLocale(locale.toString());
        UserUtils.storeSession(getActivity().getBaseContext(), user);
        setAppLocale(locale);
        updateSetting();
    }
    private void onGerman(View v)
    {
        Log.d("onGerman", "German button clicked");
        UserObject user = UserUtils.getSession(getContext());
        Locale locale = new Locale("de");
        user.setLocale(locale.toString());
        UserUtils.storeSession(getActivity().getBaseContext(), user);
        setAppLocale(locale);
        updateSetting();
    }
    private void onItalian(View v)
    {
        Log.d("onItalian", "Italian button clicked");
        UserObject user = UserUtils.getSession(getContext());
        Locale locale = new Locale("it");
        user.setLocale(locale.toString());
        UserUtils.storeSession(getActivity().getBaseContext(), user);
        setAppLocale(locale);
        updateSetting();
    }
    private void onSpanish(View v)
    {
        Log.d("onSpanish", "Spanish button clicked");
        UserObject user = UserUtils.getSession(getContext());
        Locale locale = new Locale("es");
        user.setLocale(locale.toString());
        UserUtils.storeSession(getActivity().getBaseContext(), user);
        setAppLocale(locale);
        updateSetting();
    }
    private void on1Min(View v)
    {
        UserObject user = UserUtils.getSession(getContext());
        user.setLogoutTime(1);
        UserUtils.storeSession(getActivity().getBaseContext(), user);
        updateSetting();
    }
    private void on2Min(View v)
    {
        UserObject user = UserUtils.getSession(getContext());
        user.setLogoutTime(2);
        UserUtils.storeSession(getActivity().getBaseContext(), user);
        updateSetting();
    }
    private void on3Min(View v)
    {
        UserObject user = UserUtils.getSession(getContext());
        user.setLogoutTime(3);
        UserUtils.storeSession(getActivity().getBaseContext(), user);
        updateSetting();
    }
    private void on4Min(View v)
    {
        UserObject user = UserUtils.getSession(getContext());
        user.setLogoutTime(4);
        UserUtils.storeSession(getActivity().getBaseContext(), user);
        updateSetting();
    }
    private void setAppLocale(Locale locale)
    {
        Resources res = getActivity().getBaseContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }
    private void updateSetting()
    {
        UserObject user = UserUtils.getSession(getContext());

        // Set Language buttons
        String language = user.getLocale().toString();
        if (language.equals("en"))
        {
            btn_english.setBackgroundResource(R.drawable.btn_language_first_selected);
            btn_german.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_italian.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_spanish.setBackgroundResource(R.drawable.btn_language_middle_normal);
        }
        else if (language.equals("de"))
        {
            btn_english.setBackgroundResource(R.drawable.btn_language_first_normal);
            btn_german.setBackgroundResource(R.drawable.btn_language_middle_selected);
            btn_italian.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_spanish.setBackgroundResource(R.drawable.btn_language_middle_normal);
        }
        else if (language.equals("it"))
        {
            btn_english.setBackgroundResource(R.drawable.btn_language_first_normal);
            btn_german.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_italian.setBackgroundResource(R.drawable.btn_language_middle_selected);
            btn_spanish.setBackgroundResource(R.drawable.btn_language_middle_normal);
        }
        else if (language.equals("es"))
        {
            btn_english.setBackgroundResource(R.drawable.btn_language_first_normal);
            btn_german.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_italian.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_spanish.setBackgroundResource(R.drawable.btn_language_last_selected);
        }
        // Set Logout Setting
        int logoutTime = user.getLogoutTime();
        if (logoutTime == 1)
        {
            btn_1min.setBackgroundResource(R.drawable.btn_language_first_selected);
            btn_2min.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_3min.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_4min.setBackgroundResource(R.drawable.btn_language_middle_normal);
        }
        else if (logoutTime == 2)
        {
            btn_1min.setBackgroundResource(R.drawable.btn_language_first_normal);
            btn_2min.setBackgroundResource(R.drawable.btn_language_middle_selected);
            btn_3min.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_4min.setBackgroundResource(R.drawable.btn_language_middle_normal);
        }
        else if (logoutTime == 3)
        {
            btn_1min.setBackgroundResource(R.drawable.btn_language_first_normal);
            btn_2min.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_3min.setBackgroundResource(R.drawable.btn_language_middle_selected);
            btn_4min.setBackgroundResource(R.drawable.btn_language_middle_normal);
        }
        else if (logoutTime == 4)
        {
            btn_1min.setBackgroundResource(R.drawable.btn_language_first_normal);
            btn_2min.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_3min.setBackgroundResource(R.drawable.btn_language_middle_normal);
            btn_4min.setBackgroundResource(R.drawable.btn_language_last_selected);
        }
    }
}
