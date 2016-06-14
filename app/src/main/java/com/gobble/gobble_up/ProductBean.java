package com.gobble.gobble_up;

/**
 * Created by hi on 30-05-2016.
 */
public class ProductBean {
    int id;
    String image , name , price , description;
    Boolean set = false;

    public void setSet(Boolean set) {
        this.set = set;
    }

    public Boolean getSet() {
        return set;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {


        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
