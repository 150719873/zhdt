package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批次管理

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBatchVo extends BaseVo {
	/**批次号ID*/
	private String id;
	/**批次号*/
	private String serialNumber;
	/**起始日期*/
	private String startTime;
	/**截止日期*/
	private String endTime;
	/**数量*/
	private Integer quantity;
	/**剩余数量*/
	private Integer surplus;
	/**状态（0-未开始 1-生产中 2-已结束）*/
	private String status;
	/**说明*/
	private String remark;
	/**设备绑定参数ID*/
	private String deviceParId;
	/**绑定参数名称*/
	private String deviceName;
	/**通讯方式*/
	private String mode;
	/**是否带阀*/
	private String taptype;
	/**计量单位*/
	private String unit;
	/**传感器类型*/
	private String sensorType;
	/**产品ID*/
	private String productId;
	/**产品编号*/
	private String productNo;
	/**产品名称*/
	private String productName;
	/**产品型号*/
	private String productModel;
	/**口径*/
	private String productCaliber;
	/**生产厂家*/
	private String productVender;
}
