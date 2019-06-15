package com.zainfabrics.pk.fragments.models;

import java.io.Serializable;

/**
 * Created by Talal on 10/1/2017.
 */

public class RelatedProductsModel implements Serializable {
    String image, title, price;

    public RelatedProductsModel(String image, String title, String price) {
        this.image = "http://zainfabricspk.com/img/cat/" + image;
        this.title = title;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
