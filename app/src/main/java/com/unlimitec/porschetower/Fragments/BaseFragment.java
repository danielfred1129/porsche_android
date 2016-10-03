package com.unlimitec.porschetower.Fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by buddy on 10/3/2016.
 */

public class BaseFragment extends Fragment {

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
