package com.appspace.evyalert.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

import com.appspace.evyalert.R;

/**
 * Created by siwaweswongcharoen on 8/17/2016 AD.
 */
public class TopCenterRadioButton extends RadioButton {

    Drawable buttonDrawable;

    @SuppressLint("Recycle")
    public TopCenterRadioButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray a = context
                .obtainStyledAttributes(attributeSet, R.styleable.CompoundButton, 0, 0);
        buttonDrawable = a.getDrawable(0);
        setButtonDrawable(android.R.color.transparent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (buttonDrawable != null) {
            buttonDrawable.setState(getDrawableState());
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = buttonDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            int buttonWidth = buttonDrawable.getIntrinsicWidth();
            int buttonLeft = (getWidth() - buttonWidth) / 2;
            buttonDrawable.setBounds(buttonLeft, y, buttonLeft + buttonWidth, y + height);
            buttonDrawable.draw(canvas);
        }
    }
}
