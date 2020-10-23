package com.pos.porschetower;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by coala on 10/19/2020.
 */

public class BaseActivity extends AppCompatActivity {

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}