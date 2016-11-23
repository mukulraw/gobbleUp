package com.gobble.gobble_up;

public class ProductBean {
    int id;
    String image , name , price , protein , calories , brand , size;
    Boolean set = false;
    Boolean setlist = false;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }



    public void setSetlist(Boolean setlist) {
        this.setlist = setlist;
    }

    public Boolean getSetlist() {
        return setlist;
    }

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

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
