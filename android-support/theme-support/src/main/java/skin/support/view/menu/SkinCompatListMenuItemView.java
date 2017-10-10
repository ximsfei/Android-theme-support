package skin.support.view.menu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.AttributeSet;

import java.lang.reflect.Field;

import skin.support.R;
import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatDrawableManager;
import skin.support.widget.SkinCompatThemeUtils;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

public class SkinCompatListMenuItemView extends ListMenuItemView {
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
            Drawable icon = SkinCompatDrawableManager.get().getDrawable(getContext(), mIconResId);
            if (icon != null) {
                setIcon(icon);
            }
        }
    }

    private void applyBackgroundResource() {
        int colorBackground = SkinCompatThemeUtils.getThemeAttrColor(getContext(), android.R.attr.colorBackground);
        TypedArray a = SkinCompatResources.getInstance()
                .obtainStyledAttributes(getContext(), new int[]{R.attr.selectableItemBackground});

        Drawable drawable = a.getDrawable(0);
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof RippleDrawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ColorDrawable colorDrawable = new ColorDrawable(colorBackground);
                    ((RippleDrawable) drawable).addLayer(colorDrawable);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ColorDrawable colorDrawable = new ColorDrawable(colorBackground);
                    ColorStateList rippleColorStateList = getResources().getColorStateList(android.R.color.darker_gray);
                    if (rippleColorStateList != null) {
                        drawable = new RippleDrawable(rippleColorStateList, colorDrawable, null);
                    }
                }
            } else if (drawable instanceof StateListDrawable && drawable.getCurrent() instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) drawable.getCurrent();
                colorDrawable.mutate();
                colorDrawable.setColor(colorBackground);
            }
            ViewCompat.setBackground(this, drawable);
        }
        a.recycle();
    }
}