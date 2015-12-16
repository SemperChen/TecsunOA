package com.bolong.tecsun.oa.mobile.core.util;

import android.content.Context;
import com.android.volley.*;
import com.bolong.tecsun.oa.mobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 * <p/>
 * <p>Project: TecsunOA.</p>
 * <p>Date: 15/11/10.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class VolleyErrorUtil {
    /**
     * 返回请求错误消息
     *
     * @param error 错误
     * @param context 上下文
     * @return string
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_down);
        }
        else if (isServerProblem(error)) {
            return handleServerError(error, context);
        }
        else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.no_internet);
        }
        return context.getResources().getString(R.string.generic_error);
    }

    /**
     * 请求错误是否与网络有关
     *
     * @param error 错误
     * @return boolean
     */
    private static boolean isNetworkProblem(Object error) {
        //return (error instanceof NetworkError) || (error instanceof NoConnectionError);
        return (error instanceof NetworkError);
    }
    /**
     * 请求错误是否与服务器有关
     *
     * @param error 错误
     * @return boolean
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }
    /**
     * 处理服务器错误
     *
     * @param err 错误
     * @param context 上下文
     * @return string
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:return context.getString(R.string.no_find_server);
                case 422:return context.getString(R.string.generic_error);
                case 401:
                    try {
                        // 服务器可能返回的错误,如{ "error": "Some error occured" }
                        HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());

                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 无效的请求
                    return error.getMessage();

                default:
                    return context.getResources().getString(R.string.generic_server_down);
            }
        }
        return context.getResources().getString(R.string.generic_error);
    }
}