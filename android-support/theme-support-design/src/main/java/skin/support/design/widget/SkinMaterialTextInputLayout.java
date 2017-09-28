package skin.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.StyleRes;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;
import skin.support.design.R;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatEditText;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by ximsfei on 17-3-2.
 */

public class SkinMaterialTextInputLayout extends TextInputLayout implements SkinCompatSupportable {
    private static final String TAG = SkinMaterialTextInputLayout.class.getSimpleName();
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    private SkinCompatTypedValue mPasswordToggleTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mCounterTextAppearanceTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mErrorTextAppearanceTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mFocusedTextColorTypedValue = new SkinCompatTypedValue();

    public SkinMaterialTextInputLayout(Context context) {
        this(context, null);
    }

    public SkinMaterialTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);

        SkinCompatTypedArray
                .obtain(context, attrs, R.styleable.TextInputLayout, defStyleAttr, R.style.Widget_Design_TextInputLayout)
                .getValue(R.styleable.TextInputLayout_android_textColorHint, mFocusedTextColorTypedValue)
                .getValue(R.styleable.TextInputLayout_errorTextAppearance, mErrorTextAppearanceTypedValue)
                .getValue(R.styleable.TextInputLayout_counterTextAppearance, mCounterTextAppearanceTypedValue)
                .getValue(R.styleable.TextInputLayout_passwordToggleDrawable, mPasswordToggleTypedValue);
        applyFocusedTextColorResource();
        applyErrorTextAppearanceResource();
        applyCounterTextAppearanceResource();
        applyPasswordToggleResource();
    }

    private void applyPasswordToggleResource() {
        Drawable drawable = mPasswordToggleTypedValue.getDrawable();
        if (drawable != null) {
            setPasswordVisibilityToggleDrawable(drawable);
        }
    }

    @Override
    public void setCounterEnabled(boolean enabled) {
        super.setCounterEnabled(enabled);
        if (enabled) {
            applyCounterTextAppearanceResource();
        }
    }

    private void applyCounterTextAppearanceResource() {
        if (mCounterTextAppearanceTypedValue == null) {
            return;
        }
        TypedArray a = mCounterTextAppearanceTypedValue.obtainStyledAttributes(R.styleable.SkinTextAppearance);
        int counterTextColor = a.getColor(R.styleable.SkinTextAppearance_android_textColor, 0);
        if (counterTextColor != 0) {
            TextView counterView = getCounterView();
            if (counterView != null) {
                counterView.setTextColor(counterTextColor);
                updateEditTextBackground();
            }
        }
        a.recycle();
    }

    private TextView getCounterView() {
        try {
            Field counterView = TextInputLayout.class.getDeclaredField("mCounterView");
            counterView.setAccessible(true);
            return (TextView) counterView.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setErrorTextAppearance(@StyleRes int resId) {
        super.setErrorTextAppearance(resId);
        mErrorTextAppearanceTypedValue.setData(resId);
        applyErrorTextAppearanceResource();
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);
        if (enabled) {
            applyErrorTextAppearanceResource();
        }
    }

    private void applyErrorTextAppearanceResource() {
        TypedArray a = mErrorTextAppearanceTypedValue.obtainStyledAttributes(R.styleable.SkinTextAppearance);
        int errorTextColor = a.getColor(R.styleable.SkinTextAppearance_android_textColor, 0);
        if (errorTextColor != 0) {
            TextView errorView = getErrorView();
            if (errorView != null) {
                errorView.setTextColor(errorTextColor);
                updateEditTextBackground();
            }
        }
        a.recycle();
    }

    private TextView getErrorView() {
        try {
            Field errorView = TextInputLayout.class.getDeclaredField("mErrorView");
            errorView.setAccessible(true);
            return (TextView) errorView.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateEditTextBackground() {
        try {
            Method updateEditTextBackground = TextInputLayout.class.getDeclaredMethod("updateEditTextBackground");
            updateEditTextBackground.setAccessible(true);
            updateEditTextBackground.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaultTextColor(ColorStateList colors) {
        try {
            Field defaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            defaultTextColor.setAccessible(true);
            defaultTextColor.set(this, colors);
            updateLabelState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFocusedTextColorResource() {
        ColorStateList focusedTextColor = mFocusedTextColorTypedValue.getColorStateList();
        if (focusedTextColor == null && getEditText() != null) {
            if (getEditText() instanceof SkinCompatEditText) {
                SkinCompatTypedValue typedValue = ((SkinCompatEditText) getEditText()).getTextColorTypedValue();
                focusedTextColor = typedValue.getColorStateList();
                if (focusedTextColor == null) {
                    typedValue = ((SkinCompatEditText) getEditText()).getTextAppearanceTypedValue();
                    TypedArray a = typedValue.obtainStyledAttributes(R.styleable.SkinTextAppearance);
                    focusedTextColor = a.getColorStateList(R.styleable.SkinTextAppearance_android_textColor);
                    a.recycle();
                }
            } else if (getEditText() instanceof SkinMaterialTextInputEditText) {
                SkinCompatTypedValue typedValue = ((SkinMaterialTextInputEditText) getEditText()).getTextColorTypedValue();
                focusedTextColor = typedValue.getColorStateList();
                if (focusedTextColor == null) {
                    typedValue = ((SkinMaterialTextInputEditText) getEditText()).getTextAppearanceTypedValue();
                    TypedArray a = typedValue.obtainStyledAttributes(R.styleable.SkinTextAppearance);
                    focusedTextColor = a.getColorStateList(R.styleable.SkinTextAppearance_android_textColor);
                    a.recycle();
                }
            }
        }
        if (focusedTextColor != null) {
            setFocusedTextColor(focusedTextColor);
        }
    }

    private void setFocusedTextColor(ColorStateList colors) {
        try {
            Field focusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
            focusedTextColor.setAccessible(true);
            focusedTextColor.set(this, colors);
            updateLabelState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLabelState() {
        try {
            Method updateLabelState = TextInputLayout.class.getDeclaredMethod("updateLabelState", boolean.class);
            updateLabelState.setAccessible(true);
            updateLabelState.invoke(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void applySkin() {
        applyErrorTextAppearanceResource();
        applyCounterTextAppearanceResource();
        applyFocusedTextColorResource();
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }

}
