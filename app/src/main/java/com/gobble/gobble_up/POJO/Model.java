package com.gobble.gobble_up.POJO;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("nutration")
    @Expose
    private List<Nutration> nutration = new ArrayList<Nutration>();
    @SerializedName("image")
    @Expose
    private String image;

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
     * The brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     *
     * @param brand
     * The brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     *
     * @return
     * The size
     */
    public String getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The size
     */
    public void setSize(String size) {
        this.size = size;
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

}
