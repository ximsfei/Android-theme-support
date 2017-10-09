package skin.support.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SkinCompatContextWrapper extends ContextWrapper {
    private static final Object CACHE_LOCK = new Object();
    private static ArrayList<WeakReference<SkinCompatContextWrapper>> sCache;
    private int mThemeResId = 0;

    public static Context wrap(@NonNull final Context context) {
        if (shouldWrap(context)) {
            synchronized (CACHE_LOCK) {
                if (sCache == null) {
                    sCache = new ArrayList<>();
                } else {
                    // This is a convenient place to prune any dead reference entries
                    for (int i = sCache.size() - 1; i >= 0; i--) {
                        final WeakReference<SkinCompatContextWrapper> ref = sCache.get(i);
                        if (ref == null || ref.get() == null) {
                            sCache.remove(i);
                        }
                    }
                    // Now check our instance cache
                    for (int i = sCache.size() - 1; i >= 0; i--) {
                        final WeakReference<SkinCompatContextWrapper> ref = sCache.get(i);
                        final SkinCompatContextWrapper wrapper = ref != null ? ref.get() : null;
                        if (wrapper != null && wrapper.getBaseContext() == context) {
                            return wrapper;
                        }
                    }
                }
                // If we reach here then the cache didn't have a hit, so create a new instance
                // and add it to the cache
                final SkinCompatContextWrapper wrapper = new SkinCompatContextWrapper(context);
                try {
                    Method getThemeResId =ContextWrapper.class.getDeclaredMethod("getThemeResId");
                    getThemeResId.setAccessible(true);
                    int themeId = (int) getThemeResId.invoke(context);
                    wrapper.setTheme(themeId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sCache.add(new WeakReference<>(wrapper));
                return wrapper;
            }
        }
        return context;
    }

    private static boolean shouldWrap(@NonNull final Context context) {
        if (context instanceof SkinCompatContextWrapper
                || context.getResources() instanceof SkinResources
                || context.getResources() instanceof SkinCompatVectorEnabledTintResources) {
            // If the Context already has a SkinResources[Experimental] impl, no need to wrap again
            // If the Context is already a TintContextWrapper, no need to wrap again
            return false;
        }
        return Build.VERSION.SDK_INT < 21 || SkinCompatVectorEnabledTintResources.shouldBeUsed();
    }

    private final Resources mResources;
    private final Resources.Theme mTheme;

    private SkinCompatContextWrapper(@NonNull final Context base) {
        super(base);

        if (SkinCompatVectorEnabledTintResources.shouldBeUsed()) {
            // We need to create a copy of the Theme so that the Theme references our
            // new Resources instance
            mResources = new SkinCompatVectorEnabledTintResources(this, base.getResources());
            mTheme = mResources.newTheme();
            mTheme.setTo(base.getTheme());
        } else {
            mResources = new SkinResources(this, base.getResources());
            mTheme = null;
        }
    }

    public int getThemeResId() {
        return mThemeResId;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    @Override
    public void setTheme(int resid) {
        mThemeResId = resid;
        if (mTheme == null) {
            super.setTheme(resid);
        } else {
            mTheme.applyStyle(resid, true);
        }
    }

    @Override
    public Resources getResources() {
        return mResources;
    }

    @Override
    public AssetManager getAssets() {
        // Ensure we're returning assets with the correct configuration.
        return mResources.getAssets();
    }


    class SkinResources extends ResourcesWrapper {

        private final WeakReference<Context> mContextRef;

        public SkinResources(@NonNull Context context, @NonNull final Resources res) {
            super(res);
            mContextRef = new WeakReference<>(context);
        }

        /**
         * We intercept this call so that we tint the result (if applicable). This is needed for
         * things like {@link android.graphics.drawable.DrawableContainer}s which can retrieve
         * their children via this method.
         */
        @Override
        public Drawable getDrawable(int id) throws Resources.NotFoundException {
            Drawable d = super.getDrawable(id);
            Context context = mContextRef.get();
            if (d != null && context != null) {
                SkinCompatDrawableManager.get().tintDrawableUsingColorFilter(context, id, d);
            }
            return d;
        }
    }

    class ResourcesWrapper extends Resources {

        private final Resources mResources;

        public ResourcesWrapper(Resources resources) {
            super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
            mResources = resources;
        }

        @Override
        public CharSequence getText(int id) throws NotFoundException {
            return mResources.getText(id);
        }

        @Override
        public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
            return mResources.getQuantityText(id, quantity);
        }

        @Override
        public String getString(int id) throws NotFoundException {
            return mResources.getString(id);
        }

        @Override
        public String getString(int id, Object... formatArgs) throws NotFoundException {
            return mResources.getString(id, formatArgs);
        }

        @Override
        public String getQuantityString(int id, int quantity, Object... formatArgs)
                throws NotFoundException {
            return mResources.getQuantityString(id, quantity, formatArgs);
        }

        @Override
        public String getQuantityString(int id, int quantity) throws NotFoundException {
            return mResources.getQuantityString(id, quantity);
        }

        @Override
        public CharSequence getText(int id, CharSequence def) {
            return mResources.getText(id, def);
        }

        @Override
        public CharSequence[] getTextArray(int id) throws NotFoundException {
            return mResources.getTextArray(id);
        }

        @Override
        public String[] getStringArray(int id) throws NotFoundException {
            return mResources.getStringArray(id);
        }

        @Override
        public int[] getIntArray(int id) throws NotFoundException {
            return mResources.getIntArray(id);
        }

        @Override
        public TypedArray obtainTypedArray(int id) throws NotFoundException {
            return mResources.obtainTypedArray(id);
        }

        @Override
        public float getDimension(int id) throws NotFoundException {
            return mResources.getDimension(id);
        }

        @Override
        public int getDimensionPixelOffset(int id) throws NotFoundException {
            return mResources.getDimensionPixelOffset(id);
        }

        @Override
        public int getDimensionPixelSize(int id) throws NotFoundException {
            return mResources.getDimensionPixelSize(id);
        }

        @Override
        public float getFraction(int id, int base, int pbase) {
            return mResources.getFraction(id, base, pbase);
        }

        @Override
        public Drawable getDrawable(int id) throws NotFoundException {
            return mResources.getDrawable(id);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Drawable getDrawable(int id, Theme theme) throws NotFoundException {
            return mResources.getDrawable(id, theme);
        }

        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        @Override
        public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
            return mResources.getDrawableForDensity(id, density);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Drawable getDrawableForDensity(int id, int density, Theme theme) {
            return mResources.getDrawableForDensity(id, density, theme);
        }

        @Override
        public Movie getMovie(int id) throws NotFoundException {
            return mResources.getMovie(id);
        }

        @Override
        public int getColor(int id) throws NotFoundException {
            return mResources.getColor(id);
        }

        @Override
        public ColorStateList getColorStateList(int id) throws NotFoundException {
            return mResources.getColorStateList(id);
        }

        @Override
        public boolean getBoolean(int id) throws NotFoundException {
            return mResources.getBoolean(id);
        }

        @Override
        public int getInteger(int id) throws NotFoundException {
            return mResources.getInteger(id);
        }

        @Override
        public XmlResourceParser getLayout(int id) throws NotFoundException {
            return mResources.getLayout(id);
        }

        @Override
        public XmlResourceParser getAnimation(int id) throws NotFoundException {
            return mResources.getAnimation(id);
        }

        @Override
        public XmlResourceParser getXml(int id) throws NotFoundException {
            return mResources.getXml(id);
        }

        @Override
        public InputStream openRawResource(int id) throws NotFoundException {
            return mResources.openRawResource(id);
        }

        @Override
        public InputStream openRawResource(int id, TypedValue value) throws NotFoundException {
            return mResources.openRawResource(id, value);
        }

        @Override
        public AssetFileDescriptor openRawResourceFd(int id) throws NotFoundException {
            return mResources.openRawResourceFd(id);
        }

        @Override
        public void getValue(int id, TypedValue outValue, boolean resolveRefs)
                throws NotFoundException {
            mResources.getValue(id, outValue, resolveRefs);
        }

        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        @Override
        public void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs)
                throws NotFoundException {
            mResources.getValueForDensity(id, density, outValue, resolveRefs);
        }

        @Override
        public void getValue(String name, TypedValue outValue, boolean resolveRefs)
                throws NotFoundException {
            mResources.getValue(name, outValue, resolveRefs);
        }

        @Override
        public TypedArray obtainAttributes(AttributeSet set, int[] attrs) {
            return mResources.obtainAttributes(set, attrs);
        }

        @Override
        public void updateConfiguration(Configuration config, DisplayMetrics metrics) {
            super.updateConfiguration(config, metrics);
            if (mResources != null) { // called from super's constructor. So, need to check.
                mResources.updateConfiguration(config, metrics);
            }
        }

        @Override
        public DisplayMetrics getDisplayMetrics() {
            return mResources.getDisplayMetrics();
        }

        @Override
        public Configuration getConfiguration() {
            return mResources.getConfiguration();
        }

        @Override
        public int getIdentifier(String name, String defType, String defPackage) {
            return mResources.getIdentifier(name, defType, defPackage);
        }

        @Override
        public String getResourceName(int resid) throws NotFoundException {
            return mResources.getResourceName(resid);
        }

        @Override
        public String getResourcePackageName(int resid) throws NotFoundException {
            return mResources.getResourcePackageName(resid);
        }

        @Override
        public String getResourceTypeName(int resid) throws NotFoundException {
            return mResources.getResourceTypeName(resid);
        }

        @Override
        public String getResourceEntryName(int resid) throws NotFoundException {
            return mResources.getResourceEntryName(resid);
        }

        @Override
        public void parseBundleExtras(XmlResourceParser parser, Bundle outBundle)
                throws XmlPullParserException, IOException {
            mResources.parseBundleExtras(parser, outBundle);
        }

        @Override
        public void parseBundleExtra(String tagName, AttributeSet attrs, Bundle outBundle)
                throws XmlPullParserException {
            mResources.parseBundleExtra(tagName, attrs, outBundle);
        }
    }
}
