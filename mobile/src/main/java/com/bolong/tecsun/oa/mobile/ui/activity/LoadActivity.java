package com.bolong.tecsun.oa.mobile.ui.activity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bolong.tecsun.oa.mobile.R;
import com.bolong.tecsun.oa.mobile.core.api.APIConstants;
import com.bolong.tecsun.oa.mobile.core.entity.TokenData;
import com.bolong.tecsun.oa.mobile.core.repository.PreferenceManager;
import com.bolong.tecsun.oa.mobile.ui.base.BaseActivity;

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
public class LoadActivity extends BaseActivity {

    @Override
    protected void initializeViewsAndData() {

        setTitle(getString(R.string.app_name));

        //标记请求,让请求可以根据标记取消
        tag="accessToken";
        String accessToken=PreferenceManager.getInstance().getAccessToken();
        url=APIConstants.Urls.ACCESS_TOKEN_URL;
        urlParams.clear();
        //添加请求参数
        urlParams.put(
                PreferenceManager.FIELD_NAME_OF_ACCESS_TOKEN,accessToken);
        setNetworkRequest(Request.Method.GET,url,TokenData.class);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_load;
    }

    @Override
    protected void requestSuccess(Object response) {
        TokenData tokenData= (TokenData) response;
        switch (url){
            //验证accessToken
            case APIConstants.Urls.ACCESS_TOKEN_URL:

                if(tokenData.isCorrect()){
                    //验证accessToken成功
                    startActivity(MainActivity.class);
                    finish();
                }else{
                    getAccessTokenByRefreshToken();
                }
                break;
            case APIConstants.Urls.REFRESH_TOKEN_URL:

                if(tokenData.isCorrect()){
                    //通过refreshToken获取accessToken成功
                    PreferenceManager.getInstance().setAccessToken(tokenData.getAccessToken());
                    startActivity(MainActivity.class);
                    finish();
                }else {
                    startActivity(SignActivity.class);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void requestError(VolleyError error) {
    }

    @Override
    protected boolean isSetToolbar() {
        return false;
    }
}
