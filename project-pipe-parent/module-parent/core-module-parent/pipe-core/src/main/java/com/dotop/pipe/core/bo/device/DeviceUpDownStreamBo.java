package com.dotop.pipe.core.bo.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 *
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceUpDownStreamBo extends BasePipeBo {

	private String id;
	private String deviceId;
	private List<String> parentDeviceIds;
	private String type;
	private String areaId;
	private String name;
	private String code;
	private String alarmProperties;
}
