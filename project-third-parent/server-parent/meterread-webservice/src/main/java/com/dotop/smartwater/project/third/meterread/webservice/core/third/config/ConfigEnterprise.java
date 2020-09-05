package com.dotop.smartwater.project.third.meterread.webservice.core.third.config;

import lombok.Data;

@Data
public class ConfigEnterprise {

    public ConfigEnterprise(String enterpriseid, Integer factoryId) {
        this.enterpriseid = enterpriseid;
        this.factoryId = factoryId;
    }

    private String enterpriseid;

    private Integer factoryId;
}
