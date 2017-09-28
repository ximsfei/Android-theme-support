package skin.support.widget;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;

import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 2017/1/20.
 */

public class SkinCompatProgressBarHelper extends SkinCompatHelper {

    private static final int[] TINT_ATTRS;

    static {
        if (Build.VERSION.SDK_INT > 21) {
            TINT_ATTRS = new int[]{
                    android.R.attr.indeterminateDrawable,
                    android.R.attr.progressDrawable,
                    android.R.attr.indeterminateTint
            };
        } else {
            TINT_ATTRS = new int[]{
                    android.R.attr.indeterminateDrawable,
                    android.R.attr.progressDrawable
            };
        }
    }

    protected final ProgressBar mView;

    private Bitmap mSampleTile;
    protected SkinCompatTypedValue mIndeterminateDrawableTypedValue = new SkinCompatTypedValue();
    protected SkinCompatTypedValue mProgressDrawableTypedValue = new SkinCompatTypedValue();
    protected SkinCompatTypedValue mIndeterminateTintTypedValue = new SkinCompatTypedValue();

    SkinCompatProgressBarHelper(ProgressBar view) {
        mView = view;
    }

    void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        SkinCompatTypedArray
                .obtain(mView.getContext(), attrs, TINT_ATTRS, defStyleAttr)
                .getValue(0, mIndeterminateDrawableTypedValue)
                .getValue(1, mProgressDrawableTypedValue)
                .getValue(2, mIndeterminateTintTypedValue);
        applySkin();
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     */
    private Drawable tileify(Drawable drawable, boolean clip) {
        if (drawable instanceof DrawableWrapper) {
            Drawable inner = ((DrawableWrapper) drawable).getWrappedDrawable();
            if (inner != null) {
                inner = tileify(inner, clip);
                ((DrawableWrapper) drawable).setWrappedDrawable(inner);
            }
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable background = (LayerDrawable) drawable;
            final int N = background.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];

            for (int i = 0; i < N; i++) {
                int id = background.getId(i);
                outDrawables[i] = tileify(background.getDrawable(i),
                        (id == android.R.id.progress || id == android.R.id.secondaryProgress));
            }
            LayerDrawable newBg = new LayerDrawable(outDrawables);

            for (int i = 0; i < N; i++) {
                newBg.setId(i, background.getId(i));
            }

            return newBg;

        } else if (drawable instanceof BitmapDrawable) {
            final BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            final Bitmap tileBitmap = bitmapDrawable.getBitmap();
            if (mSampleTile == null) {
                mSampleTile = tileBitmap;
            }

            final ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());
            final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader(bitmapShader);
            shapeDrawable.getPaint().setColorFilter(bitmapDrawable.getPaint().getColorFilter());
            return (clip) ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
                    ClipDrawable.HORIZONTAL) : shapeDrawable;
        }

        return drawable;
    }

    /**
     * Convert a AnimationDrawable for use as a barberpole animation.
     * Each frame of the animation is wrapped in a ClipDrawable and
     * given a tiling BitmapShader.
     */
    private Drawable tileifyIndeterminate(Drawable drawable) {
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable background = (AnimationDrawable) drawable;
            final int N = background.getNumberOfFrames();
            AnimationDrawable newBg = new AnimationDrawable();
            newBg.setOneShot(background.isOneShot());

            for (int i = 0; i < N; i++) {
                Drawable frame = tileify(background.getFrame(i), true);
                frame.setLevel(10000);
                newBg.addFrame(frame, background.getDuration(i));
            }
            newBg.setLevel(10000);
            drawable = newBg;
        }
        return drawable;
    }

    private Shape getDrawableShape() {
        final float[] roundedCorners = new float[]{5, 5, 5, 5, 5, 5, 5, 5};
        return new RoundRectShape(roundedCorners, null, null);
    }

    Bitmap getSampleTime() {
        return mSampleTile;
    }

    protected void applyIndeterminateDrawableResource() {
        Drawable indeterminateDrawable = mIndeterminateDrawableTypedValue.getDrawable();
        if (indeterminateDrawable != null) {
            indeterminateDrawable.setBounds(mView.getIndeterminateDrawable().getBounds());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // FIXME: Any better way without setIndeterminateTintList?
                if (mIndeterminateDrawableTypedValue.isTypeNull() && mIndeterminateTintTypedValue.isTypeNull()) {
                    ColorStateList colorStateList = SkinCompatThemeUtils.getColorAccentList(mView.getContext());
                    if (colorStateList != null) {
                        mView.setIndeterminateTintList(colorStateList);
                    }
                } else {
                    mView.setIndeterminateDrawableTiled(indeterminateDrawable);
                }
            } else {
                mView.setIndeterminateDrawable(tileifyIndeterminate(indeterminateDrawable));
            }
        }
    }

    protected void applyProgressDrawableResource() {
        Drawable progressDrawable = mProgressDrawableTypedValue.getDrawable();
        if (progressDrawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mView.setProgressDrawableTiled(progressDrawable);
            } else {
                mView.setProgressDrawable(tileify(progressDrawable, false));
            }
        }
    }

    protected void applyIndeterminateTintResource() {
        if (Build.VERSION.SDK_INT > 21) {
            ColorStateList tintList = mIndeterminateTintTypedValue.getColorStateList();
            if (tintList != null) {
                mView.setIndeterminateTintList(tintList);
            }
        }
    }

    @Override
    public void applySkin() {
        applyIndeterminateDrawableResource();
        applyProgressDrawableResource();
        applyIndeterminateTintResource();
    }
}
