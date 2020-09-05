package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.api.tool.IReportRoleBindFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.ReportRoleBindBo;
import com.dotop.smartwater.project.module.core.water.form.ReportRoleBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportRoleBindVo;
import com.dotop.smartwater.project.module.service.tool.IReportRoleBindService;

@Component
public class ReportRoleBindFactoryImpl implements IReportRoleBindFactory, IAuthCasClient {

	@Autowired
	private IReportRoleBindService iReportRoleBindService;

	@Override
	public List<ReportRoleBindVo> list(ReportRoleBindForm reportRoleBindForm) {
		ReportRoleBindBo reportRoleBindBo = BeanUtils.copy(reportRoleBindForm, ReportRoleBindBo.class);
		reportRoleBindBo.setEnterpriseid(getEnterpriseid());
		return iReportRoleBindService.list(reportRoleBindBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public ReportRoleBindVo edit(ReportRoleBindForm reportRoleBindForm) {
		// 传递参数
		String roleid = reportRoleBindForm.getRoleid();
		List<String> bindidForms = reportRoleBindForm.getBindids();
		Map<String, String> bindidFormMap = new HashMap<>();
		for (String bindid : bindidForms) {
			bindidFormMap.put(bindid, bindid);
		}

		// 获取该角色现在的报表绑定
		ReportRoleBindBo reportRoleBindBo = new ReportRoleBindBo();
		reportRoleBindBo.setRoleid(reportRoleBindForm.getRoleid());
		reportRoleBindBo.setEnterpriseid(getEnterpriseid());
		List<ReportRoleBindVo> reportRoleBindVos = iReportRoleBindService.list(reportRoleBindBo);
		Map<String, ReportRoleBindVo> reportRoleBindVoMap = reportRoleBindVos.stream()
				.collect(Collectors.toMap(ReportRoleBindVo::getBindid, a -> a, (k1, k2) -> k1));

		// 过滤
		List<ReportRoleBindBo> reportRoleBindBoAdds = new ArrayList<>();
		List<ReportRoleBindBo> reportRoleBindBoDels = new ArrayList<>();
		// 从已有数据中查找传递
		for (String bindid : reportRoleBindVoMap.keySet()) {
			if (bindidFormMap.get(bindid) == null) {
				// 不存在则删除
				ReportRoleBindBo reportRoleBindBoDel = new ReportRoleBindBo();
				reportRoleBindBoDel.setBindid(bindid);
				reportRoleBindBoDel.setEnterpriseid(getEnterpriseid());
				reportRoleBindBoDel.setUserBy(getAccount());
				reportRoleBindBoDel.setCurr(getCurr());
				reportRoleBindBoDels.add(reportRoleBindBoDel);
			}
		}
		// 从传递数据中查找已有数据
		for (String bindid : bindidFormMap.keySet()) {
			if (reportRoleBindVoMap.get(bindid) == null) {
				// 不存在则新增
				ReportRoleBindBo reportRoleBindBoAdd = new ReportRoleBindBo();
				reportRoleBindBoAdd.setRoleid(roleid);
				reportRoleBindBoAdd.setBindid(bindid);
				reportRoleBindBoAdd.setEnterpriseid(getEnterpriseid());
				reportRoleBindBoAdd.setUserBy(getAccount());
				reportRoleBindBoAdd.setCurr(getCurr());
				reportRoleBindBoAdds.add(reportRoleBindBoAdd);
			}
		}

		// 过滤新增
		iReportRoleBindService.adds(reportRoleBindBoAdds);
		// 过滤删除
		iReportRoleBindService.dels(reportRoleBindBoDels);

		return null;
	}

	@Override
	public List<ReportRoleBindVo> listByRole(ReportRoleBindBo reportRoleBindBo) {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		ReportRoleBindBo rrb = new ReportRoleBindBo();
		if (!"-1".equals(user.getRoleid())) {
			rrb.setRoleid(user.getRoleid());
		}
		rrb.setEnterpriseid(operEid);
		return iReportRoleBindService.list(rrb);
	}
}
