package skin.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;

import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;
import skin.support.design.R;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by ximsfei on 17-1-14.
 */

public class SkinMaterialTabLayout extends TabLayout implements SkinCompatSupportable {
    private SkinCompatTypedValue mTabTextAppearanceTypeValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mTabIndicatorColorTypeValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mTabTextColorsTypeValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mTabSelectedTextColorTypeValue = new SkinCompatTypedValue();

    public SkinMaterialTabLayout(Context context) {
        this(context, null);
    }

    public SkinMaterialTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SkinCompatTypedArray.obtain(context, attrs, R.styleable.TabLayout, defStyleAttr, R.style.Widget_Design_TabLayout)
                .getValue(R.styleable.TabLayout_tabIndicatorColor, mTabIndicatorColorTypeValue)
                .getValue(R.styleable.TabLayout_tabTextAppearance, mTabTextAppearanceTypeValue)
                .getValue(R.styleable.TabLayout_tabTextColor, mTabTextColorsTypeValue)
                .getValue(R.styleable.TabLayout_tabSelectedTextColor, mTabSelectedTextColorTypeValue);
        applySkin();
    }

    @Override
    public void applySkin() {
        int indicatorColor = mTabIndicatorColorTypeValue.getColor();
        if (indicatorColor != 0) {
            setSelectedTabIndicatorColor(indicatorColor);
        }
        ColorStateList textColors;
        if (mTabTextColorsTypeValue.isTypeNull()) {
            TypedArray a = mTabTextAppearanceTypeValue.obtainStyledAttributes(R.styleable.SkinTextAppearance, R.style.TextAppearance_Design_Tab);
            textColors = a.getColorStateList(R.styleable.SkinTextAppearance_android_textColor);
            a.recycle();
        } else {
            textColors = mTabTextColorsTypeValue.getColorStateList();
        }
        if (textColors != null) {
            int selectedTextColor = mTabSelectedTextColorTypeValue.getColor();
            if (selectedTextColor != 0) {
                setTabTextColors(textColors.getDefaultColor(), selectedTextColor);
            } else {
                setTabTextColors(textColors);
            }
        }
    }

}
