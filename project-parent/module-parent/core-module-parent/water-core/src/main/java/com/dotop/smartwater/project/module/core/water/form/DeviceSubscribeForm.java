package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceSubscribeForm extends BaseForm {
    private Long id;
    private String devno;
    private String enterpriseid;
    private Date subscribeTime;
}