package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.tool.IPageSettingFactory;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.PageSettingMenuBo;
import com.dotop.smartwater.project.module.core.water.bo.PageSettingReportBo;
import com.dotop.smartwater.project.module.core.water.bo.ReportRoleBindBo;
import com.dotop.smartwater.project.module.core.water.form.PageSettingMenuForm;
import com.dotop.smartwater.project.module.core.water.form.PageSettingReportForm;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingMenuVo;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingReportVo;
import com.dotop.smartwater.project.module.core.water.vo.ReportRoleBindVo;
import com.dotop.smartwater.project.module.service.tool.IPageSettingService;
import com.dotop.smartwater.project.module.service.tool.IReportRoleBindService;

/**
 * 个人主页常用菜单和报表配置
 * 

 * @date 2019-04-03 14:09
 *
 */
@Component
public class PageSettingFactoryImpl implements IPageSettingFactory {

	@Autowired
	private IPageSettingService iPageSettingService;
	@Autowired
	private IReportRoleBindService iReportRoleBindService;

	@Override
	public Integer updateMenuSetting(List<PageSettingMenuForm> list) {
		AuthCasClient.getUser();
		List<PageSettingMenuBo> menuList = new ArrayList<>();

		for (PageSettingMenuForm psmf : list) {
			PageSettingMenuBo pageSettingMenuBo = new PageSettingMenuBo();
			BeanUtils.copyProperties(psmf, pageSettingMenuBo);
			pageSettingMenuBo.setId(UuidUtils.getUuid());
			menuList.add(pageSettingMenuBo);
		}

		return iPageSettingService.updateMenuSetting(menuList);
	}

	@Override
	public List<PageSettingMenuVo> getPageSettingMenus() {
		UserVo user = AuthCasClient.getUser();

		return iPageSettingService.getPageSettingMenus(user.getUserid());
	}

	@Override
	public Integer deleteMenuSetting(PageSettingMenuForm pageSettingMenuForm) {
		AuthCasClient.getUser();

		PageSettingMenuBo pageSettingMenuBo = new PageSettingMenuBo();
		BeanUtils.copyProperties(pageSettingMenuForm, pageSettingMenuBo);

		return iPageSettingService.deleteMenuSetting(pageSettingMenuBo);
	}

	@Override
	public Integer updateReportSetting(List<PageSettingReportForm> list) {
		AuthCasClient.getUser();
		List<PageSettingReportBo> reportList = new ArrayList<>();

		for (PageSettingReportForm psrf : list) {
			PageSettingReportBo pageSettingReportBo = new PageSettingReportBo();
			BeanUtils.copyProperties(psrf, pageSettingReportBo);
			pageSettingReportBo.setId(UuidUtils.getUuid());
			reportList.add(pageSettingReportBo);
		}

		return iPageSettingService.updateReportSetting(reportList);
	}

	@Override
	public List<PageSettingReportVo> getPageSettingReports() {
		UserVo user = AuthCasClient.getUser();

		String operEid = user.getEnterpriseid();
		ReportRoleBindBo rrb = new ReportRoleBindBo();
		if (!"-1".equals(user.getRoleid())) {
			rrb.setRoleid(user.getRoleid());
		}
		rrb.setEnterpriseid(operEid);
		List<ReportRoleBindVo> roleBindVolist = iReportRoleBindService.list(rrb);
		List<PageSettingReportVo> reportVoList = iPageSettingService.getPageSettingReports(user.getUserid());

		if (roleBindVolist == null || roleBindVolist.isEmpty()) {
			return new ArrayList<>();
		}

		List<PageSettingReportVo> list = new ArrayList<>();
		Map<String, ReportRoleBindVo> roleBindVoMap = new HashMap<>();
		for (ReportRoleBindVo reportRoleBindVo : roleBindVolist) {
			roleBindVoMap.put(reportRoleBindVo.getBindid(), reportRoleBindVo);
		}
		for (PageSettingReportVo pageSettingReportVo : reportVoList) {
			if (roleBindVoMap.containsKey(pageSettingReportVo.getBindid())) {
				list.add(pageSettingReportVo);
			}
		}
		return list;
	}

	@Override
	public UserVo getRoleIdAndSysChoice() {
		UserVo user = AuthCasClient.getUser();

		return iPageSettingService.getRoleIdAndSysChoice(user.getUserid());
	}

	@Override
	public Integer updateSysChoice(UserForm userForm) {
		UserVo user = AuthCasClient.getUser();
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(userForm, userBo);
		userBo.setUserid(user.getUserid());

		return iPageSettingService.updateSysChoice(userBo);
	}

}
