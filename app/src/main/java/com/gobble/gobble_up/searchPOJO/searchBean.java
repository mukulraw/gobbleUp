package com.gobble.gobble_up.searchPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class searchBean {


    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("subcat_name")
    @Expose
    private String subcatName;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("nutration")
    @Expose
    private List<Nutration> nutration = new ArrayList<Nutration>();

    /**
     *
     * @return
     * The subCatId
     */
    public String getSubCatId() {
        return subCatId;
    }

    /**
     *
     * @param subCatId
     * The sub_cat_id
     */
    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    /**
     *
     * @return
     * The catId
     */
    public String getCatId() {
        return catId;
    }

    /**
     *
     * @param catId
     * The cat_id
     */
    public void setCatId(String catId) {
        this.catId = catId;
    }

    /**
     *
     * @return
     * The subcatName
     */
    public String getSubcatName() {
        return subcatName;
    }

    /**
     *
     * @param subcatName
     * The subcat_name
     */
    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    /**
     *
     * @return
     * The catName
     */
    public String getCatName() {
        return catName;
    }

    /**
     *
     * @param catName
     * The cat_name
     */
    public void setCatName(String catName) {
        this.catName = catName;
    }

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
     * The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The tag
     */
    public String getTag() {
        return tag;
    }

    /**
     *
     * @param tag
     * The tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The nutration
     */
    public List<Nutration> getNutration() {
        return nutration;
    }

    /**
     *
     * @param nutration
     * The nutration
     */
    public void setNutration(List<Nutration> nutration) {
        this.nutration = nutration;
    }



}
