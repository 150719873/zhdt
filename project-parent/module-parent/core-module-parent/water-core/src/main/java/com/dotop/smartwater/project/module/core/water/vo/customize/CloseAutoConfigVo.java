package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小区欠费自动关阀配置 closeAutoConfig
 * 

 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CloseAutoConfigVo extends BaseVo {

	private String id;
	private String communityid;
	private String config;
	private String configuser;
	private String configtime;

	private String enterprise;
	private String community;

}
