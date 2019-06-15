package com.zainfabrics.pk.fragments.models;

import java.io.Serializable;

/**
 * Created by Talal on 10/15/2017.
 */

public class CategoryBrandModel implements Serializable{
    String image;

    public CategoryBrandModel(String image) {
        this.image =image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
