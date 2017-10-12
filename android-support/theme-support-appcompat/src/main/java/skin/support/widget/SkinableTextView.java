package skin.support.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

public interface SkinableTextView {
    Context getContext();
    void setSkinTextColor(int textColor);
    void setSkinTextColor(ColorStateList textColor);
    void setSkinHintTextColor(ColorStateList hintColor);
    void setSkinHighlightColor(int highlightColor);
    void setSkinCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom);
    void setSkinCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start, Drawable top, Drawable end, Drawable bottom);
}
