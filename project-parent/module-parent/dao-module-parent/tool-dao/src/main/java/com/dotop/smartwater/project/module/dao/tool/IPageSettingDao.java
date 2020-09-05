package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.auth.dto.UserDto;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.dto.PageSettingMenuDto;
import com.dotop.smartwater.project.module.core.water.dto.PageSettingReportDto;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingMenuVo;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 个人主页常用菜单和报表配置
 *

 * @date 2019-04-03 14:09
 */
public interface IPageSettingDao extends BaseDao<BaseDto, BaseVo> {

	List<PageSettingMenuVo> getPageSettingMenus(@Param("userid") String userid);

	Integer updateMenuSetting(PageSettingMenuDto pageSettingMenuDto);

	Integer deleteSettingMenus(PageSettingMenuDto pageSettingMenuDto);

	List<PageSettingReportVo> getPageSettingReports(@Param("userid") String userid);

	Integer updateReportSetting(PageSettingReportDto pageSettingReportDto);

	Integer deleteSettingReports(PageSettingReportDto pageSettingReportDto);

	UserVo getRoleIdAndSysChoice(@Param("userid") String userid);

	Integer updateSysChoice(UserDto user);
}
