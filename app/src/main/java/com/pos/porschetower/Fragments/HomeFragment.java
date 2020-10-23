package com.pos.porschetower.Fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.customview.CustomPager;
import com.pos.porschetower.customview.PorscheTextView;
import com.pos.porschetower.pagertransformations.BackPageTransformer;
import com.pos.porschetower.pagertransformations.FrontPageTransformer;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POS = "position";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mPosition;
    private String mParam2;
    private View rootView;

//    SubCategory Button on top left
    ImageButton btnSubCategory;
    /**
     * this text will show title for given design
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
    String[] mPorscheDesignStringArray;
    TypedArray mMenuTitleTypedArray;

    int[] frontviewarry = {
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

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String position, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POS, position);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl()
    {
//        UserUtils.storeSelectedCategory(Objects.requireNonNull(getActivity()), "100");

        // Set SubCategory Button with Car Elevator Image
        btnSubCategory = Objects.requireNonNull(getActivity()).findViewById(R.id.activity_home_sub);
        btnSubCategory.setImageResource(frontviewarry[1]);

        mFrontViewPager = rootView.findViewById(R.id.front_vp);
        mBackViewPager = rootView.findViewById(R.id.backgroup_vp);
        mPorscheDesignStringArray = getActivity().getResources().getStringArray(R.array.title_string_array);
        mMenuTitleTypedArray = getActivity().getResources().obtainTypedArray(R.array.menutitles_array);
        PorscheTextView txt_sub_title = getActivity().findViewById(R.id.txt_sub_title);
        txt_sub_title.setText(mPorscheDesignStringArray[0]);
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
            mPosition = getArguments().getString(ARG_POS);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingMeasures();
        settingDataInList();
        settingUpPagerAdapters();
        setListeners();
    }

    /**
     * here we set dependencies of viewpagers,offscreenlimit,pagemargin,transformation
     */
    private void settingMeasures() {

        int screenwidth = Utils.gettingScreenWidth(Objects.requireNonNull(getActivity()));

        mFrontViewPager.setViewPager(mBackViewPager);
        mBackViewPager.setViewPager(mFrontViewPager);

//        mFrontViewPager.setOffscreenPageLimit(6);
//        mBackViewPager.setOffscreenPageLimit(6);
        mFrontViewPager.setPageTransformer(false, new FrontPageTransformer());
        mBackViewPager.setPageTransformer(false, new BackPageTransformer());

        int mfrontPageMargin = (int) (screenwidth * (float) 7.4 / 100);
        mFrontViewPager.setPageMargin(mfrontPageMargin);
        mBackViewPager.setPageMargin(-screenwidth / 3 - PAGE_MARGEN);
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

    private void settingUpPagerAdapters() {
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
                PorscheTextView txt_sub_title = Objects.requireNonNull(getActivity()).findViewById(R.id.txt_sub_title);
                txt_sub_title.setText(mPorscheDesignStringArray[pos]);

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

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {

            final int pos = position % mFrontList.size();

            View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.front_view_row, container, false);
            ImageView image = view.findViewById(R.id.front_iamge);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int posss = pagerCurrentPos >= 10 ? -1 : pagerCurrentPos;
                    if ((posss + 1) == pos) {
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
                        UserUtils.storeSelectedCategory(getActivity(), String.valueOf(pos));
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
        public void destroyItem(ViewGroup container, int position, @NonNull Object view) {
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    private class BackViewPagerAdatper extends PagerAdapter {

        @Override
        public int getCount() {
            return mBackList.size() * LOOP;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            int pos = position % mFrontList.size();
            View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.back_view, container, false);
            ImageView image = view.findViewById(R.id.back_iamge);
            ImageLoader.getInstance().displayImage("drawable://" + mBackList.get(pos), image, setUpDisplayOptions());
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object view) {
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
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