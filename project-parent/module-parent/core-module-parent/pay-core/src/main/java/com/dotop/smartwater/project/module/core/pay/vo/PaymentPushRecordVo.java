package com.dotop.smartwater.project.module.core.pay.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentPushRecordVo extends BaseVo {
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