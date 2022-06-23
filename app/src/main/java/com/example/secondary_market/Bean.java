package com.example.secondary_market;

public class Bean {

    private int iconId;
    private String title;
    private float price;
    private String phone;

    public Bean(int iconId, String title,float price, String phone) {
        this.iconId = iconId;
        this.title = title;
        this.price = price;
        this.phone = phone;
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
