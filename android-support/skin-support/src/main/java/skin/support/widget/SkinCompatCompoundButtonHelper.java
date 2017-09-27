package skin.support.widget;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CompoundButtonCompat;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import skin.support.R;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 17-1-14.
 */
public class SkinCompatCompoundButtonHelper extends SkinCompatHelper {
    private final CompoundButton mView;
    private SkinCompatTypedValue mButtonTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mButtonTintTypedValue = new SkinCompatTypedValue();

    public SkinCompatCompoundButtonHelper(CompoundButton view) {
        mView = view;
    }

    void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        SkinCompatTypedArray
                .obtain(mView.getContext(), attrs, R.styleable.CompoundButton, defStyleAttr)
                .getValue(R.styleable.CompoundButton_android_button, mButtonTypedValue)
                .getValue(R.styleable.CompoundButton_buttonTint, mButtonTintTypedValue);

        applySkin();
    }

    public void setButtonDrawable(int resId) {
        mButtonTintTypedValue.setData(resId);
        applySkin();
    }

    @Override
    public void applySkin() {
        Drawable buttonDrawable = mButtonTypedValue.getDrawable();
        if (buttonDrawable != null) {
            mView.setButtonDrawable(buttonDrawable);
        }
        ColorStateList tintList = mButtonTintTypedValue.getColorStateList();
        if (tintList != null) {
            CompoundButtonCompat.setButtonTintList(mView, tintList);
        }
    }
}
