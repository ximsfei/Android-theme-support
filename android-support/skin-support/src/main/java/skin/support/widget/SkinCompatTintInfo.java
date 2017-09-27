package skin.support.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public class SkinCompatTintInfo {
    public ColorStateList mTintList;
    public PorterDuff.Mode mTintMode;
    public boolean mHasTintMode;
    public boolean mHasTintList;

    void clear() {
        mTintList = null;
        mHasTintList = false;
        mTintMode = null;
        mHasTintMode = false;
    }
}
