package com.bolong.tecsun.oa.mobile.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bolong.tecsun.oa.mobile.R;
import com.bolong.tecsun.oa.mobile.core.api.APIConstants;
import com.bolong.tecsun.oa.mobile.core.entity.TokenData;
import com.bolong.tecsun.oa.mobile.core.util.EncryptionUtil;
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
public class SignActivity extends BaseActivity {

    private EditText username;
    private EditText password;
    private Button signIn;

    @Override
    protected void initializeViewsAndData() {
        initializeViews();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlParams.put("username",username.getText());
                urlParams.put("password",password.getText());
                setNetworkRequest(Request.Method.GET, APIConstants.Urls.SIGN_URL, TokenData.class);
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initializeViews() {
        username= (EditText) findViewById(R.id.sign_username);
        password= (EditText) findViewById(R.id.sign_password);
        signIn= (Button) findViewById(R.id.sign_in);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sign;
    }

    @Override
    protected void requestSuccess(Object response) {
        TokenData tokenData= (TokenData) response;
        if (tokenData.isCorrect()){
            //获取refreshToken成功,进行加密储存
            EncryptionUtil.init(getApplicationContext());
            EncryptionUtil.getInstance().encryptText(tokenData.getRefreshToken());
            startActivity(MainActivity.class);
            finish();
        }else{
            //用户名或密码错误及其他原因
            Toast.makeText(this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
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
