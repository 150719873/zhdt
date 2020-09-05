package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.PageSettingMenuBo;
import com.dotop.smartwater.project.module.core.water.bo.PageSettingReportBo;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingMenuVo;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingReportVo;

/**
 * 个人主页常用菜单和报表配置
 * 

 * @date 2019-04-03 14:09
 *
 */

public interface IPageSettingService extends BaseService<BaseBo, BaseVo> {
	/**
	 * 更新常用菜单配置
	 * 
	 * @param list
	 * @return @
	 */
	Integer updateMenuSetting(List<PageSettingMenuBo> list);

	/**
	 * 获取常用菜单配置
	 * 
	 * @param userid
	 * @return @
	 */
	List<PageSettingMenuVo> getPageSettingMenus(String userid);

	/**
	 * 删除单个配置菜单
	 * 
	 * @param pageSettingMenuBo
	 * @return @
	 */
	Integer deleteMenuSetting(PageSettingMenuBo pageSettingMenuBo);

	/**
	 * 更新报表配置
	 * 
	 * @param list
	 * @return @
	 */
	Integer updateReportSetting(List<PageSettingReportBo> list);

	/**
	 * 获取报表配置
	 * 
	 * @param userid
	 * @return @
	 */
	List<PageSettingReportVo> getPageSettingReports(String userid);

	/**
	 * 获取用户角色ID和默认系统选择
	 * 
	 * @param userid
	 * @return @
	 */
	UserVo getRoleIdAndSysChoice(String userid);

	/**
	 * 更新用户的系统默认选择
	 * 
	 * @param user
	 * @return @
	 */
	Integer updateSysChoice(UserBo user);
}
