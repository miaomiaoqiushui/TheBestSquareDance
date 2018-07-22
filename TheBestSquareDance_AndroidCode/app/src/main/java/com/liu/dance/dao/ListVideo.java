package com.liu.dance.dao;

/**
 * Created by 舞动的心 on 2018/1/27.
 */

public class ListVideo {

    private String name;

    private String imageId;

    private boolean isChecked;

    public ListVideo(String name, String imageId, boolean isChecked) {
        this.name = name;
        this.imageId = imageId;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public String getImageId() {
        return imageId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
