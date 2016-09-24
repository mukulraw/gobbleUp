package com.gobble.gobble_up;


public class offlineSubListBean {

    private String listId , name , price , productId;

    byte[] image;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }



    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }


}
