package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 流程-通知

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DispatchNoticeDto extends BaseDto {
	private String id;
	
	/*  流水号 */
	private String flowno;
	
	/*  节点序号 */
	private Integer nodeno;
	
	/*  通知ID */
	private String noticeid;
}
