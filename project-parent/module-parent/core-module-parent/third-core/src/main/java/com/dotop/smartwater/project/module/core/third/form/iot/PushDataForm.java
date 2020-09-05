package com.dotop.smartwater.project.module.core.third.form.iot;

import lombok.Data;

/**
 * 

 * @date 2018年4月16日 上午10:20:42
 * @version 1.0.0
 */
@Data
public class PushDataForm {

	private MeterInfoForm meterInfo;
	private MeterOperForm meterOper;
	private BaseForm base;
	private EcpextForm ecpext;
	private String rssi;
	private String lsnr;
	private String devEui;
	private String rxTime;

	private MeterNb meterNb;

}
