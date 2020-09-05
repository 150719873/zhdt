package com.dotop.smartwater.project.module.core.third.form.iot;

import lombok.Data;

/**
 * 

 * @date 2019年03月30日 上午10:08:04
 * @version 2.1.0 增加生命状态
 */
@Data
public class MeterInfoForm {

	private String waterConsumption;// 用水量：单位 m3
	private String valveStatus;// 阀门状态：0开启，1关闭
	private String openException;// 开到位异常：0,正常、1,异常
	private String closeException;// 关到位异常：0,正常、1,异常
	private String abnormalCurrent;// 阀电流异常：0,正常、1,异常
	private String timeSync;// 时间同步：0,忽略、1,同步
	private String abnormalPower;// 电量异常：0,正常、1,异常
	private String magneticAttack;// 磁暴攻击：0,正常、1,攻击

	private String serialAbnormal;

	private String resetType;

	// 生命状态：0，初始状态、1，贮存状态、2，运行状态、3，报废状态
	private Integer lifeStatus;

	// 水表口径
	private String caliber;

	// 水表ID
	private String wmid;

	private String time;

	private String resetPeriod;

	/**上报原因*/
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
