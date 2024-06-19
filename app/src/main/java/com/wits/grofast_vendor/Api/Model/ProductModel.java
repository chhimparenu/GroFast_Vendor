package com.wits.grofast_vendor.Api.Model;

import static com.wits.grofast_vendor.CommonUtilities.formatDate;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductModel {
    private String name;
    private String image;
    private String product_code;
    private Integer category_id;
    private String unit_id;
    private Integer tax_id;
    private String per;
    private String price;
    private String discount;
    @SerializedName("product_status")
    private ProductStatus productStatus;
    @SerializedName("is_returnable")
    private String return_policy;
    public static class ProductStatus {
        private String label;
        private String color;

        public String getLabel() {
            return label;
        }

        public String getColor() {
            return color;
        }

    }

    private String stock_status;
    private String product_detail;
    private String uuid;
    private String created_at;
    private Integer id;

    public String getCreated_at() {
        return formatDate(created_at);
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getProduct_code() {
        return product_code;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public Integer getTax_id() {
        return tax_id;
    }

    public String getPer() {
        return per;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getReturn_policy() {
        return return_policy;
    }

    public String getStock_status() {
        return stock_status;
    }

    public String getProduct_detail() {
        return product_detail;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getId() {
        return id;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }
}
