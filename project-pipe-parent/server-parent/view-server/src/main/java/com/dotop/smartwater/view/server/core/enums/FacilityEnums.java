package com.dotop.smartwater.view.server.core.enums;

import lombok.Getter;

/**
 * 设施类型  包含水厂
 */
public enum FacilityEnums {


    WATER_FACTORY("WATER_FACTORY", "水厂", "water_factory", "water_factory");

    @Getter
    private String id;
    @Getter
    private String name;

    FacilityEnums(String id, String name, String Category, String type) {
        this.id = id;
        this.name = name;
    }

    
}
