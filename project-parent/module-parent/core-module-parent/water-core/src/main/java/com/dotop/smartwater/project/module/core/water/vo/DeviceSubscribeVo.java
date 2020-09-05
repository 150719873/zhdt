package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceSubscribeVo extends BaseVo {
    private Long id;
    private String devno;
    private String enterpriseid;
    private Date subscribeTime;
}