package com.dotop.pipe.core.vo.alarm;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmVo extends BasePipeVo {

	// 主键
	private String alarmId;

	// 报警编号
	private String code;

	// 报警名字
	private String name;

	// 报警描述
	private String des;

	// 状态(0:异常;1:已处理)
	private String status;

	// 处理结果
	private String processResult;

	// 备注
	private String remark;

	// 报警时间

	private Date createDate;

	// 冗余字段
	@JSONField(serialize = false, deserialize = false)
	private String deviceId;

	// 设备信息
	private DeviceVo device;
	
	// 报警次数
	private Integer alarmCount;

}
