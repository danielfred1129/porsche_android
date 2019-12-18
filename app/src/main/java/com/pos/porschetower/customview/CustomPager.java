package com.pos.porschetower.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class CustomPager extends ViewPager {


    CustomPager mCustomPager;
    private boolean forSuper;

    private boolean backPager;

    public CustomPager(Context context) {
        super(context);
    }

    public CustomPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!forSuper) {

            mCustomPager.forSuper(true);
            mCustomPager.onInterceptTouchEvent(arg0);
            mCustomPager.forSuper(false);
        }
        return super.onInterceptTouchEvent(arg0);
    }

    public void isAmBackPager() {
        backPager = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.onTouchEvent(arg0);
            mCustomPager.forSuper(false);
        }
        return super.onTouchEvent(arg0);


    }


    public void setViewPager(CustomPager customPager) {
        mCustomPager = customPager;
    }

    public void forSuper(boolean forSuper) {
        this.forSuper = forSuper;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.setCurrentItem(item, smoothScroll);
            mCustomPager.forSuper(false);
        }
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.setCurrentItem(item);
            mCustomPager.forSuper(false);
        }
        super.setCurrentItem(item);

    }


    private ScrollerCustomDuration mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    public class ScrollerCustomDuration extends Scroller {

        private double mScrollFactor = 1;

        public ScrollerCustomDuration(Context context) {
            super(context);
        }

        public ScrollerCustomDuration(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @SuppressLint("NewApi")
        public ScrollerCustomDuration(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        /**
         * Set the factor by which the duration will change
         */
        public void setScrollDurationFactor(double scrollFactor) {
            mScrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration / mScrollFactor));
        }

    }

}