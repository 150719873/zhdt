package com.dotop.pipe.core.dto.alarm;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmNoticeDto extends BasePipeDto {

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
