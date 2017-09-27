package skin.support.view.menu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.AttributeSet;

import java.lang.reflect.Field;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatSupportable;
import skin.support.widget.SkinCompatTextHelper;
import skin.support.widget.SkinableTextView;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

public class SkinCompatActionMenuItemView extends ActionMenuItemView implements SkinCompatSupportable, SkinableTextView {
    private final SkinCompatTextHelper mTextHelper;
    private int mIconResId = INVALID_ID;

    public SkinCompatActionMenuItemView(Context context) {
        this(context, null);
    }

    public SkinCompatActionMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinCompatActionMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextHelper = SkinCompatTextHelper.create(this);
        mTextHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @Override
    public void setTextColor(@ColorInt int color) {
        super.setTextColor(color);
        if (mTextHelper != null) {
            mTextHelper.onSetTextColor();
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        if (mTextHelper != null) {
            mTextHelper.onSetTextColor();
        }
    }

    @Override
    public void setSkinTextColor(int textColor) {
        super.setTextColor(textColor);
    }

    @Override
    public void setSkinTextColor(ColorStateList textColor) {
        super.setTextColor(textColor);
    }

    @Override
    public void setSkinHintTextColor(ColorStateList hintColor) {
        super.setHintTextColor(hintColor);
    }

    @Override
    public void setSkinHighlightColor(int highlightColor) {
        super.setHighlightColor(highlightColor);
    }

    @Override
    public void setSkinCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void setSkinCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
    }

    @Override
    public void initialize(MenuItemImpl itemData, int menuType) {
        readIconResId(itemData);
        super.initialize(itemData, menuType);
        writeIconResId(itemData);
        applyIconResource();
    }

    public void readIconResId(MenuItemImpl itemData) {
        try {
            Field field = MenuItemImpl.class.getDeclaredField("mIconResId");
            field.setAccessible(true);
            mIconResId = (int) field.get(itemData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeIconResId(MenuItemImpl itemData) {
        if (mIconResId != INVALID_ID) {
            try {
                Field field = MenuItemImpl.class.getDeclaredField("mIconResId");
                field.setAccessible(true);
                field.set(itemData, mIconResId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void applyIconResource() {
        if (mIconResId != INVALID_ID && getItemData() != null) {
            Drawable icon = SkinCompatResources.getInstance().getDrawable(mIconResId);
            if (icon != null) {
                setIcon(icon);
            }
        }
    }

    @Override
    public void applySkin() {
        if (mTextHelper != null) {
            mTextHelper.applySkin();
        }
        applyIconResource();
    }
}