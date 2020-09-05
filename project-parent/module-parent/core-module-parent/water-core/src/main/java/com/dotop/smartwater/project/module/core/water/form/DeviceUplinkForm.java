package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceUplinkForm extends BaseForm {
	private String id;

	private String deveui;

	private String devno;

	private String date;

	private String devid;

	private String uplinkData;

	private boolean confirmed;

	private Integer tapstatus;

	private String water;
	
	//初始用水量
	private String beginWater;

	private String rssi;

	private Double lsnr;
	//上行时间
	private Date rxtime;

	// 计量方式：默认长度为 1。00：不变，01：相对计量，02：绝对计量
	private String measureMethod;
	// 计量值：默认长度为4。单位0.01m3，比如123表示1.23 m3
	private String measureValue;
	// 计量类型：默认长度为1。 00：不变，01：霍尔，02：干簧管
	private String measureType;
	// 计量单位：认长度为1。00：不变，01：1L，02：10L，03:100L，04:1000L
	private String measureUnit;
	// NB入网间隔：默认长度为1。00：不变，01：24h，02：48h，03：72h，04：96h，05：120h
	private String networkInterval;

	//图片路径
	private String url;
	private String iccid;
	private String reason;

	/**无水异常：0,正常、1,异常*/
	private String anhydrousAbnormal;
	/**断线异常：0,正常、1,异常*/
	private String disconnectionAbnormal;
	/**压力异常：0,正常、1,异常*/
	private String pressureException;

	/**厂家代码*/
	private String factory;
	/**流速，单位升*/
	private String flowRate;
	/**管道压力，单位:Kpa*/
	private String pressure;
	/**累计工作时间，单位：小时*/
	private String totalWorkTime;
	/**管道水温，单位：度*/
	private String temperature;
	/**水表内部时钟，如:2019-09-05 22:38:15*/
	private String internalClock;
}