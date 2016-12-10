package com.gobble.gobble_up;

public class filterBean {

    String name;
    private Boolean isChecked = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Boolean getChecked() {
        return isChecked;
    }

    void setChecked(Boolean checked) {
        isChecked = checked;
    }


}
