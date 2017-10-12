package skin.support.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;

import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 17-1-14.
 */

public class SkinCompatCheckedTextView extends AppCompatCheckedTextView implements SkinCompatSupportable, SkinableTextView {

    private static final int[] TINT_ATTRS = {
            android.R.attr.checkMark
    };
    private SkinCompatTypedValue mCheckMarkTypedValue = new SkinCompatTypedValue();

    private SkinCompatTextHelper mTextHelper;
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinCompatCheckedTextView(Context context) {
        this(context, null);
    }

    public SkinCompatCheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.checkedTextViewStyle);
    }

    public SkinCompatCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        mTextHelper = SkinCompatTextHelper.create(this);
        mTextHelper.loadFromAttributes(attrs, defStyleAttr);

        SkinCompatTypedArray
                .obtain(context, attrs, TINT_ATTRS, defStyleAttr)
                .getValue(0, mCheckMarkTypedValue);
        applyCheckMark();
    }

    @Override
    public void setCheckMarkDrawable(@DrawableRes int resId) {
        mCheckMarkTypedValue.setData(resId);
        applyCheckMark();
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @Override
    public void setTextAppearance(int resId) {
        setTextAppearance(getContext(), resId);
    }

    @Override
    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        if (mTextHelper != null) {
            mTextHelper.onSetTextAppearance(context, resId);
        }
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(
            @DrawableRes int start, @DrawableRes int top, @DrawableRes int end, @DrawableRes int bottom) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        if (mTextHelper != null) {
            mTextHelper.onSetCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        }
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(
            @DrawableRes int left, @DrawableRes int top, @DrawableRes int right, @DrawableRes int bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        if (mTextHelper != null) {
            mTextHelper.onSetCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        }
    }

    @Override
    public void setTextColor(@ColorInt int color) {
        super.setTextColor(color);
        if (mTextHelper != null) {
            mTextHelper.onSetTextColor();
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        if (mTextHelper != null) {
            mTextHelper.onSetTextColor();
        }
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        super.setError(error, icon);
        if (mTextHelper != null) {
            mTextHelper.onSetError(error);
        }
    }

    @Override
    public void setSkinTextColor(int textColor) {
        super.setTextColor(textColor);
    }

    @Override
    public void setSkinTextColor(ColorStateList textColor) {
        super.setTextColor(textColor);
    }

    @Override
    public void setSkinHintTextColor(ColorStateList hintColor) {
        super.setHintTextColor(hintColor);
    }

    @Override
    public void setSkinHighlightColor(int highlightColor) {
        super.setHighlightColor(highlightColor);
    }

    @Override
    public void setSkinCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void setSkinCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
        if (mTextHelper != null) {
            mTextHelper.applySkin();
        }
        applyCheckMark();
    }

    private void applyCheckMark() {
        Drawable drawable = mCheckMarkTypedValue.getDrawable();
        if (drawable != null) {
            setCheckMarkDrawable(drawable);
        }
    }
}
