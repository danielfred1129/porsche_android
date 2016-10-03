package com.unlimitec.porschetower;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by buddy on 10/3/2016.
 */

public class BaseActivity extends AppCompatActivity {

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}