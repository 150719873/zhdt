package com.dotop.smartwater.project.module.core.water.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentTradeExtraDto {
    private String extraid;
    private String detail;
    private Date ctime;
}