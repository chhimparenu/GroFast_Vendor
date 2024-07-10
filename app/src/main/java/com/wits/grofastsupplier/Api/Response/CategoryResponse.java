package com.wits.grofastsupplier.Api.Response;

import com.wits.grofastsupplier.Api.Model.CategoryModel;

import java.util.List;

public class CategoryResponse {

    private String message;

    private Integer status;

    private List<CategoryModel> categories;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }
}
