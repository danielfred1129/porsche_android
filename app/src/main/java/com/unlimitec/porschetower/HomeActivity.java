package com.unlimitec.porschetower;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.unlimitec.porschetower.Fragments.HomeFragment;
import com.unlimitec.porschetower.Fragments.SettingsFragment;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.utils.Utils;

import org.w3c.dom.Text;

import static com.unlimitec.porschetower.R.dimen.settings_button_width;

public class HomeActivity extends BaseActivity {

    public ImageButton btnSubCategory, btnPlus;
    public TextView txt_main_title, txt_sub_title;
    LinearLayout bottom_buttons_layout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnSubCategory = (ImageButton) findViewById(R.id.activity_home_sub);
//        btnSubCategory.setVisibility(View.GONE);

//        bottom_buttons_layout = (LinearLayout) findViewById(R.id.bottom_buttons_layout);

        Typeface font = Typeface.createFromAsset(getAssets(), "porschedesignfont.otf");

        String[] mPorschoDesginStringArray = getResources().getStringArray(R.array.title_string_array);

        UserObject user = UserUtils.getSession(this);

        HomeFragment home_fragment = new HomeFragment();

        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragManager.beginTransaction();

        transaction.replace(R.id.home_fragment, home_fragment);
        transaction.commit();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        ImageButton btnNewBottom = new ImageButton(this);
//        <ImageButton
//        android:id="@+id/activity_home_home_button"
//        android:layout_width="@dimen/settings_button_width"
//        android:layout_height="@dimen/settings_button_width"
//        android:background="@android:color/transparent"
//        android:contentDescription="@string/app_name"
//        android:layout_alignParentLeft="true"
//        android:layout_centerInParent="true"
//        android:onClick="onHome"
//        android:scaleType="fitXY"
//        android:src="@drawable/btn_home" />
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams("@dimen/settings_button_width", "@dimen/settings_button_width");

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        lp.addRule(RelativeLayout.CENTER_IN_PARENT,1);

//        btnNewBottom.setLayoutParams(lp);
//
//        btnNewBottom.setClickable(true);
//        btnNewBottom.setImageResource(R.drawable.btn_home);
//        btnNewBottom.setMinimumHeight(settings_button_width);
//        btnNewBottom.setMinimumWidth(settings_button_width);
//        btnNewBottom.setMaxHeight(settings_button_width);
//        btnNewBottom.setMaxWidth(settings_button_width);
//        btnNewBottom.setScaleType(ImageButton.ScaleType.FIT_XY);
//        btnNewBottom.setBackground(transpa);
//        bottom_buttons_layout.addView(btnNewBottom);
    }

    public void onHome(View v) {
        Log.d("onHome", "Home button clicked");
        HomeFragment fragment = new HomeFragment();
        Utils.addFragmentToBackstack(fragment, this, true);
    }

    public void onSettings(View v) {
        Log.d("onSettings", "Settings button clicked");
        SettingsFragment fragment = new SettingsFragment();
        Utils.addFragmentToBackstack(fragment, this, true);
    }

    public void onSubCategory(View v) {
        Log.d("onSubCategory", "SubCategory button clicked");
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
