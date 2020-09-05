package com.dotop.smartwater.project.module.core.pay.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentCallbackBo extends BaseBo {
    private String id;
    private String payResId;
    private String mode;
    private Date createDate;
    private String enterpriseid;
    private String tradeNumber;
    private String detail;
}