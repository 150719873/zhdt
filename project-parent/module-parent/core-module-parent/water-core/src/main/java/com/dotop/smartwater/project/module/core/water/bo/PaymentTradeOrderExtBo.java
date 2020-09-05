package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentTradeOrderExtBo extends BaseBo {
    private String id;
    private String tradeid;
    private String couponid;
    private String couponMoney;
    private String balance;
    private String actualamount;
    private String change;
    private String cost;
    private String penalty;
    private String mode;
    private String detail;

}