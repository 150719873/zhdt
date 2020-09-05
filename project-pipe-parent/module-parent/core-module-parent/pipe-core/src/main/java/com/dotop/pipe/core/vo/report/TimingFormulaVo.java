package com.dotop.pipe.core.vo.report;

import com.alibaba.fastjson.annotation.JSONField;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimingFormulaVo extends BasePipeVo {

	private String tfId;

	// 冗余字段
	@JSONField(serialize = false, deserialize = false)
	private String deviceId;

	private DeviceVo device;

	private String direction;

	private String multiple;

	private String tcId;
}
