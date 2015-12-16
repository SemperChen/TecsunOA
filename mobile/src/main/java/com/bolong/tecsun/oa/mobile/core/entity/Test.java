package com.bolong.tecsun.oa.mobile.core.entity;

import com.google.gson.annotations.SerializedName;

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
public class Test {
    @SerializedName("text")
    private String text;
    @SerializedName("string")
    private String string;
    @SerializedName("isCorrect")
    private boolean isCorrect;

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
