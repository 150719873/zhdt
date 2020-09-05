package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人主页常用菜单配置

 * @date 2019-04-03 14:03
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageSettingMenuForm extends BaseForm {
	private String id;
	
	private String menuid;
	
	private String userid;
	
	private String status;
}
