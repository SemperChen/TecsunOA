package com.bolong.tecsun.oa.mobile.core.net;

import android.net.Uri;
import com.android.volley.Response;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
public class VolleyWrapper<T> {

    private String url;
    private int method;
    private Class<T> mClass;
    private Uri.Builder builder;

    private Response.Listener<T> mListener;
    private Response.ErrorListener mErrorListener;

    public VolleyWrapper(int method,
                         String url,
                         Class<T> tClass,
                         Response.Listener<T> mListener,
                         Response.ErrorListener mErrorListener) {
        this.method = method;
        this.url = url;
        this.mClass = tClass;
        this.mListener = mListener;
        this.mErrorListener = mErrorListener;
    }

    /**
     * 发送GET请求并添加标记
     */
    public void sendGETRequest(String tag) {
        GsonRequest<T> request = new GsonRequest<>(method, url, mClass, mListener, mErrorListener);
        request.setTag(tag);
        RequestManager.getRequestQueue().add(request);
    }

    /**
     * 取消所有指定标记的请求
     *
     * @param tag 标签
     */
    public void cancelRequest(String tag){
        RequestManager.getRequestQueue().cancelAll(tag);
    }

    /**
     * 为url添加参数
     *
     * @param param 参数
     * @param value 值
     */
    public void addUrlParameter(String param,Object value){
        builder = Uri.parse(url).buildUpon().appendQueryParameter(param,value.toString());
        url = builder.build().toString();
    }

    /**
     * 为url添加参数
     *
     * @param urlParams map数组
     */
    public void addUrlParameter(Map<String, Object> urlParams) {
        builder = Uri.parse(url).buildUpon();
        for (int i = 0; i < urlParams.size(); i++) {
            Set keys = urlParams.keySet();
            Iterator iterator = keys.iterator();
            for (; iterator.hasNext(); ) {
                String key = (String) iterator.next();
                Object value = urlParams.get(key);
                if (value!=null){
                    builder.appendQueryParameter(key, value.toString());
                }
            }
        }
        url = builder.build().toString();
    }
}