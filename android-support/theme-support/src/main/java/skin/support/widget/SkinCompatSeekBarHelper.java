package skin.support.widget;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;

import skin.support.R;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 17-1-21.
 */
public class SkinCompatSeekBarHelper extends SkinCompatProgressBarHelper {
    private final SeekBar mView;

    private SkinCompatTypedValue mThumbTypedValue = new SkinCompatTypedValue();

    public SkinCompatSeekBarHelper(SeekBar view) {
        super(view);
        mView = view;
    }

    @Override
    void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        SkinCompatTypedArray
                .obtain(mView.getContext(), attrs, R.styleable.AppCompatSeekBar, defStyleAttr)
                .getValue(R.styleable.AppCompatSeekBar_android_thumb, mThumbTypedValue);
        super.loadFromAttributes(attrs, defStyleAttr);
    }

    @Override
    public void applySkin() {
        super.applySkin();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = mThumbTypedValue.getDrawable();
            if (drawable != null) {
                mView.setThumb(drawable);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (mThumbTypedValue.isTypeNull() && mThumbTypedValue.isDataInvalid()) {
                Drawable drawable = mView.getThumb();
                if (drawable != null) {
                    int color = SkinCompatThemeUtils.getColorAccent(mView.getContext());
                    if (color != 0) {
                        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    }
                }
            } else {
                Drawable drawable = mThumbTypedValue.getDrawable();
                if (drawable != null) {
                    mView.setThumb(drawable);
                }
            }
        }
    }
}
