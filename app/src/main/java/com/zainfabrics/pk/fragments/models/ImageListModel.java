package com.zainfabrics.pk.fragments.models;

import java.io.Serializable;

/**
 * Created by M Umer Saleem on 9/27/2017.
 */

public class ImageListModel implements Serializable {
    String id,image, name;

    public ImageListModel(String image, String name) {

        this.image = image;
        this.name = name;
    }

    public ImageListModel(String name) {
        this.name = name;
    }

    public ImageListModel(String id, String image, String name) {
        this.id = id;
        this.image =image;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }


}
