package com.pos.porschetower.Fragments;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * Created by coala on 10/19/2020.
 */

public class BaseFragment extends Fragment {

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
