package com.unlimitec.porschetower.customview;

/**
 * Created by buddy on 10/5/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class PorscheTextView extends TextView{

    public PorscheTextView(Context context) {
        super(context);
    }

    public PorscheTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface= Typeface.createFromAsset(getResources().getAssets(), "porschedesignfont.otf");
        this.setTypeface(typeface);
    }

    public PorscheTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
