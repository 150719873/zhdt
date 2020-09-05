package com.dotop.pipe.core.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceUpDownStreamForm extends BasePipeForm {

	private String id;
	private String deviceId;
	private List<String> parentDeviceIds;
	private String type;
	private String areaId;
	private String name;
	private String code;
	private String alarmProperties;
}
