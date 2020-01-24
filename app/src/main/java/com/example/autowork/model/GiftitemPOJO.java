package com.example.autowork.model;

public class GiftitemPOJO {
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_URL() {
        return Item_URL;
    }

    public void setItem_URL(String item_URL) {
        Item_URL = item_URL;
    }

    public String getItem_type_code() {
        return Item_type_code;
    }

    public void setItem_type_code(String item_type_code) {
        Item_type_code = item_type_code;
    }

    public String getWishCount() {
        return WishCount;
    }

    public void setWishCount(String wishCount) {
        WishCount = wishCount;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    String  item_name
            ,item_image
            ,item_price
            ,Item_URL
            ,Item_type_code
            ,WishCount
            ,CreatedAt;
}
