package com.pos.porschetower.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.utils.Utils;

import java.util.Calendar;

public class CalendarFragment extends Fragment {

    private View rootView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TYPE = "type";

    // TODO: Rename and change types of parameters
    private String type;


    public CalendarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String mtype) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, mtype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(TYPE))
                type = getArguments().getString(TYPE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_calendar, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl()
    {
        CalendarView calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH);
                int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                if (year > currentYear || (year >= currentYear && month > currentMonth) || (year >= currentYear && month >= currentMonth && dayOfMonth >= currentDayOfMonth))
                {
                    SelectTimeFragment selectTimeFragment = new SelectTimeFragment();
                    Bundle bd = new Bundle();
                    bd.putInt("year", year);
                    bd.putInt("month", month + 1);
                    bd.putInt("dayOfMonth", dayOfMonth);
                    bd.putString("scheduleData", type);
                    selectTimeFragment.setArguments(bd);
                    Utils.replaceFragmentToBackStack(selectTimeFragment, (HomeActivity)getActivity(), true);
                }
                else
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_cant_select_date), Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (currentYear > year || (currentYear <= year && currentMonth > month) || (currentYear <= year && currentMonth <= month && currentDayOfMonth > dayOfMonth))
//                {
//
//                    return;
//                }
            }
        });
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
