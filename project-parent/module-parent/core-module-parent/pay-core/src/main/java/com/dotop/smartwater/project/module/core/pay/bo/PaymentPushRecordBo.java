package com.dotop.smartwater.project.module.core.pay.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentPushRecordBo extends BaseBo {
    private String pushId;
    private String payid;
    private Integer times;
    private Date ctime;
    private Date pushTime;
    private String status;
    private String pushData;
    private String enterpriseid;
    private String pushUrl;
}