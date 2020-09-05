package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**

 * @date 2019/3/4.
 * 运维日志 operation_log
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OperationLogForm extends BaseForm{
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
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

	/**
	 * 是否删除
	 */
	private int isDel;

	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 流程id
	 */
	private String businessId;
}
