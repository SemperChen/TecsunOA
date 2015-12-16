package com.bolong.tecsun.oa.mobile.core.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * <p></p>
 * <p/>
 * <p>Project: TecsunOA.</p>
 * <p>Date: 2015/11/5.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class PreferenceManager {

    private static final String TAG = "PreferenceManager";

    private static final String SHARE_PREFERENCE_NAME = "global.preference";
    private static PreferenceManager instance = null;
    private SharedPreferences preferences;

    private String accessToken;
    public static final String FIELD_NAME_OF_ACCESS_TOKEN = "accessToken";
    public static final String VALUE_OF_ACCESS_TOKEN = null;

    private PreferenceManager(Context context) {
        this.preferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        this.accessToken=preferences.getString(FIELD_NAME_OF_ACCESS_TOKEN,VALUE_OF_ACCESS_TOKEN);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
    }

    /**
     * 获取唯一实例
     *
     * @return 数据源实例
     */
    public static PreferenceManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(TAG + "未初始化");
        }
        return instance;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public void setAccessToken(String accessToken){
        preferences.edit().putString(FIELD_NAME_OF_ACCESS_TOKEN,accessToken).apply();
        this.accessToken=accessToken;
    }
}
