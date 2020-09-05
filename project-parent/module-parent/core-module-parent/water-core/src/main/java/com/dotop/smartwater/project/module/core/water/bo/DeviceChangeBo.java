package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 绑定App-更换水表

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceChangeBo extends BaseBo{

	private String id;
	/**原设备EUI*/
	private String scDeveui;
	/**原设备水表号*/
	private String scDevno;
	/**原设备通讯方式*/
	private String scMode;
	/**原设备读数*/
	private String scWater;
	/**新设备EUI*/
	private String nwDeveui;
	/**新设备水表编号*/
	private String nwDevno;
	/**新设备读数*/
	private Double nwWater;
	/**用户编号*/
	private String userno;
	/**用户名称*/
	private String username;
	/**地址*/
	private String useraddr;
	/**原因*/
	private String reason;
	/**换表人ID*/
	private String createById;
}
