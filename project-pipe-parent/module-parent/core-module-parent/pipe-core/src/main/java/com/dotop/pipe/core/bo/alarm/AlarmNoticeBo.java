package com.dotop.pipe.core.bo.alarm;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmNoticeBo extends BasePipeBo {
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
