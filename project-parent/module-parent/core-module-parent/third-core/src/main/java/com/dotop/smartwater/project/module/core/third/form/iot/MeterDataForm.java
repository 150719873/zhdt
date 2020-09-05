package com.dotop.smartwater.project.module.core.third.form.iot;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MeterDataForm extends BaseForm {

	/**设备EUI*/
	private String deveui;
	/**水表号*/
	private String devno;
	/**最新读数*/
	private String meter;
	/**令牌*/
	private String token;
	/**用户ID*/
	private String userid;

	/**通讯方式*/
	private String mode;
	/**识别码，供应商为 NB-移动为必填*/
	private String imsi;
	/**版本信息*/
	private String version;
	/**设备类型 1带阀 0不带阀*/
	private Integer tapType;
	/**开关阀状态 1开 0关*/
	private Integer tapStatus;
	/**洗阀日期*/
	private Integer tapcycle;
	/**电量*/
	private Integer battery;
	/**信号强度*/
	private Long rssi;
	/**口径*/
	private String caliber;
	/**月份*/
	private String month;
	/**上行时间*/
	private String uplinktime;
	/**备注*/
	private String remark;

	/** 批次号*/
	private String batchNumber;
	/**下发绑定参数ID*/
	private String deviceParId;
	/**产品ID*/
	private String productId;
	
	/**NFC初始密码*/
	private String nfcComPwd;
	/**NFC通讯密码*/
	private String nfcInitPwd;
	/**NFC-tag*/
	private String nfcTag;

	/**计量单位*/
	private String unit;
	/**传感器类型*/
	private String sensorType;
	/**绑定方式  生产：product 样品:sample*/
	private String bindType;
}
