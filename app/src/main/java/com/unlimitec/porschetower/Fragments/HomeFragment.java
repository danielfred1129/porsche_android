package com.unlimitec.porschetower.Fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.unlimitec.porschetower.HomeActivity;
import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.customview.CustomPager;
import com.unlimitec.porschetower.customview.PorscheTextView;
import com.unlimitec.porschetower.pagertransformations.BackPageTransformer;
import com.unlimitec.porschetower.pagertransformations.FrontPageTransformer;
import com.unlimitec.porschetower.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;

//    SubCategory Button on top left
    ImageButton btnSubCategory;
    /**
     * this text will show title for given desgin
     */
    TextView mDesginShowingTxt;

    /*
    this is front visible view
     */
    CustomPager mFrontViewPager;

    /*
    this is back side view
     */
    CustomPager mBackViewPager;

    /**
     * loop to givee infinite effect to pager
     */
    int LOOP = 3000;


    /**
     * this is margin between two pages in viewpager
     */
    private final int PAGE_MARGEN = 50;

    /**
     * use to handle click on image to show subtitle
     */
    private int pagerCurrentPos = 0;
    String[] mPorschoDesginStringArray;
    TypedArray mMenuTitleTypedArray;

    int[] frontviewarry = {R.drawable._concierge_,
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


    int[] backviewarray = {R.drawable.elevator_,
            R.drawable.apartment,
            R.drawable.garage_,
            R.drawable.poolbeach_,
            R.drawable.wellness_,
            R.drawable.ativities_,
            R.drawable.dinning_,
            R.drawable.noticeboard_,
            R.drawable.info_,
            R.drawable.cloud_,
            R.drawable._concierge
    };

    List<Integer> mFrontList = new ArrayList<>();
    List<Integer> mBackList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Set SubCategory Button with Car Elevator Image
        btnSubCategory = (ImageButton) getActivity().findViewById(R.id.activity_home_sub);
        btnSubCategory.setImageResource(frontviewarry[1]);

        mFrontViewPager = (CustomPager) rootView.findViewById(R.id.front_vp);
        mBackViewPager = (CustomPager) rootView.findViewById(R.id.backgroup_vp);
        mPorschoDesginStringArray = (String[]) getActivity().getResources().getStringArray(R.array.title_string_array);
        mMenuTitleTypedArray = getActivity().getResources().obtainTypedArray(R.array.menutitles_array);
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        settingMeasures();
        settingDataInList();
        settingUpPagerAdatpers();
        setListeners();
    }

    /**
     * here we set dependencies of viewpagers,offscreenlimit,pagemargin,transformation
     */
    private void settingMeasures() {

        int screenwidth = Utils.gettingScreentwidth(getActivity());

        /**
         * synchronizing both viewpagers
         */
        mFrontViewPager.setViewPager(mBackViewPager);
        mBackViewPager.setViewPager(mFrontViewPager);

//        mFrontViewPager.setOffscreenPageLimit(6);
//        mBackViewPager.setOffscreenPageLimit(6);
        mFrontViewPager.setPageTransformer(false, new FrontPageTransformer());
        mBackViewPager.setPageTransformer(false, new BackPageTransformer());

        int mfrontPageMargin = (int) ((int) screenwidth * (float) 7.4 / 100);
        mFrontViewPager.setPageMargin(mfrontPageMargin);
        mBackViewPager.setPageMargin(-(int) screenwidth / 3 - PAGE_MARGEN);


    }


    /**
     * setting data for both viewpagers
     */
    private void settingDataInList() {
        mFrontList = new ArrayList<>();
        mBackList = new ArrayList<>();

        for (int i = 0; i < backviewarray.length; i++) {

            mFrontList.add(frontviewarry[i]);
            mBackList.add(backviewarray[i]);
        }

    }

    private void settingUpPagerAdatpers() {

        mBackViewPager.setAdapter(new BackViewPagerAdatper());
        mFrontViewPager.setAdapter(new FrontViewPagerAdapter());
        mFrontViewPager.setCurrentItem(298);
        mBackViewPager.setCurrentItem(297);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFrontViewPager.setOffscreenPageLimit(6);
                mBackViewPager.setOffscreenPageLimit(6);
            }
        }, 1000);
    }


    private void setListeners() {


        mFrontViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBackViewPager.setCurrentItem(mFrontViewPager.getCurrentItem(), true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
//            }
        });

        mBackViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pos = position % mFrontList.size();
                int tempPos = (pos + 1) % 11;
                pagerCurrentPos = pos;
                PorscheTextView txt_sub_title = (PorscheTextView) getActivity().findViewById(R.id.txt_sub_title);
                txt_sub_title.setText(mPorschoDesginStringArray[pos]);

                //Change SubCategory button on HomeActivity
                btnSubCategory.setVisibility(View.VISIBLE);
                btnSubCategory.setImageResource(frontviewarry[tempPos]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private class FrontViewPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mFrontList.size() * LOOP;
        }

        @Override
        public float getPageWidth(int position) {
            return 0.31f;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            final int pos = position % mFrontList.size();

            View view = getActivity().getLayoutInflater().inflate(R.layout.front_view_row, container, false);
            ImageView image = (ImageView) view.findViewById(R.id.front_iamge);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int posss = pagerCurrentPos >= 10 ? -1 : pagerCurrentPos;
                    if ((posss + 1) == pos) {
//                        Toast.makeText(getActivity(), mPorschoDesginStringArray[pagerCurrentPos], Toast.LENGTH_SHORT).show();
                        //Get TitleArray as a CharSequence
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
                        Utils.addFragmentToBackstack(picker_fragment, (HomeActivity)getActivity(), true);
                    } else if ((pagerCurrentPos) == pos) {
                        mFrontViewPager.setCurrentItem(mFrontViewPager.getCurrentItem() - 1, true);
                    } else if ((pagerCurrentPos) < pos || (pagerCurrentPos) > pos) {
                        mFrontViewPager.setCurrentItem(mFrontViewPager.getCurrentItem() + 1, true);
                    }
                }

            });
            ImageLoader.getInstance().displayImage("drawable://" + mFrontList.get(pos), image, setUpDisplayOptions());
            container.addView(view);

            return view;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class BackViewPagerAdatper extends PagerAdapter {


        @Override
        public int getCount() {
            return mBackList.size() * LOOP;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            int pos = position % mFrontList.size();
            View view = getActivity().getLayoutInflater().inflate(R.layout.back_view, container, false);
            ImageView image = (ImageView) view.findViewById(R.id.back_iamge);
            ImageLoader.getInstance().displayImage("drawable://" + mBackList.get(pos), image, setUpDisplayOptions());
            container.addView(view);


            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    private DisplayImageOptions setUpDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .considerExifParams(true)
                .build();
    }
}