package com.dotop.smartwater.project.module.core.water.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentTradeExtraVo {
    private String extraid;
    private String detail;
    private Date ctime;
}