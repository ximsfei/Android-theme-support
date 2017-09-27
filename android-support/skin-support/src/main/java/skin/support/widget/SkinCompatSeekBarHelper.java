package skin.support.widget;

import android.graphics.drawable.Drawable;
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
        Drawable drawable = mThumbTypedValue.getDrawable();
        if (drawable != null) {
            mView.setThumb(drawable);
        }
    }
}
