package com.bolong.tecsun.oa.mobile.ui.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bolong.tecsun.oa.mobile.R;
import com.bolong.tecsun.oa.mobile.core.api.APIConstants;
import com.bolong.tecsun.oa.mobile.core.entity.TokenData;
import com.bolong.tecsun.oa.mobile.core.net.VolleyWrapper;
import com.bolong.tecsun.oa.mobile.core.util.EncryptionUtil;
import com.bolong.tecsun.oa.mobile.core.util.VolleyErrorUtil;
import com.bolong.tecsun.oa.mobile.ui.activity.SignActivity;

import java.util.HashMap;
import java.util.Map;

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
public abstract class BaseActivity extends AppCompatActivity{
    protected Toolbar mToolbar;
    protected VolleyWrapper volleyWrapper;
    //url参数
    protected Map<String,Object> urlParams =new HashMap<>();
    //标记
    protected String tag;
    protected String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("必须返回一个内容视图布局ID");
        }
        
        initializeViewsAndData();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if(isSetToolbar()){
            mToolbar= (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
        }

    }

    /**
     * 发送网络请求
     *
     * @param method 请求方法
     * @param url 请求地址
     * @param tClass 类
     * @param <T> 泛型
     */
    protected <T> void setNetworkRequest(
            int method,
            String url,
            Class<T> tClass){
        volleyWrapper=new VolleyWrapper<>(method,url,tClass,new RequestSuccessListener<T>(),new RequestErrorListener());
        volleyWrapper.addUrlParameter(urlParams);
        volleyWrapper.sendGETRequest(tag);
    }

    /**
     * 请求成功监听类
     */
    private class RequestSuccessListener<T> implements Response.Listener<T>{
        @Override
        public void onResponse(Object response) {
            requestSuccess(response);
        }
    }

    /**
     * 请求失败监听
     */
    private class RequestErrorListener implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error) {
            //请求错误处理
            String errorMessage= VolleyErrorUtil.getMessage(error,BaseActivity.this);
            dialog(errorMessage);
            requestError(error);
        }
    }

    /**
     * 会话
     *
     * @param errorMessage 错误信息
     */
    private void dialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BaseActivity.this.finish();
            }
        });

        builder.create().show();
    }

    /**
     * 通过refreshToken请求accessToken
     */
    protected void getAccessTokenByRefreshToken(){
        EncryptionUtil.init(getApplicationContext());
        String refreshToken= EncryptionUtil.getInstance().decryptText();
        if(refreshToken!=null){
            url= APIConstants.Urls.REFRESH_TOKEN_URL;
            urlParams.clear();
            urlParams.put("refreshToken",refreshToken);
            setNetworkRequest(Request.Method.GET,url,TokenData.class);
        }else {
            startActivity(SignActivity.class);
            finish();
        }
    }

    /**
     * 跳转到目标Activity
     *
     * @param clazz 类
     */
    protected void startActivity(Class clazz){
        Intent intent=new Intent(this,clazz);
        startActivity(intent);
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

    /**
     * 请求成功返回response
     *
     * @param response 响应
     */
    protected abstract void requestSuccess(Object response);

    /**
     * 请求失败返回error
     */
    protected abstract void requestError(VolleyError error);

    /**
     * 是否需要toolbar
     *
     * @return boolean
     */
    protected abstract boolean isSetToolbar();


}
