package skin.support.content.res;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.StyleRes;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;

import java.util.HashMap;

import static skin.support.content.res.SkinCompatTypedValue.TYPE_ATTR;
import static skin.support.content.res.SkinCompatTypedValue.TYPE_NULL;
import static skin.support.content.res.SkinCompatTypedValue.TYPE_RES;
import static skin.support.widget.SkinCompatHelper.INVALID_ID;

public class SkinCompatTypedArray {
    private Context context;
    private AttributeSet set;
    private int defStyleAttr = INVALID_ID;
    private int defStyleRes = INVALID_ID;
    private int[] attrs;

    private HashMap<Integer, Value> attrValueMap = new HashMap<>();

    private SkinCompatTypedArray(Context context, AttributeSet set,
                                 @StyleableRes int[] attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes,
                                 HashMap<Integer, Value> map) {
        this.context = context;
        this.set = set;
        this.defStyleAttr = defStyleAttr;
        this.defStyleRes = defStyleRes;
        this.attrs = attrs;
        this.attrValueMap.putAll(map);
    }

    public SkinCompatTypedArray getValue(int index, SkinCompatTypedValue outValue) {
        if (index >= 0 && index < attrs.length) {
            outValue.context = context;
            outValue.set = set;
            outValue.defStyleAttr = defStyleAttr;
            outValue.defStyleRes = defStyleRes;
            outValue.attrs = attrs;
            outValue.index = index;
            Value value = attrValueMap.get(attrs[index]);
            if (value != null) {
                outValue.type = value.type;
                outValue.data = value.data;
            }
        }
        return this;
    }

    public static SkinCompatTypedArray obtain(Context context, AttributeSet set,
                                              @StyleableRes int[] attrs, @AttrRes int defStyleAttr) {
        return obtain(context, set, attrs, defStyleAttr, 0);
    }

    public static SkinCompatTypedArray obtain(Context context, AttributeSet set,
                                              @StyleableRes int[] attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        int styleValue = set.getStyleAttribute();
        if (styleValue != INVALID_ID) {
            String type = context.getResources().getResourceTypeName(styleValue);
            if ("attr".equals(type)) {
                defStyleAttr = styleValue;
                defStyleRes = INVALID_ID;
            } else if ("style".equals(type)) {
                defStyleAttr = INVALID_ID;
                defStyleRes = styleValue;
            }
        }

        HashMap<Integer, Value> map = new HashMap<>();

        for (int index = 0; index < set.getAttributeCount(); index++) {
            int attrResource = set.getAttributeNameResource(index);
            if (attrResource != INVALID_ID) {
                Value value = new Value();
                String attrValue = set.getAttributeValue(index);
                if (attrValue.startsWith("?")) {
                    value.type = TYPE_ATTR;
                    value.data = Integer.valueOf(attrValue.substring(1));
                } else if (attrValue.startsWith("@")) {
                    value.type = TYPE_RES;
                    value.data = Integer.valueOf(attrValue.substring(1));
                }
                map.put(attrResource, value);
            }
        }

        return new SkinCompatTypedArray(context, set, attrs, defStyleAttr, defStyleRes, map);
    }

    public static SkinCompatTypedArray obtain(Context context, @StyleableRes int[] attrs, @AttrRes int defStyleAttr) {
        return new SkinCompatTypedArray(context, null, attrs, defStyleAttr, 0, new HashMap<Integer, Value>());
    }

    public static SkinCompatTypedArray obtain(Context context, @StyleableRes int[] attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        return new SkinCompatTypedArray(context, null, attrs, defStyleAttr, defStyleRes, new HashMap<Integer, Value>());
    }

    private static class Value {
        int type = TYPE_NULL;
        int data = INVALID_ID;
    }
}
