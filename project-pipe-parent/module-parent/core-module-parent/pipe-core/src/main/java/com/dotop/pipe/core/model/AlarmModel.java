package com.dotop.pipe.core.model;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 报警(TODO:报警阈值和报警值根据模型生成,报警状态具体状态可根据模型设计)
@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmModel extends BaseEnterpriseModel {

	// 主键
	private String alarmId;

	// 报警编号
	private String code;

	// 报警名字
	private String name;

	// 报警描述
	private String des;

	// 设备主键
	private String deviceId;

	// 状态(0:异常;1:已处理)
	private Integer status;

	// 处理结果
	private String processResult;

	// 备注
	private String remark;

}
