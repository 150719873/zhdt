package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**

 * @date 2019/2/25.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceDownlinkVo extends BaseVo {
	private String id;

	private String deveui;

	private String devid;

	private String downlinkdata;

	private String tagid;

	private String tagvalue;

	private boolean confirmed;

	private boolean rx2;

	private String clientid;

	private Integer status;

	private Date gentime;

	private Date txtime;
	
	private String reason;
	/**
	 * 请求数据开发者平台
	 *

	 * @date 2019-01-25
	 */
	private String reqData;

	private String userid;

	private String name;

	private String devno;

	private String measurementMethods;

	private String measurementValue;

	private String measurementType;

	private String measurementUnit;

	private String networkInterval;

	private String life;

	private String period;

	private String expire;

	private String mode;

	private String modeCode;
	
	private String modeName;
	
	private String caliber;
}
