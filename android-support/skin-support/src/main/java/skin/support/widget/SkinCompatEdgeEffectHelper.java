package skin.support.widget;

import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EdgeEffect;

import java.lang.reflect.Field;

import skin.support.R;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

import static android.view.View.OVER_SCROLL_NEVER;

public class SkinCompatEdgeEffectHelper extends SkinCompatHelper {
    private final View mView;
    private final Class<?> mContainer;
    private EdgeEffect mEdgeGlowTop;
    private EdgeEffect mEdgeGlowBottom;
    private SkinCompatTypedValue mColorEdgeEffectTypedValue = new SkinCompatTypedValue();

    public SkinCompatEdgeEffectHelper(View view, Class<?> container) {
        mView = view;
        mContainer = container;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        SkinCompatTypedArray
                .obtain(mView.getContext(), attrs, R.styleable.SkinCompatEdgeEffect, defStyleAttr)
                .getValue(R.styleable.SkinCompatEdgeEffect_android_colorEdgeEffect, mColorEdgeEffectTypedValue);
        loadEdgeEffect();
        applySkin();
    }

    public void onSetOverScrollMode(int mode) {
        if (mode != OVER_SCROLL_NEVER) {
            if (mEdgeGlowTop == null) {
                loadEdgeEffect();
            }
        } else {
            mEdgeGlowTop = null;
            mEdgeGlowBottom = null;
        }
    }

    private void loadEdgeEffect() {
        try {
            Field edgeGlowTop = mContainer.getDeclaredField("mEdgeGlowTop");
            edgeGlowTop.setAccessible(true);
            mEdgeGlowTop = (EdgeEffect) edgeGlowTop.get(mView);
            Field edgeGlowBottom = mContainer.getDeclaredField("mEdgeGlowBottom");
            edgeGlowBottom.setAccessible(true);
            mEdgeGlowBottom = (EdgeEffect) edgeGlowBottom.get(mView);
        } catch (Exception e) {
        }
    }

    @Override
    public void applySkin() {
        if (mEdgeGlowTop == null && mEdgeGlowBottom == null) {
            return;
        }
        int color = mColorEdgeEffectTypedValue.getColor();
        if (color != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mEdgeGlowTop != null) {
                    mEdgeGlowTop.setColor(color);
                }
                if (mEdgeGlowBottom != null) {
                    mEdgeGlowBottom.setColor(color);
                }
            } else {
                try {
                    Field paintField = EdgeEffect.class.getDeclaredField("mPaint");
                    paintField.setAccessible(true);
                    Paint topPaint = (Paint) paintField.get(mEdgeGlowTop);
                    Paint bottomPaint = (Paint) paintField.get(mEdgeGlowBottom);
                    topPaint.setColor(color);
                    bottomPaint.setColor(color);
                } catch (Exception e) {
                }
            }
        }
    }
}
