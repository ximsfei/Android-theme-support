package skin.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;

import skin.support.content.res.SkinCompatResources;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;
import skin.support.design.R;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by pengfengwang on 2017/1/15.
 */

public class SkinMaterialNavigationView extends NavigationView implements SkinCompatSupportable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};
    private SkinCompatTypedValue mItemTextAppearanceTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mItemBackgroundTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mTextColorTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mIconTintTypedValue = new SkinCompatTypedValue();

    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinMaterialNavigationView(Context context) {
        this(context, null);
    }

    public SkinMaterialNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, 0);
        SkinCompatTypedArray
                .obtain(context, attrs, R.styleable.NavigationView, defStyleAttr, R.style.Widget_Design_NavigationView)
                .getValue(R.styleable.NavigationView_itemTextAppearance, mItemTextAppearanceTypedValue)
                .getValue(R.styleable.NavigationView_itemIconTint, mIconTintTypedValue)
                .getValue(R.styleable.NavigationView_itemTextColor, mTextColorTypedValue)
                .getValue(R.styleable.NavigationView_itemBackground, mItemBackgroundTypedValue);
        applyItemTextAppearanceResource();
        applyItemIconTintResource();
        applyItemTextColorResource();
        applyItemBackgroundResource();
    }

    @Override
    public void setItemBackgroundResource(@DrawableRes int resId) {
        super.setItemBackgroundResource(resId);
        mItemBackgroundTypedValue.setData(resId);
        applyItemBackgroundResource();
    }

    private void applyItemBackgroundResource() {
        Drawable drawable = mItemBackgroundTypedValue.getDrawable();
        if (drawable != null) {
            setItemBackground(drawable);
        }
    }

    @Override
    public void setItemTextAppearance(@StyleRes int resId) {
        super.setItemTextAppearance(resId);
        mItemTextAppearanceTypedValue.setData(resId);
        mTextColorTypedValue.reset();
        applyItemTextAppearanceResource();
    }

    private void applyItemTextAppearanceResource() {
        if (mTextColorTypedValue.isTypeNull()) {
            TypedArray a = mItemTextAppearanceTypedValue.obtainStyledAttributes(R.styleable.SkinTextAppearance);
            if (a.hasValue(R.styleable.SkinTextAppearance_android_textColor)) {
                setItemTextColor(a.getColorStateList(R.styleable.SkinTextAppearance_android_textColor));
            }
            a.recycle();
        }
    }

    private void applyItemTextColorResource() {
        ColorStateList textColor = mTextColorTypedValue.getColorStateList();
        if (textColor != null) {
            setItemTextColor(textColor);
        } else if (mItemTextAppearanceTypedValue.isTypeNull()) {
            setItemTextColor(createDefaultColorStateList(android.R.attr.textColorPrimary));
        }
    }

    private void applyItemIconTintResource() {
        ColorStateList iconTint = mIconTintTypedValue.getColorStateList();
        if (iconTint != null) {
            setItemIconTintList(iconTint);
        } else {
            setItemIconTintList(createDefaultColorStateList(android.R.attr.textColorSecondary));
        }
    }

    private ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        ColorStateList baseColor = SkinCompatResources.getInstance()
                .obtainStyledAttributes(getContext(), new int[]{baseColorThemeAttr}).getColorStateList(0);

        int colorPrimary = SkinCompatResources.getInstance()
                .obtainStyledAttributes(getContext(), new int[]{R.attr.colorPrimary}).getColor(0, 0);
        if (baseColor == null || colorPrimary == 0) {
            return null;
        }

        int defaultColor = baseColor.getDefaultColor();
        return new ColorStateList(new int[][]{
                DISABLED_STATE_SET,
                CHECKED_STATE_SET,
                EMPTY_STATE_SET
        }, new int[]{
                baseColor.getColorForState(DISABLED_STATE_SET, defaultColor),
                colorPrimary,
                defaultColor
        });
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
        applyItemTextAppearanceResource();
        applyItemIconTintResource();
        applyItemTextColorResource();
        applyItemBackgroundResource();
    }

}
