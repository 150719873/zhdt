package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentTradeResultDto extends BaseDto {
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