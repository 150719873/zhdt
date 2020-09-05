package com.dotop.smartwater.project.module.core.pay.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentOrderForm extends BaseForm {
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

    private String openid;
    private String payCard;
}