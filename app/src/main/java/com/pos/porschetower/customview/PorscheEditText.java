package com.pos.porschetower.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by coala on 10/19/2020.
 */

public class PorscheEditText extends androidx.appcompat.widget.AppCompatEditText {
    public PorscheEditText(Context context)
    {
        super(context);
    }
    public PorscheEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface= Typeface.createFromAsset(getResources().getAssets(), "porschedesignfont.otf");
        this.setTypeface(typeface);
    }

    public PorscheEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
