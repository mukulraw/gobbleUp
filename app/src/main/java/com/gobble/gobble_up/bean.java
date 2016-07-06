package com.gobble.gobble_up;

public class bean {
    private String left , right;

    public bean(String left , String right)
    {
        this.left = left;
        this.right = right;
    }

    String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

}
