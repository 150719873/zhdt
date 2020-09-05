package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DispatchBillVo extends BaseVo {
	private String id;
	
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
	/* 账号/工号 */
	private String account;
	/* 申请时间-时间戳 */
	private String atime;
	/* 申请时间 */
	private String atimetext;
	/* 区域ID */
	private String regionid;
	/* 区域 */
	private String region;
	/* 工单描述 */
	private String describe;
	/* 企业ID */
//	private Long enterpriseid;
}
