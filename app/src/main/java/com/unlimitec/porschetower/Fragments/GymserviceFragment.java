package com.unlimitec.porschetower.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GymserviceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TYPE = "type";
    private static final String INDEX = "index";

    // TODO: Rename and change types of parameters
    private String mType;
    private int mIndex;
    private boolean mHasCall;

    private String phoneNumber, emailAddress;

    private View rootView;
    private TextView txt_description_gym;
    private ImageButton btn_phone_gymservice, btn_email_gymservice;
    private ImageView img_staff;

    public GymserviceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GymserviceFragment newInstance(String type, int index) {
        GymserviceFragment fragment = new GymserviceFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putInt(INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
            mIndex = getArguments().getInt(INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_gymservice, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl() {
        //getting schedule data
        String descriptionStr = UserUtils.getScheduleDataArray(getActivity());

        txt_description_gym = (TextView) rootView.findViewById(R.id.txt_description_gym);
        btn_phone_gymservice = (ImageButton) rootView.findViewById(R.id.btn_phone_gymservice);
        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        btn_phone_gymservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Utils.isCallActive(getActivity())){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
//                }
//                else
//                {
//                    Utils.showAlert(getActivity(), getResources().getString(R.string.msg_call_not_available));
//                    return;
//                }
            }
        });
        btn_email_gymservice = (ImageButton) rootView.findViewById(R.id.btn_email_gymservice);
        btn_email_gymservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        img_staff = (ImageView) rootView.findViewById(R.id.img_staff);
        try {
            JSONArray scheduleArray = new JSONArray(descriptionStr);
            JSONObject object = scheduleArray.getJSONObject(mIndex);
            txt_description_gym.setText(object.getString("description"));
            phoneNumber = object.getString("phone");
            String img_url = object.getString("image");
            final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
            ImageLoader.getInstance().displayImage(img_url, img_staff, options);
        }
        catch (JSONException e)
        {
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
//                    Intent i = getActivity().getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getActivity().getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

    protected void sendEmail() {

        String[] TO = {"tosmos07@outlook.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.showAlert(getActivity(), getResources().getString(R.string.msg_dev_not_configured));
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
