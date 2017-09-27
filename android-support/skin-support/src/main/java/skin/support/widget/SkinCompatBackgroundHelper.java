package skin.support.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import skin.support.R;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 2017/1/10.
 */

public class SkinCompatBackgroundHelper extends SkinCompatHelper {
    private final View mView;
    private final SkinCompatDrawableManager mDrawableManager;

    private SkinCompatTintInfo mInternalBackgroundTint;
    private SkinCompatTintInfo mTmpInfo;

    private SkinCompatTypedValue mBackgroundTypedValue = new SkinCompatTypedValue();

    public SkinCompatBackgroundHelper(View view) {
        mView = view;
        mDrawableManager = SkinCompatDrawableManager.get();
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        SkinCompatTypedArray
                .obtain(mView.getContext(), attrs, R.styleable.SkinBackgroundHelper, defStyleAttr)
                .getValue(R.styleable.SkinBackgroundHelper_android_background, mBackgroundTypedValue);
        applySkin();
    }

    public void onSetBackgroundResource(int resId) {
        mBackgroundTypedValue.setData(resId);
        // Update the default background tint
        applySkin();
    }

    void applySupportBackgroundTint() {
        final Drawable background = mView.getBackground();
        if (background != null) {
            if (shouldApplyFrameworkTintUsingColorFilter()
                    && applyFrameworkTintUsingColorFilter(background)) {
                // This needs to be called before the internal tints below so it takes
                // effect on any widgets using the compat tint on API 21 (EditText)
                return;
            }

            if (mInternalBackgroundTint != null) {
                SkinCompatDrawableManager.tintDrawable(background, mInternalBackgroundTint,
                        mView.getDrawableState());
            }
        }
    }

    void setInternalBackgroundTint(ColorStateList tint) {
        if (tint != null) {
            if (mInternalBackgroundTint == null) {
                mInternalBackgroundTint = new SkinCompatTintInfo();
            }
            mInternalBackgroundTint.mTintList = tint;
            mInternalBackgroundTint.mHasTintList = true;
        } else {
            mInternalBackgroundTint = null;
        }
        applySupportBackgroundTint();
    }

    private boolean shouldApplyFrameworkTintUsingColorFilter() {
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < 21) {
            // API 19 and below doesn't have framework tint
            return false;
        } else if (sdk == 21) {
            // GradientDrawable doesn't implement setTintList on API 21, and since there is
            // no nice way to unwrap DrawableContainers we have to blanket apply this
            // on API 21
            return true;
        } else {
            // On API 22+, if we're using an internal compat background tint, we're also
            // responsible for applying any custom tint set via the framework impl
            return mInternalBackgroundTint != null;
        }
    }

    /**
     * Applies the framework background tint to a view, but using the compat method (ColorFilter)
     *
     * @return true if a tint was applied
     */
    private boolean applyFrameworkTintUsingColorFilter(@NonNull Drawable background) {
        if (mTmpInfo == null) {
            mTmpInfo = new SkinCompatTintInfo();
        }
        final SkinCompatTintInfo info = mTmpInfo;
        info.clear();

        final ColorStateList tintList = ViewCompat.getBackgroundTintList(mView);
        if (tintList != null) {
            info.mHasTintList = true;
            info.mTintList = tintList;
        }
        final PorterDuff.Mode mode = ViewCompat.getBackgroundTintMode(mView);
        if (mode != null) {
            info.mHasTintMode = true;
            info.mTintMode = mode;
        }

        if (info.mHasTintList || info.mHasTintMode) {
            SkinCompatDrawableManager.tintDrawable(background, info, mView.getDrawableState());
            return true;
        }

        return false;
    }

    public void applySkin() {
        ColorStateList tint = mBackgroundTypedValue.getTintList();
        if (tint != null) {
            setInternalBackgroundTint(tint);
        } else {
            Drawable drawable = mBackgroundTypedValue.getDrawable();
            if (drawable != null) {
                ViewCompat.setBackground(mView, drawable);
            }
        }
    }
}
