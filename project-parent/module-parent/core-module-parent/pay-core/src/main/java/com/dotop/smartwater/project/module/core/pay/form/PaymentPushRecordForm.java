package com.dotop.smartwater.project.module.core.pay.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentPushRecordForm extends BaseForm {
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