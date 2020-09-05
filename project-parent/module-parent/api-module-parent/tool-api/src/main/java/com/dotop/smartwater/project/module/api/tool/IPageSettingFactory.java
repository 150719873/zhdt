package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.PageSettingMenuForm;
import com.dotop.smartwater.project.module.core.water.form.PageSettingReportForm;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingMenuVo;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingReportVo;

import java.util.List;

/**
 * 个人主页常用菜单和报表配置
 * 

 * @date 2019-04-03 14:09
 *
 */
public interface IPageSettingFactory extends BaseFactory<BaseForm, BaseVo> {
	/**
	 * 更新常用菜单配置
	 * 
	 * @param list
	 * @return @
	 */
	Integer updateMenuSetting(List<PageSettingMenuForm> list);

	/**
	 * 获取常用菜单配置
	 *
	 * @return @
	 */
	public List<PageSettingMenuVo> getPageSettingMenus();

	/**
	 * 删除单个配置菜单
	 * 
	 * @param pageSettingMenuForm
	 * @return @
	 */
	Integer deleteMenuSetting(PageSettingMenuForm pageSettingMenuForm);

	/**
	 * 更新报表配置
	 * 
	 * @param list
	 * @return @
	 */
	Integer updateReportSetting(List<PageSettingReportForm> list);

	/**
	 * 获取报表配置
	 * @return @
	 */
	List<PageSettingReportVo> getPageSettingReports();

	/**
	 * 获取用户角色ID和默认系统选择
	 * 
	 * @return @
	 */
	UserVo getRoleIdAndSysChoice();

	/**
	 * 更新用户系统默认选择
	 * 
	 * @param userForm
	 * @return @
	 */
	Integer updateSysChoice(UserForm userForm);
}
