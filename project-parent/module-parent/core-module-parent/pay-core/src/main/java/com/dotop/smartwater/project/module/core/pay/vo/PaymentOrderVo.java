package com.dotop.smartwater.project.module.core.pay.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentOrderVo extends BaseVo {
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
}