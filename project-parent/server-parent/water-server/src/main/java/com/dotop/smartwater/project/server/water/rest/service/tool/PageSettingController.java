package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IPageSettingFactory;
import com.dotop.smartwater.project.module.api.tool.IReportRoleBindFactory;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.ReportRoleBindBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.PageSettingMenuForm;
import com.dotop.smartwater.project.module.core.water.form.PageSettingReportForm;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingMenuVo;
import com.dotop.smartwater.project.module.core.water.vo.PageSettingReportVo;
import com.dotop.smartwater.project.module.core.water.vo.ReportRoleBindVo;

/**
 * 个人主页常用菜单和报表配置
 * 

 * @date 2019-04-03 14:09
 *
 */
@RestController

@RequestMapping("/pageSetting")
public class PageSettingController implements BaseController<BaseForm> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PageSettingController.class);

	@Autowired
	private IPageSettingFactory iPageSettingFactory;

	@Autowired
	private IReportRoleBindFactory iReportRoleBindFactory;

	@PostMapping(value = "/updateMenuSetting", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String updateMenuSetting(@RequestBody List<PageSettingMenuForm> list) {
		LOGGER.info(LogMsg.to("msg:", "个人常用菜单配置开始", "List<PageSettingMenuForm>", list));

		if (list == null) {
			return resp(ResultCode.Fail, "获取配置信息失败", null);
		}
		for (PageSettingMenuForm pmf : list) {
			String menuid = pmf.getMenuid();
			String userid = pmf.getUserid();
			String status = pmf.getStatus();

			VerificationUtils.string("menuid", menuid);
			VerificationUtils.string("userid", userid);
			VerificationUtils.string("status", status);
		}

		Integer count = iPageSettingFactory.updateMenuSetting(list);
		if (count == list.size()) {
			LOGGER.info(LogMsg.to("msg:", "个人常用菜单配置结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "FAIL", null);
		}
	}

	@PostMapping(value = "/getPageSettingMenus", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getPageSettingMenus() {
		LOGGER.info(LogMsg.to("msg:", "获取个人常用菜单配置开始"));

		List<PageSettingMenuVo> list = iPageSettingFactory.getPageSettingMenus();

		LOGGER.info(LogMsg.to("msg:", "获取个人常用菜单配置结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	@PostMapping(value = "/deleteMenuSetting", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String deleteMenuSetting(@RequestBody PageSettingMenuForm pageSettingMenuForm) {
		LOGGER.info(LogMsg.to("msg:", "删除个人常用菜单配置开始", "pageSettingMenuForm", pageSettingMenuForm));

		String id = pageSettingMenuForm.getId();
		VerificationUtils.string("id", id);

		Integer count = iPageSettingFactory.deleteMenuSetting(pageSettingMenuForm);
		if (count == 1) {
			LOGGER.info(LogMsg.to("msg:", "删除个人常用菜单配置结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "FAIL", null);
		}
	}

	@PostMapping(value = "/updateReportSetting", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String updateReportSetting(@RequestBody List<PageSettingReportForm> list) {
		LOGGER.info(LogMsg.to("msg:", "个人报表配置开始", "List<PageSettingReportForm>", list));

		if (list == null) {
			return resp(ResultCode.Fail, "获取配置信息异常", null);
		}
		for (PageSettingReportForm prf : list) {
			String bindid = prf.getBindid();
			String userid = prf.getUserid();
			String status = prf.getStatus();

			VerificationUtils.string("bindid", bindid);
			VerificationUtils.string("userid", userid);
			VerificationUtils.string("status", status);
		}

		Integer count = iPageSettingFactory.updateReportSetting(list);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		/*if (count == list.size()) {
			LOGGER.info(LogMsg.to("msg:", "个人报表配置结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "FAIL", null);
		}*/
	}

	@PostMapping(value = "/getPageSettingReports", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getPageSettingReports() {
		LOGGER.info(LogMsg.to("msg:", "获取报表配置开始"));

		List<PageSettingReportVo> list = iPageSettingFactory.getPageSettingReports();

		LOGGER.info(LogMsg.to("msg:", "获取报表配置结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	@PostMapping(value = "/listByRole", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String listByRole(@RequestBody ReportRoleBindBo reportRoleBindBo) {
		LOGGER.info(LogMsg.to("msg:", "获取该角色已有报表配置开始", "reportRoleBindBo ", reportRoleBindBo));

		List<ReportRoleBindVo> list = iReportRoleBindFactory.listByRole(reportRoleBindBo);

		LOGGER.info(LogMsg.to("msg:", "获取报表配置结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	@PostMapping(value = "/getRoleIdAndSysChoice", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getRoleIdAndSysChoice() {
		LOGGER.info(LogMsg.to("msg:", "获取角色ID和系统选择开始"));
		UserVo user = iPageSettingFactory.getRoleIdAndSysChoice();
		LOGGER.info(LogMsg.to("msg:", "获取角色ID和系统选择结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, user);
	}

	@PostMapping(value = "/updateSysChoice", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String updateSysChoice(@RequestBody UserForm userForm) {
		LOGGER.info(LogMsg.to("msg:", "更新用户默认系统选择开始", "UserForm", userForm));

		String syschocie = userForm.getSyschoice();
		VerificationUtils.string("syschocie", syschocie);

		Integer flag = iPageSettingFactory.updateSysChoice(userForm);
		if (flag == 1) {
			LOGGER.info(LogMsg.to("msg:", "更新用户默认系统选择结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Success, "更新用户默认系统选择失败", null);
		}
	}
}
