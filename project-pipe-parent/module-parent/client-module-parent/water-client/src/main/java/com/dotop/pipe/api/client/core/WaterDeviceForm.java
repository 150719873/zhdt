package com.dotop.pipe.api.client.core;

import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WaterDeviceForm extends DeviceForm {

    // 兼容管漏系统
    private String enterpriseId;
    private Integer page;
    private Integer pageSize;
}
