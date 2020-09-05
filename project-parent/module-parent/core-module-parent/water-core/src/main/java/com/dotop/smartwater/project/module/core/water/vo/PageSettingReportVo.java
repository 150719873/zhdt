package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageSettingReportVo extends BaseVo {
	
	private String id;
	//报表ID
	private String bindid;
	
	private String userid;
	//状态YES/NO
	private String status;
	
	private String reportid;
	
	private String reportname;
}
