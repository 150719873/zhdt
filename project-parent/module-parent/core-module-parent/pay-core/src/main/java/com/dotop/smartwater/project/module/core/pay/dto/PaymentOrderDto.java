package com.dotop.smartwater.project.module.core.pay.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentOrderDto extends BaseDto {
    private String payid;

    private String tradeNumber;

    private String tradeName;

    private String amount;

    private String mode;

    private String status;

    private Date payTime;

    private Date createDate;

    private String extra;

    private String callbackUrl;
}