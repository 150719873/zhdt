package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备-附属表

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceExtDto extends BaseDto{
	/**设备ID*/
	private String devid;
	/**信号强度*/
	private Long rssi;
	/**信噪比*/
	private Double lsnr;
	/**电量*/
	private Integer battery;
	/**版本*/
	private String version;
	/**设备类型(开发者平台上传)*/
	private String actdevType;
	/**设备型号(开发者平台上传)*/
	private String actdevMod;
	/**阀电流异常：0,正常、1,异常(开发者平台上传)*/
	private Integer abnormalCurrent;
	/**时间同步：0,忽略、1,同步(开发者平台上传)*/
	private Integer timeSync;
	/**定时配置：0,取消、1,2h、2,4h、3,6h、4,12h、5,24h、6,48h、7,数值保持不变*/
	private String timeConfig;
	/**定量配置：0,取消、1,0.1t、2,0.2t、3,0.5t、4,1t、5,5t、6,10t、7,数值保持不变*/
	private String quantitativeConfig;
	/**补抄：0,没有、1,补抄水表数据*/
	private String upCopy;
	/**磁场*/
	private String tapcycle;
	/**小区局域id*/
	private String pci;
	//厂家
	private String factory;
	/**基站id*/
	private String cellId;
	/**版本信息(开发者平台上传)*/
	private String ver;
}
