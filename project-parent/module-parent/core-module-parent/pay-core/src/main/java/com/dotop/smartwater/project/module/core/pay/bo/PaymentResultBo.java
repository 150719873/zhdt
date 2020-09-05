package com.dotop.smartwater.project.module.core.pay.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentResultBo extends BaseBo {
    private String payResId;

    private String payid;

    private String payNumber;

    private String amount;

    private String mode;

    private String status;

    private String description;

    private String enterpriseid;

    private Date payTime;

    private String thirdPartyNum;
    private Date createDate;
}