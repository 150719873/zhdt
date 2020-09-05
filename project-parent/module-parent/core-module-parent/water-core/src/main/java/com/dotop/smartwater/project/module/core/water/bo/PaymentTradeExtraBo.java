package com.dotop.smartwater.project.module.core.water.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentTradeExtraBo {
    private String extraid;
    private String detail;
    private Date ctime;
}