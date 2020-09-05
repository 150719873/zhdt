package com.dotop.smartwater.project.module.core.third.form.iot;

import lombok.Data;


/**

 */
@Data
public class BaseForm {

	private String actDevtyp;// 设备类型
	private String actDevmod;// 设备型号
	private String actDevver;// 版本信息

	/** SIM卡卡号*/
	private String iccid;
}
