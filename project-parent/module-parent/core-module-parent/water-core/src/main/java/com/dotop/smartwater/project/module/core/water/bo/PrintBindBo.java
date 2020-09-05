package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 打印绑定
 * 
 同print_bind
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PrintBindBo extends BaseBo {

	private String id;
	/* 企业ID */
	private String enterpriseid;
	/* 模板ID */
	private String tempid;

	/* 企业名称 */
	private String enterprisename;
	/* 模板名称 */
	private String name;
	/* 描述 */
	private String describe;
	/* 创建时间 */
	private String createTime;
	/* 绑定时间 */
	private String bindtime;

	private String designid;

	private Integer smstype;

	private String smstypename;

	private Integer isprint;

}
