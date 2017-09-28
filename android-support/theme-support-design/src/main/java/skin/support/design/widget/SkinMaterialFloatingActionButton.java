package skin.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;
import skin.support.design.R;
import skin.support.widget.SkinCompatImageHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by pengfengwang on 2017/3/1.
 */

public class SkinMaterialFloatingActionButton extends FloatingActionButton implements SkinCompatSupportable {
    private SkinCompatTypedValue mRippleColorTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mBackgroundTintTypedValue = new SkinCompatTypedValue();

    private SkinCompatImageHelper mImageHelper;

    public SkinMaterialFloatingActionButton(Context context) {
        this(context, null);
    }

    public SkinMaterialFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SkinCompatTypedArray
                .obtain(context, attrs, R.styleable.FloatingActionButton, defStyleAttr, R.style.Widget_Design_FloatingActionButton)
                .getValue(R.styleable.FloatingActionButton_backgroundTint, mBackgroundTintTypedValue)
                .getValue(R.styleable.FloatingActionButton_rippleColor, mRippleColorTypedValue);
        applyBackgroundTintResource();
        applyRippleColorResource();

        mImageHelper = new SkinCompatImageHelper(this);
        mImageHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    private void applyBackgroundTintResource() {
        ColorStateList backgroundTint = mBackgroundTintTypedValue.getColorStateList();
        if (backgroundTint != null) {
            setBackgroundTintList(backgroundTint);
        }
    }

    private void applyRippleColorResource() {
        int rippleColor = mRippleColorTypedValue.getColor();
        if (rippleColor != 0) {
            setRippleColor(rippleColor);
        }
    }

    @Override
    public void applySkin() {
        applyBackgroundTintResource();
        applyRippleColorResource();
        if (mImageHelper != null) {
            mImageHelper.applySkin();
        }
    }

}
