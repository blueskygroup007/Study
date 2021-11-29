package com.bluesky.study.ui.gallery;

import java.io.Serializable;

/**
 * @author BlueSky
 * @date 2021/11/29
 * Description:
 */
public class Sence implements Serializable {
    private String picture;
    private String description;

    public Sence(String picture, String description) {
        this.picture = picture;
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
