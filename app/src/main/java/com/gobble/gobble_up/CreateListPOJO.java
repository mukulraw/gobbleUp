package com.gobble.gobble_up;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class CreateListPOJO {

    @SerializedName("listId")
    @Expose
    private Integer listId;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;

    /**
     *
     * @return
     * The listId
     */
    public Integer getListId() {
        return listId;
    }

    /**
     *
     * @param listId
     * The listId
     */
    public void setListId(Integer listId) {
        this.listId = listId;
    }

    /**
     *
     * @return
     * The responseCode
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     *
     * @param responseCode
     * The responseCode
     */
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }


}
