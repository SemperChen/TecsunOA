package com.bolong.tecsun.oa.mobile.core.net;

import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * <p>自定义Request</p>
 * <p/>
 * <p>Project: TecsunOA.</p>
 * <p>Date: 2015/10/28.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class GsonRequest<T> extends Request<T> {

    private final static String TAG=GsonRequest.class.getSimpleName();
    private final static boolean DEBUG=true;

    private static final String PROTOCOL_CHARSET = "utf-8";
    private final Gson mGson;
    private final Class<T> mClass;

    private Response.Listener<T> mListener;

    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        this.mClass = clazz;
        this.mListener = listener;
    }

    /**
     *
     * @param response 响应
     * @return 所接收的对象
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            //String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            String json = new String(response.data, PROTOCOL_CHARSET);
            if(DEBUG){
                Log.d(TAG,json);
            }
            return Response.success(mGson.fromJson(json, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            if(DEBUG){
                Log.e(TAG,"不支持编码格式");
            }
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            if(DEBUG){
                Log.e(TAG,"json格式错误");
            }
            return Response.error(new ParseError(e));
        }
    }

    /**
     *数据返回成功后，调用onResponse方法
     *
     * @param response 响应
     */
    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}
