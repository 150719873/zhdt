package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人主页常用菜单配置

 * @date 2019-04-03 14:03
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageSettingMenuBo extends BaseBo {
	private String id;
	
	private String menuid;
	
	private String userid;
	
	private String status;
}
