package com.liu.dance.model;

/**
 * Created by 舞动的心 on 2018/1/19.
 */

public class DistrictModel {

    private String name;
    private String zipcode;

    public DistrictModel() {
        super();
    }

    public DistrictModel(String name, String zipcode) {
        super();
        this.name = name;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
    }

    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return name;
    }

}
