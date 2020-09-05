package com.dotop.smartwater.project.module.core.third.bo.pay;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentOrderBo extends BaseBo {
    private String tradeNumber;
    private String tradeName;
    private String amount;
    private String mode;
    private String extra;
    private String callbackUrl;
    private String openid;
    private String payCard;
}