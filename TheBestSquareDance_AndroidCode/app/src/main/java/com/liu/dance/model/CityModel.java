package com.liu.dance.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 舞动的心 on 2018/1/19.
 */

public class CityModel {

    private String name;
    private ArrayList<DistrictModel> districtList;

    public ArrayList<String> getDisList() {
        ArrayList<String> list = new ArrayList<>();
        for (DistrictModel districtModel : districtList) {
            list.add(districtModel.getPickerViewText());
        }
        return list;
    }

    public CityModel() {
        super();
    }

    public CityModel(String name,  ArrayList<DistrictModel> districtList) {
        super();
        this.name = name;
        this.districtList = districtList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DistrictModel> getDistrictList() {
        return districtList;
    }

    public void setDistrictList( ArrayList<DistrictModel> districtList) {
        this.districtList = districtList;
    }

    @Override
    public String toString() {
        return "CityModel [name=" + name + ", districtList=" + districtList
                + "]";
    }

    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return name;
    }

}
