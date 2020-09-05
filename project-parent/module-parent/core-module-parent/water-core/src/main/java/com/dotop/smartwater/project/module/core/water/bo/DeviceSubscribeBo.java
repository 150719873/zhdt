package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceSubscribeBo extends BaseBo {
    private Long id;
    private String devno;
    private String enterpriseid;
    private Date subscribeTime;
}