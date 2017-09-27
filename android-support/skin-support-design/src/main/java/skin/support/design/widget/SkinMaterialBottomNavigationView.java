package skin.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;

import skin.support.content.res.SkinCompatResources;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;
import skin.support.design.R;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by ximsfei on 17-3-1.
 */

public class SkinMaterialBottomNavigationView extends BottomNavigationView implements SkinCompatSupportable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};

    private SkinCompatTypedValue mTextColorTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mIconTintTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mDefaultTintTypedValue = new SkinCompatTypedValue();

    public SkinMaterialBottomNavigationView(@NonNull Context context) {
        this(context, null);
    }

    public SkinMaterialBottomNavigationView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SkinCompatTypedArray
                .obtain(context, attrs, R.styleable.BottomNavigationView, defStyleAttr, R.style.Widget_Design_BottomNavigationView)
                .getValue(R.styleable.BottomNavigationView_itemTextColor, mTextColorTypedValue)
                .getValue(R.styleable.BottomNavigationView_itemIconTint, mIconTintTypedValue);
        applyItemIconTintResource();
        applyItemTextColorResource();
    }

    private void applyItemTextColorResource() {
        ColorStateList textColor = mTextColorTypedValue.getColorStateList();
        if (textColor != null) {
            setItemTextColor(textColor);
        } else {
            setItemTextColor(createDefaultColorStateList(android.R.attr.textColorSecondary));
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
        applyItemIconTintResource();
        applyItemTextColorResource();
    }

}
