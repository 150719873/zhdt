package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备批次关系表

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBatchRelatioForm extends BaseForm {
	/**主键*/
	private String id;
	/**批次ID*/
	private String batchId;
	/**批次号*/
	private String serialNumber;
	/**设备ID*/
	private String devid;
	/**水表编号*/
	private String devno;
	/**设备eui*/
	private String deveui;
	/**iccid*/
	private String iccid;
	/**imsi*/
	private String imsi;
	/**状态*/
	private Integer status;
}
