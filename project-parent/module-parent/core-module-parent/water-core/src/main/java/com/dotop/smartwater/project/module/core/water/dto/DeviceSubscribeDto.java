package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceSubscribeDto extends BaseDto {
    private Long id;
    private String devno;
    private String enterpriseid;
    private Date subscribeTime;
}