package com.wits.grofastsupplier.Api.Model;

public class TaxModel {
    private Integer id;
    private String uuid;
    private String tax_percentage;
    private String name;
    private String type;

    public TaxModel(String name, Integer id) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTax_percentage() {
        return tax_percentage;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
