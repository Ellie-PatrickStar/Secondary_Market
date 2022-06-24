package com.example.secondary_market;

public class Bean {

    private int iconId;
    //商品图片,以二进制字节存储
    private byte[] picture;
    private String title;
    private float price;
    private String phone;

    public Bean(int iconId, String title,float price, String phone) {
        this.iconId = iconId;
        this.picture= picture;
        this.title = title;
        this.price = price;
        this.phone = phone;
    }
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
