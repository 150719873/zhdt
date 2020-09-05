package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备批次关系表

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBatchRelationBo extends BaseBo {
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
