package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentTradeResultVo extends BaseVo {
    private String transactionid;

    private String tradeid;

    private String transactionNumber;

    private String amount;

    private String mode;

    private String status;

    private String description;

    private String enterpriseid;

    private Date payTime;

    private String thirdPartyMsg;

    private Date createDate;

    private String openid;

    private String payCard;
}