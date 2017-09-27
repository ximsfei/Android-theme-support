package skin.support.content.res;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatDrawableManager;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

public class SkinCompatTypedValue {
    public static final int TYPE_NULL = 0;
    public static final int TYPE_ATTR = 1;
    public static final int TYPE_RES = 2;
    protected Context context;
    protected AttributeSet set;
    protected int defStyleAttr = INVALID_ID;
    protected int defStyleRes = INVALID_ID;
    protected int[] attrs;
    protected int index;
    protected int type = TYPE_NULL;
    protected int data = INVALID_ID;
    protected boolean valid = true;

    public int getType() {
        return type;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
        this.type = TYPE_RES;
        this.valid = true;
    }

    public void setData(int type, int data) {
        this.type = type;
        this.data = data;
        this.valid = true;
    }

    public boolean isTypeNull() {
        return type == TYPE_NULL;
    }

    public boolean isTypeRes() {
        return type == TYPE_RES;
    }

    public boolean isTypeAttr() {
        return type == TYPE_ATTR;
    }

    public boolean isDataInvalid() {
        return data == INVALID_ID;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void reset() {
        type = TYPE_NULL;
        data = INVALID_ID;
    }

    public int getColor() {
        int color = 0;
        if (!isValid()) {
            return color;
        }
        if (isTypeNull()) {
            TypedArray a;
            if (defStyleAttr != INVALID_ID || defStyleRes != INVALID_ID) {
                a = SkinCompatResources.getInstance().obtainStyledAttributes(context, set, attrs, defStyleAttr, defStyleRes);
            } else {
                a = SkinCompatResources.getInstance().obtainStyledAttributes(context, attrs);
            }
            color = a.getColor(index, 0);
            a.recycle();
        } else if (isTypeAttr()) {
            if (data != INVALID_ID) {
                TypedArray a = SkinCompatResources.getInstance().obtainStyledAttributes(context, new int[]{data});
                color = a.getColor(0, 0);
                a.recycle();
            }
        } else if (isTypeRes()) {
            if (data != INVALID_ID) {
                color = SkinCompatResources.getInstance().getColor(data);
            }
        }
        return color;
    }

    public ColorStateList getColorStateList() {
        ColorStateList colorStateList = null;
        if (!isValid()) {
            return colorStateList;
        }
        if (isTypeNull()) {
            TypedArray a;
            if (defStyleAttr != INVALID_ID || defStyleRes != INVALID_ID) {
                a = SkinCompatResources.getInstance().obtainStyledAttributes(context, set, attrs, defStyleAttr, defStyleRes);
            } else {
                a = SkinCompatResources.getInstance().obtainStyledAttributes(context, attrs);
            }
            colorStateList = a.getColorStateList(index);
            a.recycle();
        } else if (isTypeAttr()) {
            if (data != INVALID_ID) {
                TypedArray a = SkinCompatResources.getInstance().obtainStyledAttributes(context, new int[]{data});
                colorStateList = a.getColorStateList(0);
                a.recycle();
            }
        } else if (isTypeRes()) {
            if (data != INVALID_ID) {
                colorStateList = SkinCompatResources.getInstance().getColorStateList(data);
            }
        }
        return colorStateList;
    }

    public Drawable getDrawable() {
        Drawable drawable = null;
        if (!isValid()) {
            return drawable;
        }
        if (isTypeNull()) {
            TypedArray a;
            if (defStyleAttr != INVALID_ID || defStyleRes != INVALID_ID) {
                a = SkinCompatResources.getInstance().obtainStyledAttributes(context, set, attrs, defStyleAttr, defStyleRes);
            } else {
                a = SkinCompatResources.getInstance().obtainStyledAttributes(context, attrs);
            }
            drawable = a.getDrawable(index);
            a.recycle();
        } else if (isTypeAttr()) {
            if (data != INVALID_ID) {
                TypedArray a = SkinCompatResources.getInstance().obtainStyledAttributes(context, new int[]{data});
                drawable = a.getDrawable(0);
                a.recycle();
            }
        } else if (isTypeRes()) {
            if (data != INVALID_ID) {
                drawable = SkinCompatResources.getInstance().getDrawable(data);
            }
        }
        return drawable;
    }

    public ColorStateList getTintList() {
        ColorStateList tint = null;
        if (!isValid()) {
            return null;
        }
        if (isTypeNull()) {
            TypedArray a;
            if (defStyleAttr != INVALID_ID || defStyleRes != INVALID_ID) {
                a = context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
            } else {
                a = context.obtainStyledAttributes(attrs);
            }
            if (a.hasValue(index)) {
                int resourceId = a.getResourceId(index, INVALID_ID);
                tint = SkinCompatDrawableManager.get().getTintList(context, resourceId);
            }
            a.recycle();
        }
        return tint;
    }

    public TypedArray obtainStyledAttributes(int[] as) {
        return obtainStyledAttributes(as, INVALID_ID);
    }

    public TypedArray obtainStyledAttributes(int[] as, int defRes) {
        if (!isValid()) {
            return null;
        }
        int resourceId;
        if (type == TYPE_NULL) {
            TypedArray a = SkinCompatResources.getInstance().obtainStyledAttributes(context, set, attrs, defStyleAttr, defStyleRes);
            resourceId = a.getResourceId(this.index, INVALID_ID);
            a.recycle();
            if (resourceId == INVALID_ID) {
                return SkinCompatResources.getInstance().obtainStyledAttributes(context, defRes, as);
            } else {
                return SkinCompatResources.getInstance().obtainStyledAttributes(context, resourceId, false, as, true);
            }
        } else if (data != INVALID_ID) {
            if (type == TYPE_ATTR) {
                TypedArray a = SkinCompatResources.getInstance().obtainStyledAttributes(context, new int[]{data});
                resourceId = a.getResourceId(0, INVALID_ID);
                a.recycle();
                if (resourceId == INVALID_ID) {
                    return SkinCompatResources.getInstance().obtainStyledAttributes(context, defRes, as);
                } else {
                    return SkinCompatResources.getInstance().obtainStyledAttributes(context, resourceId, false, as, true);
                }
            } else if (type == TYPE_RES) {
                resourceId = data;
                return SkinCompatResources.getInstance().obtainStyledAttributes(context, resourceId, as);
            }
        }
        return SkinCompatResources.getInstance().obtainStyledAttributes(context, defRes, as);
    }

    @Override
    public String toString() {
        return "SkinCompatTypedValue type = " + type + ", data = " + data
                + ", defStyleAttr = " + defStyleAttr + ", defStyleRes = " + defStyleRes;
    }
}