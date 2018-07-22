package com.liu.dance.model;

import java.util.ArrayList;

/**
 * Created by 舞动的心 on 2018/1/19.
 */

public class ProvinceModel {

    private String name;
    private ArrayList<CityModel> cityList;


    public  ArrayList< ArrayList<String>> getDisNameList() {
        ArrayList< ArrayList<String>> list = new ArrayList<>();
        for (CityModel cityModel : cityList) {
            list.add(cityModel.getDisList());
        }
        return list;
    }

    public  ArrayList<String> getCityNameList() {
        ArrayList<String> list = new ArrayList<>();
        for (CityModel cityModel : cityList) {
            list.add(cityModel.getPickerViewText());
        }
        return list;
    }

    public ProvinceModel() {
        super();
    }

    public ProvinceModel(String name,  ArrayList<CityModel> cityList) {
        super();
        this.name = name;
        this.cityList = cityList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CityModel> getCityList() {
        return cityList;
    }

    public void setCityList( ArrayList<CityModel> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
    }

    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return name;
    }

}
