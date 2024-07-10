package com.wits.grofastsupplier.Api.Model;

public class UnitModel {
    private Integer id;
    private String uuid;
    private String unit_name;
    private String is_active;

    public UnitModel(String unit_name,Integer id) {
        this.id = id;
        this.unit_name = unit_name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public String getIs_active() {
        return is_active;
    }

    public Integer getId() {
        return id;
    }
}
