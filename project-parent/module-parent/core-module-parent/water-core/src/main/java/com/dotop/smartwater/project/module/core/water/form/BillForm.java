package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 票据
 * 

 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class BillForm extends BaseForm {

	/* 主键 */
	private String id;
	/* 模板ID */
	private String tempid;
	/* 模板名称 */
	private String tempname;
	/* 业务类型 票据类型 */
	private String type;
	/* 业务ID */
	private String businessid;
	/* 次数 */
	private int frequency;
	/* 模板内容 */
	private String tempcontent;
	/* 数据内容 */
	private String datacontent;
	/* 状态（0-未打印 1-已打印 2-已作废 3-已补打 4-已删除） */
	private int status;
	/* 业主ID */
	private String ownerid;
	/* 业主编号 */
	private String userno;
	/* 业主名称 */
	private String username;
	/* 打印时间 */
	private String printTime;
	/* 处理人ID */
	private Long operauserid;
	/* 处理人 */
	private String operaname;

}
