package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 原始数据
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OriginalVo extends BaseVo {

	private String id;
	private String deveui;
	private String userno;
	private String username;
	private String caliber;
	private String mode;
	private String devno;
	private String water;
	private String phone;
	private String status;
	private String rxtime;
	private String times;
	private String rssi;
	private Double lsnr;
	private String monthDay;

	private String modeName;
	
	/**
	 * 水表调整 meterOper
	 * 

	 * @date 2019-01-25
	 */
	private String measureMethod;
	private String measureValue;
	private String measureType;
	private String measureUnit;
	private String networkInterval;
	private String upLinkData;
	
    /**
          * 新增字段 图片路径
     * @author YangKe
     * @date 2019-10-28
     */
	private String url;
	//是否带阀 1:带阀  0:不带阀
	private Integer tapType;
	private Integer tapStatus;

	private String iccid;
	private String reason;
	
	//BaseVo 属性
	private String actDevmod;
	private String actDevtyp;
	private String actDevver;
	//ecpext 属性
	private String upCount;
	private String downRssi;
	//meterIInfoVo 属性
	private String wmid;
	private String waterConsumption;
	private String abnormalCurrent;
	private String timeSync;
	private String abnormalPower;
	private String magneticAttack;
	private String serialAbnormal;
	private String resetType;
    //生命状态：0，初始状态、1，贮存状态、2，运行状态、3，报废状态
	private String lifeStatus;
	private String time;
	private String resetPeriod;
	//meterNb 属性
	private String signalPower;
	private String totalPower;
	private String txPower;
	private String txTime;
	private String cellId;
	private String ecl;
	private String snr;
	private String earfcn;
	private String pci;
	private String rsrq;
	private String operatorMode;
	private String imsi;
	private String ver;
}
