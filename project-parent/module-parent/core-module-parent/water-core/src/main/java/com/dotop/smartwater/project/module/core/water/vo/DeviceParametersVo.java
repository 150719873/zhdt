package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceParametersVo extends BaseVo {

	/**
	 * 设备参数配置id
	 */

	private String deviceParId;

	/**
	 * 批次号
	 */
	private String serialNumber;

	/**
	 * 设备类型名称
	 */
	private String deviceName;

	/**
	 * 设备通讯方式 1LORA 2移动NB 3电信NB 4-全网通
	 */
	private DictionaryChildVo mode;

	/**通讯方式-名称*/
	private String modeName;
	
	/**
	 * 阀门类型 阀门类型（0 不带 1 带阀）
	 */
	private String valveType;

	/**阀门类型*/
	private String valveTypeName;
	
	/**
	 * 阀门状态(0 关阀 1 开阀)
	 */
	private Integer valveStatus;
	
	/**
	 * 阀门状态
	 */
	private String valveStatusName;

	/**
	 * 计量单位(10L 100L .. )
	 */
	private DictionaryChildVo unit;
	
	/**计量单位名称*/
	private String unitName;
	
	/**
	 * 设备参数配置id
	 */
	private DictionaryChildVo sensorType;
	
	/**
	 * 传感器名称
	 */
	private String sensorTypeName;
	
	/**
	 * 设备参数类型 状态(0 停用 1 启用)
	 */
	private Integer status;

	/**产品ID*/
	private String productId;
	/**产品名称*/
	private String productName;
	/**口径*/
	private String caliber;
	/**nfc初始密码*/
	private String nfcInitPwd;
	/**通讯密码*/
	private String nfcComPwd;
	
}
