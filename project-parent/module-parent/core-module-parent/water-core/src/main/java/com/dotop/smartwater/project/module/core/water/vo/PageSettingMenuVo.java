package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人主页常用菜单配置

 * @date 2019-04-03 14:03
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageSettingMenuVo extends BaseVo {
	private String id;
	
	private String menuid;
	
	private String userid;
	
	private String status;
	//菜单名称
	private String name;
	//菜单路径
	private String uri;
}
