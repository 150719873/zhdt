package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 预警规则
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmNoticeForm extends BasePipeForm {
    private String id;
    private String deviceCode;
    private String deviceName;
    private String deviceType;
    private Integer alarmNum;
    private String modelType;
    private String notifyType;
    private String notifyUserType;
    private String notifyUser;
    private String notifyUserId;
}
