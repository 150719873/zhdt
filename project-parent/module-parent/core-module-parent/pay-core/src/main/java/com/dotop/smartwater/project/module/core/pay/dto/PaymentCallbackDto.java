package com.dotop.smartwater.project.module.core.pay.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentCallbackDto extends BaseDto {
    private String id;
    private String payResId;
    private String mode;
    private Date createDate;
    private String enterpriseid;
    private String tradeNumber;
    private String detail;
}