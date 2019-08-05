package com.ciba.data.gather.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.ciba.data.synchronize.manager.LoaderUploaderManager;
import com.ciba.data.synchronize.uploader.ActivityLifecycleUploader;


/**
 * @author ciba
 * @description Activity生命周期回调
 * @date 2018/12/3
 */
public class CustomActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        onActivityLifecycleChanged(ActivityLifecycleUploader.ACTIVITY_CREATED, activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        onActivityLifecycleChanged(ActivityLifecycleUploader.ACTIVITY_STARTED, activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        onActivityLifecycleChanged(ActivityLifecycleUploader.ACTIVITY_RESUMED, activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        onActivityLifecycleChanged(ActivityLifecycleUploader.ACTIVITY_PAUSED, activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        onActivityLifecycleChanged(ActivityLifecycleUploader.ACTIVITY_STOPPED, activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        onActivityLifecycleChanged(ActivityLifecycleUploader.ACTIVITY_SAVE_INSTANCE_STATE, activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        onActivityLifecycleChanged(ActivityLifecycleUploader.ACTIVITY_DESTROYED, activity);
    }

    /**
     * Activity生命周期变化
     */
    private void onActivityLifecycleChanged(int lifecycle, Activity activity) {
        LoaderUploaderManager.getInstance().uploadActivityLifecycle(lifecycle, activity);
    }
}
