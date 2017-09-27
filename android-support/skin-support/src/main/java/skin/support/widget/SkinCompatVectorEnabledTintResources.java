package skin.support.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import java.lang.ref.WeakReference;

public class SkinCompatVectorEnabledTintResources extends Resources {

    public static boolean shouldBeUsed() {
        return AppCompatDelegate.isCompatVectorFromResourcesEnabled()
                && Build.VERSION.SDK_INT <= MAX_SDK_WHERE_REQUIRED;
    }

    /**
     * The maximum API level where this class is needed.
     */
    public static final int MAX_SDK_WHERE_REQUIRED = 20;

    private final WeakReference<Context> mContextRef;

    public SkinCompatVectorEnabledTintResources(@NonNull final Context context,
                                      @NonNull final Resources res) {
        super(res.getAssets(), res.getDisplayMetrics(), res.getConfiguration());
        mContextRef = new WeakReference<>(context);
    }

    /**
     * We intercept this call so that we tint the result (if applicable). This is needed for
     * things like {@link android.graphics.drawable.DrawableContainer}s which can retrieve
     * their children via this method.
     */
    @Override
    public Drawable getDrawable(int id) throws NotFoundException {
        final Context context = mContextRef.get();
        if (context != null) {
            return SkinCompatDrawableManager.get().onDrawableLoadedFromResources(context, this, id);
        } else {
            return super.getDrawable(id);
        }
    }

    final Drawable superGetDrawable(int id) {
        return super.getDrawable(id);
    }
}
