package com.bolong.tecsun.oa.mobile.core;

import android.app.Application;
import com.bolong.tecsun.oa.mobile.core.net.RequestManager;
import com.bolong.tecsun.oa.mobile.core.repository.PreferenceManager;

/**
 * <p></p>
 * <p/>
 * <p>Project: TecsunOA.</p>
 * <p>Date: 2015/10/28.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        RequestManager.init(this);
        PreferenceManager.init(this);

    }

    @Override
    public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();
    }

}