package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DispatchParamVo extends BaseVo {
//	private SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public final static String ISNULL = "ISNULL";
	
	public final static String FORMATERROR = "FORMATERROR";
	
	private String id;
	/* 流程号 */
	private String flowno;
	/* 业务系统 1-营收 2-管漏*/
	private Integer sys;
	/* 类型 1-报装 2-报修 3-巡检 */
	private Integer type; 
	/* 标题 */
	private String title;
	/* 申请人ID */
	private String userid;
	/* 申请人 */
	private String username;
	/* 申请人账号/工号 */
	private String account;
	/* 申请时间 */
	private Long atime;
	/* 审核人ID */
	private String auditorid;
	/* 审核人 */
	private String auditorname;
	/* 审核时间 */
	private String etime;
	/* 审核结果  1-true 0-false*/
	private Integer result;
	/* 审核结果文本 */
	private String resulttext;
	/* 审核结果解析文本*/
	private String rtext;
	/* 审核状态 1、未查看 2、处理中 3、已完成 */
	private Integer status;
	/* 流程状态 -1-已删除 0-已撤销 1-已申请 2-已挂起 3-处理中 4-已关闭 5-已归档 */
	private Integer flowstatus;
	/* 多个状态 */
	private String statusMultiple;
	/* 企业ID */
//	private Long enterpriseid;
	
	/* 区域ID */
	private String regionid;
	/* 区域 */
	private String region;
	/* 工单描述 */
	private String describe;
}
