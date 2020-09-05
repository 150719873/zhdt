package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**

 * @date 2019/3/4.
 * 数据校准 data_calibration
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataCalibrationVo extends BaseVo{
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 发起人
	 */
	private String createBy;

	/**
	 * 发起时间
	 */
	private Date createDate;

	/**
	 * 类型
	 */
	private Integer type;

	/**
	 * 状态
	 */
	private Integer status;

}
