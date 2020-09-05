package com.dotop.smartwater.project.module.core.water.form;


import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentTradeOrderExtForm extends BaseForm {
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

    private List<String> tradeIds;
    private String payCard;
    private String openid;
    private String ownerid;
    private String amount;
}