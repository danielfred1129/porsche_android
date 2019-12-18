package com.pos.porschetower;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.pos.porschetower.BaseActivity;
import com.pos.porschetower.Fragments.HomeFragment;
import com.pos.porschetower.Fragments.MenuFragment;
import com.pos.porschetower.Fragments.SettingsFragment;
import com.pos.porschetower.customview.PorscheTextView;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import java.util.ArrayList;
import java.util.List;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.Thing;

public class HomeActivity extends BaseActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "HomeActivity";

    List<Integer> bottom_buttons = new ArrayList<Integer>();
    TypedArray mMenuTitleTypedArray;

    public ImageButton btnSubCategory, btnPlus;
    public TextView txt_main_title, txt_sub_title;
    public PorscheTextView txt_current_time;
    LinearLayout bottom_buttons_layout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    int[] button_images = {
            R.drawable._concierge_,
            R.drawable.elevator,
            R.drawable.apartament,
            R.drawable.garage,
            R.drawable.pool_beach,
            R.drawable.wellness,
            R.drawable.activities,
            R.drawable.dining,
            R.drawable.noticeboard,
            R.drawable.info,
            R.drawable.cloud
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            }
        };
        // Registering BroadcastReceiver

        registerReceiver();

//        if (checkPlayServices()) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//        }

        btnSubCategory = (ImageButton) findViewById(R.id.activity_home_sub);
        btnSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtils.getSelectedCategory(HomeActivity.this).equals("100")){
                    return;
                }
                int pos = Integer.valueOf(UserUtils.getSelectedCategory(HomeActivity.this));
                int tempPos = 0;
                if (pos == 0)
                    tempPos = 10;
                else
                    tempPos = pos - 1;
                CharSequence[] mMenuTitleArray = mMenuTitleTypedArray.getTextArray(tempPos);
                // Convert CharSequence[] to String[]
                String[] mTitlesString = new String[mMenuTitleArray.length];
                int i=0;
                for(CharSequence ch: mMenuTitleArray){
                    mTitlesString[i++] = ch.toString();
                }
                MenuFragment picker_fragment = new MenuFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArray("titles", mTitlesString);
                bundle.putString("menu_type", "MainMenu");
                bundle.putString("type", String.valueOf(pos));
                picker_fragment.setArguments(bundle);

                HomeFragment home_fragment = new HomeFragment();
                String strStatus = UserUtils.getSelectedCategory(HomeActivity.this);
                UserUtils.storeSelectedCategory(HomeActivity.this, String.valueOf(pos));

                Utils.replaceFragmentToBackStack(home_fragment, HomeActivity.this, true);
                Utils.addFragmentToBackstack(picker_fragment, HomeActivity.this, true);
            }
        });
//        btnSubCategory.setVisibility(View.GONE);

        txt_current_time = (PorscheTextView) findViewById(R.id.txt_current_time);
        txt_current_time.setVisibility(View.GONE);

        bottom_buttons_layout = (LinearLayout) findViewById(R.id.bottom_buttons_layout);

        Typeface font = Typeface.createFromAsset(getAssets(), "porschedesignfont.otf");

        String[] mPorschoDesginStringArray = getResources().getStringArray(R.array.title_string_array);
        mMenuTitleTypedArray = getResources().obtainTypedArray(R.array.menutitles_array);
        UserObject user = UserUtils.getSession(this);

        HomeFragment home_fragment = new HomeFragment();

        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragManager.beginTransaction();

        transaction.replace(R.id.home_fragment, home_fragment);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // do something
            PorscheTextView txt_currenttime = (PorscheTextView) findViewById(R.id.txt_current_time);
            txt_currenttime.setVisibility(View.GONE);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.home_fragment, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void onPlus(View v) {
        Log.d("onPlus", "Plus button clicked");
        addBottomButton();
    }
    private void addBottomButton() {
        int status = Integer.valueOf(UserUtils.getSelectedCategory(HomeActivity.this));
        if (status == 100)
            return;
        if (bottom_buttons.size() < 11)
        {
            for (int i = 0; i < bottom_buttons.size(); i++)
            {
                if (bottom_buttons.get(i) == status)
                    return;
            }
            bottom_buttons.add(status);
            addItemToBottomArray(status);
        }
    }
    private void updateBottomBar()
    {
        if (bottom_buttons_layout.getChildCount() > 0)
            bottom_buttons_layout.removeAllViews();
        for (int status = 0; status < bottom_buttons.size(); status++) {
            addItemToBottomArray(status);
        }
    }
    private void addItemToBottomArray(int status) {
        ImageButton btnNewBottom = new ImageButton(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.settings_button_width),
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins((int)getResources().getDimension(R.dimen.bottom_button_margin),0,0,0);
        btnNewBottom.setClickable(true);

        btnNewBottom.setBackgroundResource(button_images[status]);
        btnNewBottom.setLayoutParams(lp);
        btnNewBottom.setId(status);
        btnNewBottom.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int id = v.getId();
                showAlertForRemoveBottomButton(id);
                return true;
            }
        });
        btnNewBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = v.getId();
                int tempPos = 0;
                if (pos == 0)
                    tempPos = 10;
                else
                    tempPos = pos - 1;
                CharSequence[] mMenuTitleArray = mMenuTitleTypedArray.getTextArray(tempPos);
                // Convert CharSequence[] to String[]
                String[] mTitlesString = new String[mMenuTitleArray.length];
                int i=0;
                for(CharSequence ch: mMenuTitleArray){
                    mTitlesString[i++] = ch.toString();
                }
                //Send the titleArray via Bundle
//                        PickerFragment picker_fragment = new PickerFragment();
                MenuFragment picker_fragment = new MenuFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArray("titles", mTitlesString);
                bundle.putString("menu_type", "MainMenu");
                bundle.putString("type", String.valueOf(pos));
                picker_fragment.setArguments(bundle);

                HomeFragment home_fragment = new HomeFragment();
                String strStatus = UserUtils.getSelectedCategory(HomeActivity.this);
                if (UserUtils.getSelectedCategory(HomeActivity.this).equals("100")) {
                    Utils.replaceFragmentToBackStack(home_fragment, HomeActivity.this, true);
                    Utils.addFragmentToBackstack(picker_fragment, HomeActivity.this, true);
                }
                else {
                    Utils.replaceFragmentToBackStack(picker_fragment, HomeActivity.this, true);
                }
                UserUtils.storeSelectedCategory(HomeActivity.this, String.valueOf(pos));
            }
        });
        bottom_buttons_layout.addView(btnNewBottom);
    }
    private void showAlertForRemoveBottomButton(final int status) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Are you sure to remove this button?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
                for (int i = 0; i < bottom_buttons.size(); i++)
                {
                    if (bottom_buttons.get(i) == status) {
                        bottom_buttons.remove(i);
                        updateBottomBar();
                        return;
                    }
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void onHome(View v) {
        Log.d("onHome", "Home button clicked");
        HomeFragment fragment = new HomeFragment();
        PorscheTextView txt_currenttime = (PorscheTextView) findViewById(R.id.txt_current_time);
        txt_currenttime.setVisibility(View.GONE);

        UserUtils.storeSelectedCategory(HomeActivity.this, "100");
        Utils.replaceFragmentToBackStack(fragment, this, false);
    }

    public void onSettings(View v) {
        Log.d("onSettings", "Settings button clicked");
        SettingsFragment fragment = new SettingsFragment();
        Utils.replaceFragmentToBackStack(fragment, this, true);
    }

    public void onSubCategory(View v) {
        Log.d("onSubCategory", "SubCategory button clicked");
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
