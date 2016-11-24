package com.gobble.gobble_up;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class updateBean {

    @SerializedName("msg")
    @Expose
    private String msg;

    /**
     *
     * @return
     * The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     *
     * @param msg
     * The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }


}
