package com.unlimitec.porschetower;

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

import com.unlimitec.porschetower.Fragments.HomeFragment;
import com.unlimitec.porschetower.Fragments.SettingsFragment;

public class HomeActivity extends FragmentActivity {

    ImageButton btnSubCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnSubCategory = (ImageButton) findViewById(R.id.activity_home_sub);
        btnSubCategory.setVisibility(View.GONE);

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
