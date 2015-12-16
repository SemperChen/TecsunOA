package com.bolong.tecsun.oa.mobile.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p></p>
 * <p/>
 * <p>Project: TecsunOA.</p>
 * <p>Date: 2015/10/30.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public abstract class BaseFragment extends Fragment{

    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getContentViewLayoutID() != 0) {
            mView= inflater.inflate(getContentViewLayoutID(),container,false);
            initializeViewsAndData();
            return mView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    /**
     * 初始化数据
     */
    protected abstract void initializeViewsAndData();

    /**
     * 获取内容视图布局ID
     *
     * @return id
     */
    protected abstract int getContentViewLayoutID();
}
