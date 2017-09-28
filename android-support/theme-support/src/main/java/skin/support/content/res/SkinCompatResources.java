package skin.support.content.res;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.StyleRes;
import android.support.annotation.StyleableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.Map;
import java.util.WeakHashMap;

import skin.support.SkinCompatManager;

public class SkinCompatResources {
    private static volatile SkinCompatResources sInstance;
    private final Context mAppContext;
    private Resources mResources;
    private String mSkinPkgName;
    private String mSkinName;
    private SkinCompatManager.SkinLoaderStrategy mStrategy;
    private boolean isDefaultSkin;
    private Map<Context, SkinCompatTheme> mThemeCache = new WeakHashMap<>();

    private SkinCompatResources(Context context) {
        mAppContext = context.getApplicationContext();
        reset();
    }

    public static void init(Context context) {
        if (sInstance == null) {
            synchronized (SkinCompatResources.class) {
                if (sInstance == null) {
                    sInstance = new SkinCompatResources(context);
                }
            }
        }
    }

    public static SkinCompatResources getInstance() {
        return sInstance;
    }

    public void reset() {
        mResources = mAppContext.getResources();
        mSkinPkgName = mAppContext.getPackageName();
        mSkinName = "";
        mStrategy = null;
        isDefaultSkin = true;
        resetThemeMap();
    }

    @Deprecated
    public void setSkinResource(Resources resources, String pkgName) {
        mResources = resources;
        mSkinPkgName = pkgName;
        mSkinName = "";
        mStrategy = null;
        isDefaultSkin = mAppContext.getPackageName().equals(pkgName);
        resetThemeMap();
    }

    public void setupSkin(Resources resources, String pkgName, String skinName, SkinCompatManager.SkinLoaderStrategy strategy) {
        mResources = resources;
        mSkinPkgName = pkgName;
        mSkinName = skinName;
        mStrategy = strategy;
        isDefaultSkin = TextUtils.isEmpty(skinName);
        resetThemeMap();
    }

    private void resetThemeMap() {
        for (SkinCompatTheme theme : mThemeCache.values()) {
            theme.reset(mResources);
        }
    }

    public Resources getSkinResources() {
        return mResources;
    }

    public String getSkinPkgName() {
        return mSkinPkgName;
    }

    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }

    public int getColor(int resId) {
        int originColor = ContextCompat.getColor(mAppContext, resId);
        if (isDefaultSkin) {
            return originColor;
        }

        int targetResId = getTargetResId(resId, "color");
        return targetResId == 0 ? originColor : mResources.getColor(targetResId);
    }

    public Drawable getDrawable(int resId) {
        Drawable originDrawable = ContextCompat.getDrawable(mAppContext, resId);
        if (isDefaultSkin) {
            return originDrawable;
        }

        String resType = mAppContext.getResources().getResourceTypeName(resId);
        int targetResId = getTargetResId(resId, resType);
        return targetResId == 0 ? originDrawable : mResources.getDrawable(targetResId);
    }

    public Drawable getMipmap(int resId) {
        Drawable originDrawable = ContextCompat.getDrawable(mAppContext, resId);
        if (isDefaultSkin) {
            return originDrawable;
        }

        int targetResId = getTargetResId(resId, "mipmap");
        return targetResId == 0 ? originDrawable : mResources.getDrawable(targetResId);
    }

    public ColorStateList getColorStateList(int resId) {
        ColorStateList colorStateList = ContextCompat.getColorStateList(mAppContext, resId);
        if (isDefaultSkin) {
            return colorStateList;
        }

        String resType = mAppContext.getResources().getResourceTypeName(resId);
        int targetResId = getTargetResId(resId, resType);
        return targetResId == 0 ? colorStateList : mResources.getColorStateList(targetResId);
    }

    private int getTargetResId(int resId, String type) {
        try {
            String pkgName = mAppContext.getResources().getResourcePackageName(resId);
            if ("android".equals(pkgName)) {
                return 0;
            }
            String resName = null;
            if (mStrategy != null) {
                resName = mStrategy.getTargetResourceEntryName(mAppContext, mSkinName, resId);
            }
            if (TextUtils.isEmpty(resName)) {
                resName = mAppContext.getResources().getResourceEntryName(resId);
            }
            return mResources.getIdentifier(resName, type, mSkinPkgName);
        } catch (Exception e) {
            // 换肤失败不至于应用崩溃.
            return 0;
        }
    }

    public SkinCompatTheme newCompatTheme(Context context) {
        SkinCompatTheme theme = mThemeCache.get(context);
        if (theme == null) {
            theme = new SkinCompatTheme(context);
            mThemeCache.put(context, theme);
        }
        return theme;
    }

    public TypedArray obtainStyledAttributes(Context context, AttributeSet set,
                                             @StyleableRes int[] attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        return newCompatTheme(context).obtainStyledAttributes(set, attrs, true, defStyleAttr, true, defStyleRes, true);
    }

    public TypedArray obtainStyledAttributes(Context context, AttributeSet set,
                                             @StyleableRes int[] attrs, boolean isHostAttrs, @AttrRes int defStyleAttr, boolean isHostAttr, @StyleRes int defStyleRes, boolean isHostRes) {
        return newCompatTheme(context).obtainStyledAttributes(set, attrs, isHostAttrs, defStyleAttr, isHostAttr, defStyleRes, isHostRes);
    }

    public TypedArray obtainStyledAttributes(Context context, @StyleRes int resId, @StyleableRes int[] attrs)
            throws Resources.NotFoundException {
        return newCompatTheme(context).obtainStyledAttributes(resId, true, attrs, true);
    }

    public TypedArray obtainStyledAttributes(Context context, @StyleRes int resId, boolean isHostRes, @StyleableRes int[] attrs, boolean isHostAttrs)
            throws Resources.NotFoundException {
        return newCompatTheme(context).obtainStyledAttributes(resId, isHostRes, attrs, isHostAttrs);
    }

    public TypedArray obtainStyledAttributes(Context context, @StyleableRes int[] attrs)
            throws Resources.NotFoundException {
        return newCompatTheme(context).obtainStyledAttributes(attrs);
    }

    public final class SkinCompatTheme {
        private Context mContext;
        private int mThemeResId;
        private Resources.Theme mTheme;

        SkinCompatTheme(Context context) {
            mContext = context;
            if (context instanceof Activity) {
                try {
                    ActivityInfo info = context.getPackageManager().getActivityInfo(
                            new ComponentName(context.getPackageName(), context.getClass().getName()), 0);
                    if (info != null && info.theme != 0) {
                        mThemeResId = info.theme;
                    }
                } catch (Exception e) {
                }
            }
            if (mThemeResId == 0) {
                mThemeResId = context.getApplicationInfo().theme;
            }
            reset(mResources);
        }

        public void reset(Resources resources) {
            int themeId = getTargetResId(mThemeResId, "style");
            if (themeId != 0) {
                mTheme = resources.newTheme();
                mTheme.applyStyle(themeId, true);
            } else {
                mTheme = null;
            }
        }

        public TypedArray obtainStyledAttributes(AttributeSet set,
                                                 @StyleableRes int[] attrs, boolean isHostAttrs,
                                                 @AttrRes int defStyleAttr, boolean isHostAttr,
                                                 @StyleRes int defStyleRes, boolean isHostRes) {
            if (isDefaultSkin || mTheme == null) {
                return mContext.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
            } else {
                return mTheme.obtainStyledAttributes(set, fixResArray(attrs, isHostAttrs, "attr"),
                        fixRes(defStyleAttr, isHostAttr, "attr"), fixRes(defStyleRes, isHostRes, "style"));
            }
        }

        public TypedArray obtainStyledAttributes(@StyleRes int resId, boolean isHostRes, @StyleableRes int[] attrs, boolean isHostAttrs)
                throws Resources.NotFoundException {
            if (isDefaultSkin || mTheme == null) {
                return mContext.obtainStyledAttributes(resId, attrs);
            } else {
                return mTheme.obtainStyledAttributes(fixRes(resId, isHostRes, "style"), fixResArray(attrs, isHostAttrs, "attr"));
            }
        }

        public TypedArray obtainStyledAttributes(@StyleableRes int[] attrs) throws Resources.NotFoundException {
            if (isDefaultSkin || mTheme == null) {
                return mContext.obtainStyledAttributes(attrs);
            } else {
                return mTheme.obtainStyledAttributes(fixResArray(attrs, "attr"));
            }
        }

        private int fixRes(int resId, String type) {
            return fixRes(resId, true, type);
        }

        private int fixRes(int resId, boolean needFix, String type) {
            if (needFix && resId != 0) {
                int targetId = getTargetResId(resId, type);
                if (targetId != 0) {
                    resId = targetId;
                }
            }
            return resId;
        }

        private int[] fixResArray(int[] array, String type) {
            return fixResArray(array, true, type);
        }

        private int[] fixResArray(int[] array, boolean needFix, String type) {
            int[] newArray = null;
            if (needFix && array != null) {
                newArray = new int[array.length];
                for (int i = 0; i < array.length; i++) {
                    newArray[i] = array[i];
                    if (newArray[i] != 0) {
                        newArray[i] = fixRes(newArray[i], true, type);
                    }
                }
            }
            if (newArray != null) {
                return newArray;
            }
            return array;
        }
    }
}
