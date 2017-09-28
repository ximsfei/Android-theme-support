package skin.support.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import skin.support.R;
import skin.support.content.res.SkinCompatResources;
import skin.support.content.res.SkinCompatTypedArray;
import skin.support.content.res.SkinCompatTypedValue;

/**
 * Created by ximsfei on 2017/1/10.
 */

public class SkinCompatTextHelper extends SkinCompatHelper {
    private SkinCompatTypedValue mTextAppearanceTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mTextColorTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mTextColorHintTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mTextColorHighlightTypedValue = new SkinCompatTypedValue();

    private SkinCompatTypedValue mErrorMessageBackgroundTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mErrorMessageAboveBackgroundTypedValue = new SkinCompatTypedValue();

    private SkinCompatTypedValue mTextCursorDrawableTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mSelectHandleLeftTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mSelectHandleRightTypedValue = new SkinCompatTypedValue();
    private SkinCompatTypedValue mSelectHandleCenterTypedValue = new SkinCompatTypedValue();

    private Object mEditor;

    public static SkinCompatTextHelper create(SkinableTextView textView) {
        if (Build.VERSION.SDK_INT >= 17) {
            return new SkinCompatTextHelperV17(textView);
        }
        return new SkinCompatTextHelper(textView);
    }

    final SkinableTextView mView;

    SkinCompatTypedValue mDrawableBottomTypedValue = new SkinCompatTypedValue();
    SkinCompatTypedValue mDrawableLeftTypedValue = new SkinCompatTypedValue();
    SkinCompatTypedValue mDrawableRightTypedValue = new SkinCompatTypedValue();
    SkinCompatTypedValue mDrawableTopTypedValue = new SkinCompatTypedValue();

    public SkinCompatTextHelper(SkinableTextView view) {
        mView = view;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        final Context context = mView.getContext();

        SkinCompatTypedArray.obtain(context, attrs, R.styleable.SkinCompatTextHelper, defStyleAttr)
                .getValue(R.styleable.SkinCompatTextHelper_android_drawableLeft, mDrawableLeftTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_drawableRight, mDrawableRightTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_drawableTop, mDrawableTopTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_drawableBottom, mDrawableBottomTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_textCursorDrawable, mTextCursorDrawableTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_textSelectHandleLeft, mSelectHandleLeftTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_textSelectHandleRight, mSelectHandleRightTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_textSelectHandle, mSelectHandleCenterTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_textAppearance, mTextAppearanceTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_errorMessageBackground, mErrorMessageBackgroundTypedValue)
                .getValue(R.styleable.SkinCompatTextHelper_android_errorMessageAboveBackground, mErrorMessageAboveBackgroundTypedValue);


        SkinCompatTypedArray.obtain(context, attrs, R.styleable.SkinTextAppearance, defStyleAttr)
                .getValue(R.styleable.SkinTextAppearance_android_textColor, mTextColorTypedValue)
                .getValue(R.styleable.SkinTextAppearance_android_textColorHint, mTextColorHintTypedValue)
                .getValue(R.styleable.SkinTextAppearance_android_textColorHighlight, mTextColorHighlightTypedValue);

        applySkin();
    }

    public void onSetTextAppearance(Context context, int resId) {
        mTextAppearanceTypedValue.setData(resId);
        mTextColorTypedValue.reset();
        mTextColorHintTypedValue.reset();
        mTextColorHighlightTypedValue.reset();
        applyTextAppearanceResource();
    }

    public void onSetTextColor() {
        mTextAppearanceTypedValue.setValid(false);
        mTextColorTypedValue.setValid(false);
    }

    public void onSetError(CharSequence error) {
        if (error != null) {
            try {
                Object editor = getEditor();
                Class<?> clazz = editor.getClass();
                Field errorPopupField = clazz.getDeclaredField("mErrorPopup");
                errorPopupField.setAccessible(true);
                Object errorPopup = errorPopupField.get(editor);

                Field textViewField = errorPopup.getClass().getDeclaredField("mView");
                textViewField.setAccessible(true);
                TextView textView = (TextView) textViewField.get(errorPopup);

                Method isAboveAnchorMethod = PopupWindow.class.getDeclaredMethod("isAboveAnchor");
                isAboveAnchorMethod.setAccessible(true);
                boolean isAbove = (boolean) isAboveAnchorMethod.invoke(errorPopup);

                if (isAbove) {
                    ViewCompat.setBackground(textView, mErrorMessageAboveBackgroundTypedValue.getDrawable());
                } else {
                    ViewCompat.setBackground(textView, mErrorMessageBackgroundTypedValue.getDrawable());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void applyTextAppearanceResource() {
        if (mTextColorTypedValue.isTypeNull() || mTextColorHintTypedValue.isTypeNull()) {
            TypedArray a = mTextAppearanceTypedValue.obtainStyledAttributes(R.styleable.SkinTextAppearance);
            if (a == null) {
                return;
            }
            if (mTextColorTypedValue.isTypeNull() && a.hasValue(R.styleable.SkinTextAppearance_android_textColor)) {
                mView.setSkinTextColor(a.getColorStateList(R.styleable.SkinTextAppearance_android_textColor));
            }
            if (mTextColorHintTypedValue.isTypeNull() && a.hasValue(R.styleable.SkinTextAppearance_android_textColorHint)) {
                mView.setSkinHintTextColor(a.getColorStateList(R.styleable.SkinTextAppearance_android_textColorHint));
            }
            if (mTextColorHighlightTypedValue.isTypeNull() && a.hasValue(R.styleable.SkinTextAppearance_android_textColorHighlight)) {
                int highlightColor = a.getColor(R.styleable.SkinTextAppearance_android_textColorHighlight, 0);
                if (highlightColor != 0) {
                    mView.setSkinHighlightColor(highlightColor);
                }
            }
            a.recycle();
        }
    }

    private void applyTextColorHighlightResource() {
        int color = mTextColorHighlightTypedValue.getColor();
        if (color != 0) {
            mView.setSkinHighlightColor(color);
        }
    }

    private void applyTextColorHintResource() {
        ColorStateList colorStateList = mTextColorHintTypedValue.getColorStateList();
        if (colorStateList != null) {
            mView.setSkinHintTextColor(colorStateList);
        }
    }

    private void applyTextColorResource() {
        ColorStateList colorStateList = mTextColorTypedValue.getColorStateList();
        if (colorStateList != null) {
            mView.setSkinTextColor(colorStateList);
        }
    }

    public void applyTextCursorDrawableResource() {
        try {
            Object editor = getEditor();
            Class<?> clazz = editor.getClass();
            Field cursorDrawableField = clazz.getDeclaredField("mCursorDrawable");
            cursorDrawableField.setAccessible(true);

            Drawable[] drawables = new Drawable[2];
            drawables[0] = drawables[1] = mTextCursorDrawableTypedValue.getDrawable();
            cursorDrawableField.set(editor, drawables);

            Method updateCursorsPositionsMethod = clazz.getDeclaredMethod("updateCursorsPositions");
            updateCursorsPositionsMethod.setAccessible(true);
            updateCursorsPositionsMethod.invoke(editor);
        } catch (Exception ignored) {
        }
    }

    public void applyTextSelectHandleResource() {
        try {
            Object editor = getEditor();
            Class<?> clazz = editor.getClass();

            Field selectHandleLeftField = clazz.getDeclaredField("mSelectHandleLeft");
            selectHandleLeftField.setAccessible(true);
            Field selectHandleRightField = clazz.getDeclaredField("mSelectHandleRight");
            selectHandleRightField.setAccessible(true);
            Field selectHandleCenterField = clazz.getDeclaredField("mSelectHandleCenter");
            selectHandleCenterField.setAccessible(true);

            Drawable drawableLeft = mSelectHandleLeftTypedValue.getDrawable();
            selectHandleLeftField.set(editor, drawableLeft);
            Drawable drawableRight = mSelectHandleRightTypedValue.getDrawable();
            selectHandleRightField.set(editor, drawableRight);
            Drawable drawableCenter = mSelectHandleCenterTypedValue.getDrawable();
            selectHandleCenterField.set(editor, drawableCenter);


            applyTextSelectHandleResource(editor, "mSelectionModifierCursorController", "mStartHandle", drawableLeft, drawableRight);
            applyTextSelectHandleResource(editor, "mSelectionModifierCursorController", "mEndHandle", drawableRight, drawableLeft);
            applyTextSelectHandleResource(editor, "mInsertionPointCursorController", "mHandle", drawableCenter, drawableCenter);
        } catch (Exception ignored) {
        }
    }

    private void applyTextSelectHandleResource(Object editor, String controllerStr, String handleStr, Drawable drawableLeft, Drawable drawableRight) {
        try {
            Field controllerField = editor.getClass().getDeclaredField(controllerStr);
            controllerField.setAccessible(true);

            Object controller = controllerField.get(editor);
            Class<?> controllerClazz = controller.getClass();

            Field handleField = controllerClazz.getDeclaredField(handleStr);
            handleField.setAccessible(true);

            Object handle = handleField.get(controller);
            Field drawableLtr = handle.getClass().getSuperclass().getDeclaredField("mDrawableLtr");
            drawableLtr.setAccessible(true);
            drawableLtr.set(handle, drawableLeft);

            Field drawableRtl = handle.getClass().getSuperclass().getDeclaredField("mDrawableRtl");
            drawableRtl.setAccessible(true);
            drawableRtl.set(handle, drawableRight);

            Method updateDrawableMethod = handle.getClass().getSuperclass().getDeclaredMethod("updateDrawable");
            updateDrawableMethod.setAccessible(true);
            updateDrawableMethod.invoke(handle);
        } catch (Exception e) {
        }
    }

    private Object getEditor() throws Exception {
        if (mEditor == null) {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            mEditor = editorField.get(mView);
        }
        return mEditor;
    }

    public void onSetCompoundDrawablesRelativeWithIntrinsicBounds(
            @DrawableRes int start, @DrawableRes int top, @DrawableRes int end, @DrawableRes int bottom) {
        mDrawableLeftTypedValue.setData(start);
        mDrawableRightTypedValue.setData(end);
        mDrawableTopTypedValue.setData(top);
        mDrawableBottomTypedValue.setData(bottom);
        applyCompoundDrawablesRelativeResource();
    }

    public void onSetCompoundDrawablesWithIntrinsicBounds(
            @DrawableRes int left, @DrawableRes int top, @DrawableRes int right, @DrawableRes int bottom) {
        mDrawableLeftTypedValue.setData(left);
        mDrawableRightTypedValue.setData(right);
        mDrawableTopTypedValue.setData(top);
        mDrawableBottomTypedValue.setData(bottom);
        applyCompoundDrawablesResource();
    }

    protected void applyCompoundDrawablesRelativeResource() {
        applyCompoundDrawablesResource();
    }

    protected void applyCompoundDrawablesResource() {
        Drawable drawableLeft = mDrawableLeftTypedValue.getDrawable();
        Drawable drawableTop = mDrawableTopTypedValue.getDrawable();
        Drawable drawableRight = mDrawableRightTypedValue.getDrawable();
        Drawable drawableBottom = mDrawableBottomTypedValue.getDrawable();

        if (drawableLeft != null
                || drawableTop != null
                || drawableRight != null
                || drawableBottom != null) {
            mView.setSkinCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
        }
    }

    public SkinCompatTypedValue getTextColorTypedValue() {
        return mTextColorTypedValue;
    }

    public SkinCompatTypedValue getTextAppearanceTypedValue() {
        return mTextAppearanceTypedValue;
    }

    public void applySkin() {
        applyTextAppearanceResource();
        applyTextColorResource();
        applyTextColorHintResource();
        applyTextColorHighlightResource();
        applyTextCursorDrawableResource();
        applyTextSelectHandleResource();
        applyCompoundDrawablesResource();
    }
}
