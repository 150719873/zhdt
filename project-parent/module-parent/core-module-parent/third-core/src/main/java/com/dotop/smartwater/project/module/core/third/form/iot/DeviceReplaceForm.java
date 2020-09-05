package com.dotop.smartwater.project.module.core.third.form.iot;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 更换水表参数接收

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceReplaceForm {
	
	/**原设备EUI*/
	private String scDeveui;
	/**用户编号*/
	private String userNo;
	/**新设备EUI*/
	private String nwDeveui;
	/**新设备读数*/
	private Double nwWater;
	/**更换原因*/
	private String reason;
	
	/**NFC初始密码*/
	private String nfcComPwd;
	/**NFC通讯密码*/
	private String nfcInitPwd;
	/**NFC-tag*/
	private String nfcTag;
	/**识别码，供应商为 NB-移动为必填*/
	private String imsi;
	
	
}
