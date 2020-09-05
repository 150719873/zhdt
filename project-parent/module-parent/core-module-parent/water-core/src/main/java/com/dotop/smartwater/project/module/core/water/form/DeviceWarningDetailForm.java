package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @description 设备预警详情
 * @date 2019-11-19 11:18
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceWarningDetailForm extends BaseForm {
	
	//主键
	private String id;
	//设备预警主键
	private String warningId;
	//开到位异常 0,正常 1,异常
	private String openException;
	//关到位异常 0,正常 1,异常
	private String closeException;
	//阀电流异常 0,正常 1,异常
	private String abnormalCurrent;
	//电量异常 0,正常 1,异常
	private String abnormalPower;
	//磁暴攻击 0,正常 1,异常
	private String magneticAttack;

	/** 无水异常：0,正常、1,异常 */
    private String anhydrousAbnormal;
    /** 断线异常：0,正常、1,异常 */
    private String disconnectionAbnormal;
    /** 压力异常：0,正常、1,异常 */
    private String pressureException;

	//预警时间
	private Date warningTime;
}
