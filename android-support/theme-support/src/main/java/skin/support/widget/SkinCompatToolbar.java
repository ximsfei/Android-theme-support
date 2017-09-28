package skin.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import skin.support.R;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 17-1-12.
 */

public class SkinCompatToolbar extends Toolbar implements SkinCompatSupportable {
    private SkinCompatTypedValue mTitleTextAppearanceTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mSubtitleTextAppearanceTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mTitleTextColorTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mSubtitleTextColorTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mNavigationIconTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mOverflowIconTypedValue = new SkinCompatTypedValue();
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinCompatToolbar(Context context) {
        this(context, null);
    }

    public SkinCompatToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public SkinCompatToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        SkinCompatTypedArray
                .obtain(context, attrs, R.styleable.Toolbar, defStyleAttr)
                .getValue(R.styleable.Toolbar_navigationIcon, mNavigationIconTypedValue)
                .getValue(R.styleable.Toolbar_titleTextAppearance, mTitleTextAppearanceTypedValue)
                .getValue(R.styleable.Toolbar_subtitleTextAppearance, mSubtitleTextAppearanceTypedValue)
                .getValue(R.styleable.Toolbar_titleTextColor, mTitleTextColorTypedValue)
                .getValue(R.styleable.Toolbar_subtitleTextColor, mSubtitleTextColorTypedValue);

        SkinCompatTypedArray a = SkinCompatTypedArray.obtain(context, R.styleable.SkinCompatImageView, R.attr.actionOverflowButtonStyle);
        a.getValue(R.styleable.SkinCompatImageView_srcCompat, mOverflowIconTypedValue);
        if (mOverflowIconTypedValue.isDataInvalid()) {
            a.getValue(R.styleable.SkinCompatImageView_android_src, mOverflowIconTypedValue);
        }

        applyTitleTextAppearanceResource();
        applySubtitleTextAppearanceResource();

        applyTitleTextColor();
        applySubtitleTextColor();

        applyNavigationIcon();
        applyOverflowIcon();
    }

    private void applyTitleTextAppearanceResource() {
        if (mTitleTextColorTypedValue.isTypeNull()) {
            TypedArray a = mTitleTextAppearanceTypedValue.obtainStyledAttributes(R.styleable.SkinTextAppearance);
            if (a.hasValue(R.styleable.SkinTextAppearance_android_textColor)) {
                int color = a.getColor(R.styleable.SkinTextAppearance_android_textColor, 0);
                if (color != 0) {
                    setTitleTextColor(color);
                }
            }
            a.recycle();
        }
    }

    private void applySubtitleTextAppearanceResource() {
        if (mSubtitleTextColorTypedValue.isTypeNull()) {
            TypedArray a = mSubtitleTextAppearanceTypedValue.obtainStyledAttributes(R.styleable.SkinTextAppearance);
            if (a.hasValue(R.styleable.SkinTextAppearance_android_textColor)) {
                int color = a.getColor(R.styleable.SkinTextAppearance_android_textColor, 0);
                if (color != 0) {
                    setSubtitleTextColor(color);
                }
            }
            a.recycle();
        }
    }

    private void applyTitleTextColor() {
        int titleTextColor = mTitleTextColorTypedValue.getColor();
        if (titleTextColor != 0) {
            setTitleTextColor(titleTextColor);
        }
    }

    private void applySubtitleTextColor() {
        int subtitleTextColor = mSubtitleTextColorTypedValue.getColor();
        if (subtitleTextColor != 0) {
            setSubtitleTextColor(subtitleTextColor);
        }
    }

    private void applyNavigationIcon() {
        Drawable drawable = mNavigationIconTypedValue.getDrawable();
        if (drawable != null) {
            setNavigationIcon(drawable);
        }
    }

    private void applyOverflowIcon() {
        Drawable drawable = mOverflowIconTypedValue.getDrawable();
        if (drawable != null) {
            setOverflowIcon(drawable);
        }
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @Override
    public void setNavigationIcon(@DrawableRes int resId) {
        super.setNavigationIcon(resId);
        mNavigationIconTypedValue.setData(resId);
        applyNavigationIcon();
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }

        applyTitleTextAppearanceResource();
        applySubtitleTextAppearanceResource();

        applyTitleTextColor();
        applySubtitleTextColor();

        applyNavigationIcon();
        applyOverflowIcon();
    }

}
