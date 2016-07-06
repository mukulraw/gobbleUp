package com.gobble.gobble_up;

class addListBean {


    private String listId , listName , createdTime , totalItem;


    String getListId() {
        return listId;
    }

    void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }

    String getTotalItem() {
        return totalItem;
    }

    String getListName() {
        return listName;
    }

    void setListId(String listId) {
        this.listId = listId;
    }

    void setListName(String listName) {
        this.listName = listName;
    }

    String getCreatedTime() {
        return createdTime;
    }

    void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
