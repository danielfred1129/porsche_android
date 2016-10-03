package com.unlimitec.porschetower.pagertransformations;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class FrontPageTransformer implements ViewPager.PageTransformer {


    private static final float MIN_SCALE = 0.55f;
    private static final float MAX_SCALE = 1.0f;
    private static final float MIN_ALPHA = 0.2f;
    private static final float MAX_ALPHA = 1.0f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        position -= 2.0f * 0.15f;

        if (position < -1.0f) {
            view.setAlpha(0.0f);
        } else if (position <= 1.0f) {
            float factor = Math.abs(position);
            factor *= factor;
            float factorComplement = 1.0f - factor;

            float scale = factorComplement * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
            view.setScaleX(scale);
            view.setScaleY(scale);

            Log.e("scaleee", scale + "");

            float alpha = factorComplement * (MAX_ALPHA - MIN_ALPHA) + MIN_ALPHA;
            view.setAlpha(alpha);
            float translationFactor = 0.1f * (float) Math.sin(Math.PI * position - 0.1f * Math.PI) + 0.1f;
            translationFactor *= translationFactor;
            float translationX = -Math.signum(position) * 0.92f * pageWidth * translationFactor;
            view.setTranslationX(translationX);
        } else {
            view.setAlpha(0.0f);
        }
    }

}