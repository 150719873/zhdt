package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程-通知

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DispatchNoticeBo extends BaseBo {
	private String id;
	
	/*  流水号 */
	private String flowno;
	
	/*  节点序号 */
	private Integer nodeno;
	
	/*  通知ID */
	private String noticeid;
}
