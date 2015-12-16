package com.bolong.tecsun.oa.mobile.ui.activity;

import android.os.Bundle;
import com.android.volley.VolleyError;
import com.bolong.tecsun.oa.mobile.R;
import com.bolong.tecsun.oa.mobile.ui.base.BaseActivity;

/**
 * <p></p>
 * <p/>
 * <p>Project: TecsunOA.</p>
 * <p>Date: 2015/10/29.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }

    @Override
    protected void initializeViewsAndData() {
        setTitle(getString(R.string.setting));
        initializeViews();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void requestSuccess(Object response) {

    }

    @Override
    protected void requestError(VolleyError error) {

    }


    @Override
    protected boolean isSetToolbar() {
        return true;
    }

    /**
     * 初始化视图
     */
    private void initializeViews() {
    }
}
