package com.dotop.smartwater.view.server.core.enums;

import lombok.Getter;

/**
 * 水厂设施存在的子设备
 */
public enum WaterFactoryPondEnum {

    WATER_FACTORY_PRECIPITATE_POND("water_factory_precipitate_pond", "沉淀池", FacilityEnums.WATER_FACTORY, "5", "1"),

    WATER_FACTORY_MEDICINE_POND("water_factory_medicine_pond", "加药池", FacilityEnums.WATER_FACTORY, "5", "1"),

    WATER_FACTORY_FILTER_POND("water_factory_filter_pond", "过滤池", FacilityEnums.WATER_FACTORY, "5", "1"),

    WATER_FACTORY_HIGH_PUMP_POND("water_factory_high_pump_pond", "高压泵池", FacilityEnums.WATER_FACTORY, "5", "1"),

    WATER_FACTORY_DOUBLE_VALVE_FILTER_POND("water_factory_double_valve_filter_pond", "双阀滤池", FacilityEnums.WATER_FACTORY, "5", "1"),

    WATER_FACTORY_SHIMIZU_POND("water_factory_shimizu_pond", "清水池", FacilityEnums.WATER_FACTORY, "5", "1"),

    WATER_FACTORY_TUBE_FILTER_POND("water_factory_tube_filter_pond", "协管滤池", FacilityEnums.WATER_FACTORY, "5", "1"),

    WATER_FACTORY_REACTION_POND("water_factory_reaction_pond", "反应池", FacilityEnums.WATER_FACTORY, "5", "1"),

    WATER_FACTORY_OVERFLOW_POND("water_factory_overflow_pond", "溢流池", FacilityEnums.WATER_FACTORY, "5", "1");

    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private FacilityEnums parent;
    @Getter
    private String max;
    @Getter
    private String min;

    WaterFactoryPondEnum(String id, String name, FacilityEnums parent, String max, String min) {
        this.id = id;
        this.name = name;
        this.max = max;
        this.min = min;
    }
}
