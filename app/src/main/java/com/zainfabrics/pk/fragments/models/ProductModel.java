package com.zainfabrics.pk.fragments.models;

import java.io.Serializable;

/**
 * Created by M Umer Saleem on 9/28/2017.
 */

public class ProductModel implements Serializable{
    String code, title, price, salePrice,shirt,trouser, dupatta, image,image_thumb,other_img,other_img_thumb,quantity,cart_quantity,type,weight;

    public ProductModel(String code, String title, String price, String salePrice, String shirt, String trouser, String dupatta, String image, String image_thumb, String other_img, String other_img_thumb, String quantity,String weight) {
        this.code = code;
        this.title = title;
        this.price = price;
        this.salePrice = salePrice;
        this.shirt = shirt;
        this.trouser = trouser;
        this.dupatta = dupatta;
        this.image = image;
        this.image_thumb =image_thumb;
        this.other_img = other_img;
        this.other_img_thumb = other_img_thumb;
        this.quantity = quantity;
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public String getShirt() {
        return shirt;
    }

    public String getTrouser() {
        return trouser;
    }

    public String getDupatta() {
        return dupatta;
    }

    public String getImage() {
        return image;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public String getOther_img() {
        return other_img;
    }

    public String getOther_img_thumb() {
        return other_img_thumb;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public String getCartQuantity() {
        return cart_quantity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getWeight() {
        return weight;
    }
}
