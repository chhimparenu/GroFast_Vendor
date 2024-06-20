package com.wits.grofast_vendor.Api.Model;

import static com.wits.grofast_vendor.CommonUtilities.formatDate;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductModel implements Parcelable {
    private String name;
    private String image;
    private String product_code;
    private int category_id;
    private String unit_id;
    private String tax_id;
    private String per;
    private String price;
    private String discount;
    @SerializedName("product_status")
    private ProductStatus productStatus;
    @SerializedName("is_returnable")
    private String return_policy;

    protected ProductModel(Parcel in) {
        name = in.readString();
        image = in.readString();
        product_code = in.readString();
        category_id = in.readInt();
        unit_id = in.readString();
        tax_id = in.readString();
        per = in.readString();
        price = in.readString();
        discount = in.readString();
        return_policy = in.readString();
        stock_status = in.readString();
        product_detail = in.readString();
        uuid = in.readString();
        created_at = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(product_code);
        dest.writeInt(category_id);
        dest.writeString(unit_id);
        dest.writeString(tax_id);
        dest.writeString(per);
        dest.writeString(price);
        dest.writeString(discount);
        dest.writeString(return_policy);
        dest.writeString(stock_status);
        dest.writeString(product_detail);
        dest.writeString(uuid);
        dest.writeString(created_at);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
    }

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

    public int getCategory_id() {
        return category_id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public String getTax_id() {
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
