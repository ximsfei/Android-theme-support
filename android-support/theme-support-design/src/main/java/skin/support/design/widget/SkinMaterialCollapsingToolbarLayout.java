package skin.support.design.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;
import skin.support.design.R;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by ximsfei on 17-3-2.
 */

public class SkinMaterialCollapsingToolbarLayout extends CollapsingToolbarLayout implements SkinCompatSupportable {
    private SkinCompatTypedValue mContentScrimTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mStatusBarScrimTypedValue = new SkinCompatTypedValue();
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinMaterialCollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public SkinMaterialCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SkinCompatTypedArray
                .obtain(context, attrs, R.styleable.CollapsingToolbarLayout, defStyleAttr, R.style.Widget_Design_CollapsingToolbar)
                .getValue(R.styleable.CollapsingToolbarLayout_contentScrim, mContentScrimTypedValue)
                .getValue(R.styleable.CollapsingToolbarLayout_statusBarScrim, mStatusBarScrimTypedValue);
        applyContentScrimResource();
        applyStatusBarScrimResource();
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, 0);
    }

    private void applyStatusBarScrimResource() {
        Drawable drawable = mStatusBarScrimTypedValue.getDrawable();
        if (drawable != null) {
            setStatusBarScrim(drawable);
        }
    }

    private void applyContentScrimResource() {
        Drawable drawable = mContentScrimTypedValue.getDrawable();
        if (drawable != null) {
            setContentScrim(drawable);
        }
    }

    @Override
    public void applySkin() {
        applyContentScrimResource();
        applyStatusBarScrimResource();
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }

}
