package com.gobble.gobble_up.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nutration {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("p_id")
    @Expose
    private String pId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("unit")
    @Expose
    private String unit;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The pId
     */
    public String getPId() {
        return pId;
    }

    /**
     *
     * @param pId
     * The p_id
     */
    public void setPId(String pId) {
        this.pId = pId;
    }

    /**
     *
     * @return
     * The value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     * The unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     *
     * @param unit
     * The unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
