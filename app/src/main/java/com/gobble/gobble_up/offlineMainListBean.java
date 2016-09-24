package com.gobble.gobble_up;

public class offlineMainListBean {


    private String listId , listName , createdTime , totalItems;




    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }



    public String getListId() {
        return listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
