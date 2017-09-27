package skin.support.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.AttributeSet;

import java.lang.reflect.Field;

import skin.support.R;
import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

public class SkinCompatListMenuItemView extends ListMenuItemView implements SkinCompatSupportable {
    private int mIconResId = INVALID_ID;

    public SkinCompatListMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinCompatListMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize(MenuItemImpl itemData, int menuType) {
        readIconResId(itemData);
        super.initialize(itemData, menuType);
        writeIconResId(itemData);
        applyIconResource();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        applyBackgroundResource();
    }

    public void readIconResId(MenuItemImpl itemData) {
        try {
            if (showsIcon() || itemData.shouldShowIcon()) {
                Field field = MenuItemImpl.class.getDeclaredField("mIconResId");
                field.setAccessible(true);
                mIconResId = (int) field.get(itemData);
            }
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

    private void applyBackgroundResource() {
        TypedArray a = SkinCompatResources.getInstance().obtainStyledAttributes(getContext(), null,
                R.styleable.PopupWindow, R.attr.actionOverflowMenuStyle, 0);
        Drawable drawable = a.getDrawable(R.styleable.PopupWindow_android_popupBackground);
        if (drawable != null) {
            ViewCompat.setBackground(this, a.getDrawable(R.styleable.PopupWindow_android_popupBackground));
        }
        a.recycle();
    }

    @Override
    public void applySkin() {
        applyIconResource();
        applyIconResource();
    }
}