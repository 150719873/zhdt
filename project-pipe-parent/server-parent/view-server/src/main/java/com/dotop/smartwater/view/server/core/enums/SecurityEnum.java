package com.dotop.smartwater.view.server.core.enums;

import lombok.Getter;

/**
 * 安防类型
 */
public enum SecurityEnum {

    // 沉淀池
    WATER_FACTORY_PRECIPITATE_POND_FIRST("water_factory_precipitate_pond_first", "沉淀池一号门", WaterFactoryPondEnum.WATER_FACTORY_PRECIPITATE_POND),
    WATER_FACTORY_PRECIPITATE_POND_SECOND("water_factory_precipitate_pond_second", "沉淀池二号门", WaterFactoryPondEnum.WATER_FACTORY_PRECIPITATE_POND),

    WATER_FACTORY_MEDICINE_POND_FIRST("water_factory_medicine_pond_first", "加药池一号门", WaterFactoryPondEnum.WATER_FACTORY_MEDICINE_POND),
    WATER_FACTORY_MEDICINE_POND_SECOND("water_factory_medicine_pond_second", "加药池二号门", WaterFactoryPondEnum.WATER_FACTORY_MEDICINE_POND),

    WATER_FACTORY_FILTER_POND_FIRST("water_factory_filter_pond_first", "过滤池一号门", WaterFactoryPondEnum.WATER_FACTORY_FILTER_POND),
    WATER_FACTORY_FILTER_POND_SECOND("water_factory_filter_pond_second", "过滤池二号门", WaterFactoryPondEnum.WATER_FACTORY_FILTER_POND),

    WATER_FACTORY_HIGH_PUMP_POND("water_factory_high_pump_pond", "高压泵池", WaterFactoryPondEnum.WATER_FACTORY_HIGH_PUMP_POND),
    WATER_FACTORY_DOUBLE_VALVE_FILTER_POND("water_factory_double_valve_filter_pond", "双阀滤池", WaterFactoryPondEnum.WATER_FACTORY_DOUBLE_VALVE_FILTER_POND),
    WATER_FACTORY_SHIMIZU_POND("water_factory_shimizu_pond", "清水池", WaterFactoryPondEnum.WATER_FACTORY_SHIMIZU_POND),
    WATER_FACTORY_TUBE_FILTER_POND("water_factory_tube_filter_pond", "协管滤池", WaterFactoryPondEnum.WATER_FACTORY_TUBE_FILTER_POND),
    WATER_FACTORY_REACTION_POND("water_factory_reaction_pond", "反应池", WaterFactoryPondEnum.WATER_FACTORY_REACTION_POND),
    WATER_FACTORY_OVERFLOW_POND("water_factory_overflow_pond", "溢流池", WaterFactoryPondEnum.WATER_FACTORY_OVERFLOW_POND);

    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private WaterFactoryPondEnum parent;

    SecurityEnum(String id, String name, WaterFactoryPondEnum parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }
    
}
