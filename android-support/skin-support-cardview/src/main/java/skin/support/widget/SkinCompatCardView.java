package skin.support.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import skin.support.cardview.R;
import skin.support.content.res.SkinCompatResources;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 2017/3/5.
 */

public class SkinCompatCardView extends CardView implements SkinCompatSupportable {
    private static final int[] COLOR_BACKGROUND_ATTR = {android.R.attr.colorBackground};

    private SkinCompatTypedValue mBackgroundColorTypedValue = new SkinCompatTypedValue();

    public SkinCompatCardView(Context context) {
        this(context, null);
    }

    public SkinCompatCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinCompatCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SkinCompatTypedArray
                .obtain(context, attrs, R.styleable.CardView, defStyleAttr)
                .getValue(R.styleable.CardView_cardBackgroundColor, mBackgroundColorTypedValue);

        applyBackgroundColorResource();
    }

    private void applyBackgroundColorResource() {
        ColorStateList backgroundColor = mBackgroundColorTypedValue.getColorStateList();
        if (backgroundColor == null) {
            final TypedArray a = SkinCompatResources.getInstance().obtainStyledAttributes(getContext(), COLOR_BACKGROUND_ATTR);
            int themeColorBackground = a.getColor(0, 0);
            if (themeColorBackground != 0) {
                final float[] hsv = new float[3];
                Color.colorToHSV(themeColorBackground, hsv);
                backgroundColor = ColorStateList.valueOf(hsv[2] > 0.5f
                        ? getResources().getColor(R.color.cardview_light_background)
                        : getResources().getColor(R.color.cardview_dark_background));
                setCardBackgroundColor(backgroundColor);
            }
            a.recycle();
        }
        if (backgroundColor != null) {
            setCardBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void applySkin() {
        applyBackgroundColorResource();
    }

}
