package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体类
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceWarningVo extends BaseVo {

	public static final int DEVICE_WARNING_STATUS_WAIT = 0;//设备告警状态 0：待办
	public static final int DEVICE_WARNING_STATUS_FINISH = 1;//设备告警状态 1：已处理
	public static final int DEVICE_WARNING_STATUS_HANDLE = 2;//设备告警状态 2：处理中
	
	private String id;

	/** 开到位异常：0,正常、1,异常 */
	private String openException;

	/** 关到位异常：0,正常、1,异常 */
	private String closeException;

	/** 阀电流异常：0,正常、1,异常 */
	private String abnormalCurrent;

	/** 电量异常：0,正常、1,低电量 */
	private String abnormalPower;

	/** 磁暴攻击：0,正常、1,攻击 */
	private String magneticAttack;

	/** 无水异常：0,正常、1,异常 */
    private String anhydrousAbnormal;
    /** 断线异常：0,正常、1,异常 */
    private String disconnectionAbnormal;
    /** 压力异常：0,正常、1,异常 */
    private String pressureException;
    
	/** 水表id */
	private String devid;

	/** 数据记录时间 */
	private Date ctime;

	/** 处理人 */
	private String handler;

	/** 处理时间 */
	private Date handletime;

	/** 如何处理 */
	private String remark;

	/** 处理状态 1已处理 0没处理 */
	private Integer status;

	/**
	 * @description YangKe 设备告警改造添加
	 * @data 2019-11-19 11:11
	 */
	//设备编号
	private String devno;
	//设备EUI
	private String deveui;
	//通讯方式ID
	private String modeId;
	//通讯方式名称
	private String modeName;
	//设备地址
	private String address;
	//预警次数
	private Integer warningNum;	
	//告警类型
	private String warningType;
	
	//业主ID
	private String ownerid;
}
