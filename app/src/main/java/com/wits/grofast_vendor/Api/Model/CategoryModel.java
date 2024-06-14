package com.wits.grofast_vendor.Api.Model;

public class CategoryModel {
    private String image;

    private String category_name;

    private Integer is_active;

    private Integer id;

    private String uuid;

    public CategoryModel(String category_name, Integer id) {
        this.category_name = category_name;
        this.id = id;
    }


    public String getImage() {
        return image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }
}
