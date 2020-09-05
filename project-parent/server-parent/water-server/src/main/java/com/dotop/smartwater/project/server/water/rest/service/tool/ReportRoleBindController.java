package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IReportRoleBindFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.ReportRoleBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportRoleBindVo;

@RestController

@RequestMapping("/ReportRoleBind")
public class ReportRoleBindController implements BaseController<ReportRoleBindForm>, IAuthCasClient {

	@Autowired
	private IReportRoleBindFactory iReportRoleBindFactory;

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody ReportRoleBindForm reportRoleBindForm) {
		String roleid = reportRoleBindForm.getRoleid();
		VerificationUtils.string("roleid", roleid);
		List<ReportRoleBindVo> list = iReportRoleBindFactory.list(reportRoleBindForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody ReportRoleBindForm reportRoleBindForm) {
		String roleid = reportRoleBindForm.getRoleid();
		List<String> bindids = reportRoleBindForm.getBindids();
		VerificationUtils.string("roleid", roleid);
		VerificationUtils.strList("bindids", bindids);
		iReportRoleBindFactory.edit(reportRoleBindForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
}
