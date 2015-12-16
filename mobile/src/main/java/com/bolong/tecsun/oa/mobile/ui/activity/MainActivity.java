package com.bolong.tecsun.oa.mobile.ui.activity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bolong.tecsun.oa.mobile.R;
import com.bolong.tecsun.oa.mobile.core.api.APIConstants;
import com.bolong.tecsun.oa.mobile.core.entity.Test;
import com.bolong.tecsun.oa.mobile.core.entity.TokenData;
import com.bolong.tecsun.oa.mobile.core.repository.PreferenceManager;
import com.bolong.tecsun.oa.mobile.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private TextView text;

    @Override
    protected void initializeViewsAndData() {
        initializeViews();
        getData();

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void requestSuccess(Object response) {
        switch (url){
            case APIConstants.Urls.BASE_URL:

                Test test= (Test) response;
                if(test.isCorrect()){
                    text.setText(test.getText());
                }else {
                    getAccessTokenByRefreshToken();
                }
                break;
            case APIConstants.Urls.REFRESH_TOKEN_URL:

                TokenData tokenData= (TokenData) response;
                if(tokenData.isCorrect()){
                    PreferenceManager.getInstance().setAccessToken(tokenData.getAccessToken());
                    getData();
                }else {
                    startActivity(SignActivity.class);
                    finish();
                }
        }

    }

    /**
     *初始化视图
     */
    private void initializeViews() {
        text= (TextView) findViewById(R.id.main_text);
    }

    /**
     * 获取数据
     */
    private void getData() {
        url=APIConstants.Urls.BASE_URL;
        urlParams.clear();
        urlParams.put(PreferenceManager.FIELD_NAME_OF_ACCESS_TOKEN,PreferenceManager.getInstance().getAccessToken());
        setNetworkRequest(Request.Method.GET, url, Test.class);
    }

    @Override
    protected void requestError(VolleyError error) {
    }

    @Override
    protected boolean isSetToolbar() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_setting:
                startActivity(SettingActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
