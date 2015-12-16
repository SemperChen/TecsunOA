package com.bolong.tecsun.oa.mobile.core.entity;

import com.google.gson.annotations.SerializedName;

/**
 * <p></p>
 * <p/>
 * <p>Project: TecsunOA.</p>
 * <p>Date: 15/11/6.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class TokenData {
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("refreshToken")
    private String refreshToken;
    @SerializedName("isCorrect")
    private boolean isCorrect=false;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
