package com.dotop.pipe.core.vo.alarm;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmNoticeRuleVo extends BasePipeVo {

	private String id;
	private String deviceType; 
	private Integer alarmNum; // 预警数量
	private String notifyType; // 通知类型
	private String notifyUserType; // 通知类型
	private String notifyUser;
	private String notifyUserid;
	private Integer modelType;
	private String deviceCode;
	private String deviceName;
	private Date createDate;  // 时间
}
