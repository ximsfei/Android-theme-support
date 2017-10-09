package skin.support.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ActionBarContextView;
import android.util.AttributeSet;

import skin.support.R;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

public class SkinCompatActionBarContextView extends ActionBarContextView implements SkinCompatSupportable {
    private final SkinCompatTypedValue mBackgroundTypedValue = new SkinCompatTypedValue();
    public SkinCompatActionBarContextView(Context context) {
        this(context, null);
    }

    public SkinCompatActionBarContextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.actionModeStyle);
    }

    public SkinCompatActionBarContextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionMode, defStyle, 0);
//        ViewCompat.setBackground(this, a.getDrawable(R.styleable.ActionMode_background));
        SkinCompatTypedArray.obtain(context, attrs, R.styleable.ActionMode, defStyle)
                .getValue(R.styleable.ActionMode_background, mBackgroundTypedValue);
        applyActionModeBackground();
    }

    private void applyActionModeBackground() {
        Drawable drawable = mBackgroundTypedValue.getDrawable();
        if (drawable != null) {
            ViewCompat.setBackground(this, drawable);
        }
    }

    @Override
    public void applySkin() {
        applyActionModeBackground();
    }
}
