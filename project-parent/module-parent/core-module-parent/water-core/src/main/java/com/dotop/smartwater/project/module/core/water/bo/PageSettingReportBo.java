package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人主页常用报表配置

 * @date 2019-04-06 14:36
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageSettingReportBo extends BaseBo {
	
	private String id;
	//报表ID
	private String bindid;
	
	private String userid;
	//状态YES/NO
	private String status;
}
