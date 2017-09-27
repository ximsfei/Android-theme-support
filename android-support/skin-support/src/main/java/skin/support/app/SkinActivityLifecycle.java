package skin.support.app;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;

import java.lang.reflect.Field;
import java.util.WeakHashMap;

import skin.support.SkinCompatManager;
import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;
import skin.support.widget.SkinCompatThemeUtils;

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private static volatile SkinActivityLifecycle sInstance = null;
    private WeakHashMap<Activity, SkinCompatDelegate> mSkinDelegateMap;
    private WeakHashMap<Activity, SkinObserver> mSkinObserverMap;

    public static SkinActivityLifecycle init(Application application) {
        if (sInstance == null) {
            synchronized (SkinActivityLifecycle.class) {
                if (sInstance == null) {
                    sInstance = new SkinActivityLifecycle(application);
                }
            }
        }
        return sInstance;
    }

    private SkinActivityLifecycle(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    private SkinCompatDelegate getSkinDelegate(Activity activity) {
        if (mSkinDelegateMap == null) {
            mSkinDelegateMap = new WeakHashMap<>();
        }

        SkinCompatDelegate mSkinDelegate = mSkinDelegateMap.get(activity);
        if (mSkinDelegate == null) {
            mSkinDelegate = SkinCompatDelegate.create(activity);
        }
        mSkinDelegateMap.put(activity, mSkinDelegate);
        return mSkinDelegate;
    }

    private SkinObserver getObserver(final Activity activity) {
        if (mSkinObserverMap == null) {
            mSkinObserverMap = new WeakHashMap<>();
        }
        SkinObserver observer = mSkinObserverMap.get(activity);
        if (observer == null) {
            observer = new SkinObserver() {
                @Override
                public void updateSkin(SkinObservable observable, Object o) {
                    updateStatusBarColor(activity);
                    updateWindowBackground(activity);
                    getSkinDelegate(activity).applySkin();
                }
            };
        }
        mSkinObserverMap.put(activity, observer);
        return observer;
    }

    private void updateStatusBarColor(Activity activity) {
        if (SkinCompatManager.getInstance().isSkinStatusBarColorEnable()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = SkinCompatThemeUtils.getStatusBarColor(activity);
            if (color != 0) {
                activity.getWindow().setStatusBarColor(color);
            }
        }
    }

    private void updateWindowBackground(Activity activity) {
        if (SkinCompatManager.getInstance().isSkinWindowBackgroundEnable()) {
            Drawable drawable = SkinCompatThemeUtils.getWindowBackgroundDrawable(activity);
            if (drawable != null) {
                activity.getWindow().setBackgroundDrawable(drawable);
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
            LayoutInflaterCompat.setFactory(activity.getLayoutInflater(),
                    getSkinDelegate(activity));
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        updateStatusBarColor(activity);
        updateWindowBackground(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        SkinCompatManager.getInstance().addObserver(getObserver(activity));
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinCompatManager.getInstance().deleteObserver(getObserver(activity));
        mSkinObserverMap.remove(activity);
        mSkinDelegateMap.remove(activity);
    }
}