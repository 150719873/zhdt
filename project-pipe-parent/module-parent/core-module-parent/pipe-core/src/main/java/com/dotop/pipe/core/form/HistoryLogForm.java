package com.dotop.pipe.core.form;

import java.util.Date;

import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HistoryLogForm extends BasePipeForm {

	private String id;
	private String deviceId;
	private String fieldName;
	private String fieldNewVal;
	private String fieldOldVal;
	private Date createDate;
	private String createBy;
	// private String enterpriseId;
	private DeviceVo device;

}
