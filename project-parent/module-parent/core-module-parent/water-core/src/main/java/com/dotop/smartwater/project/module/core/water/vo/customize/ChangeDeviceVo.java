package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 换表记录查询 ChangeDeviceExportCreator
 * 

 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ChangeDeviceVo extends BaseVo {

	private String id;

	private String devno;

	private String olddevno;

	private String operateuser;

	private String operatetime;

	private Double olddata;

	private Double olddatabegin;

	private String reason;

	private String community;

	private String owner;

}
