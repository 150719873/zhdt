package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description: 票据

 * @create: 2019-02-26 15:18
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class BillVo extends BaseVo {
	/* 主键 */
	private String id;
	/* 模板ID */
	private String tempid;
	/* 业务类型 */
	private String type;
	/* 模板名称 */
	private String tempname;
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
	private String operauserid;
	/* 处理人 */
	private String operaname;
	/* 企业ID */
	private String enterpriseid;
}
