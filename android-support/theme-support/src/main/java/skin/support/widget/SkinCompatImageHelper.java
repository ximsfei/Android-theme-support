package skin.support.widget;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import skin.support.R;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 2017/1/12.
 */
public class SkinCompatImageHelper extends SkinCompatHelper {
    private static final String TAG = SkinCompatImageHelper.class.getSimpleName();
    private final ImageView mView;
    private SkinCompatTypedValue mSrcTypedValue = new SkinCompatTypedValue();

    public SkinCompatImageHelper(ImageView imageView) {
        mView = imageView;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        SkinCompatTypedArray a = SkinCompatTypedArray.obtain(mView.getContext(),
                attrs, R.styleable.SkinCompatImageView, defStyleAttr);
        a.getValue(R.styleable.SkinCompatImageView_srcCompat, mSrcTypedValue);
        if (mSrcTypedValue.isDataInvalid()) {
            a.getValue(R.styleable.SkinCompatImageView_android_src, mSrcTypedValue);
        }
        applySkin();
    }

    public void setImageResource(int resId) {
        mSrcTypedValue.setData(resId);
        applySkin();
    }

    public void applySkin() {
        Drawable drawable = mSrcTypedValue.getDrawable();
        if (drawable != null) {
            mView.setImageDrawable(drawable);
        }
    }

}
