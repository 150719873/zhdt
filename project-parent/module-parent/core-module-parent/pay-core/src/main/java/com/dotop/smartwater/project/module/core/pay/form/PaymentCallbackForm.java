package com.dotop.smartwater.project.module.core.pay.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentCallbackForm extends BaseForm {
    private String id;
    private String payResId;
    private String mode;
    private Date createDate;
    private String enterpriseid;
    private String tradeNumber;
    private String detail;
}