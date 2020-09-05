package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
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
public class PrintBindVo extends BaseVo {

	private String id;
	/* 企业ID */
	private String enterpriseid;
	/* 模板ID */
	private String tempid;
	
	private String typename;
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

	private String designprintid;

	private Integer printstatus;

}
