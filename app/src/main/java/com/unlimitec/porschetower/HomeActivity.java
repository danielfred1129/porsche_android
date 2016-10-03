package com.unlimitec.porschetower;

import android.graphics.Typeface;
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
import android.widget.TextView;

import com.unlimitec.porschetower.Fragments.HomeFragment;
import com.unlimitec.porschetower.Fragments.SettingsFragment;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.porschetower.utils.UserUtils;

import org.w3c.dom.Text;

public class HomeActivity extends BaseActivity {

    public ImageButton btnSubCategory,btnPlus;
    public TextView txt_main_title, txt_sub_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnSubCategory = (ImageButton) findViewById(R.id.activity_home_sub);
        btnSubCategory.setVisibility(View.GONE);

        Typeface font = Typeface.createFromAsset(getAssets(), "porschedesignfont.otf");
        txt_main_title = (TextView) findViewById(R.id.txt_main_title);
        txt_main_title.setTypeface(font);
        txt_sub_title = (TextView) findViewById(R.id.txt_sub_title);
        txt_sub_title.setTypeface(font);

        String[] mPorschoDesginStringArray = getResources().getStringArray(R.array.title_string_array);

        UserObject user = UserUtils.getSession(this);

        HomeFragment home_fragment = new HomeFragment();

        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragManager.beginTransaction();

        transaction.replace(R.id.home_fragment, home_fragment);
        transaction.commit();
    }

    public void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.home_fragment, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    public void onPlus(View v)
    {
        Log.d("onPlus", "Plus button clicked");
    }

    public void onHome(View v)
    {
        Log.d("onHome", "Home button clicked");
        HomeFragment fragment = new HomeFragment();
        addFragment(fragment);
    }

    public void onSettings(View v)
    {
        Log.d("onSettings", "Settings button clicked");
        SettingsFragment fragment = new SettingsFragment();
        addFragment(fragment);
    }

    public void onSubCategory(View v)
    {
        Log.d("onSubCategory", "SubCategory button clicked");
    }
}
