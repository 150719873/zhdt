package com.dotop.smartwater.project.module.api.app.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.api.app.IAppWorkCenterProcessFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessBo;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessVo;
import com.dotop.smartwater.project.module.service.workcenter.IProcessService;

@Component("IAppWorkCenterProcessFactory")
public class AppWorkCenterProcessFactoryImpl implements IAppWorkCenterProcessFactory, IAuthCasClient {

	@Autowired
	private IProcessService iProcessService;

	@Override
	public Pagination<WorkCenterProcessVo> page(WorkCenterProcessForm processForm) throws FrameworkRuntimeException {
		List<String> statuss = processForm.getStatuss();
		List<String> userids = BeanUtils.list(getUserid());
		List<String> roleids = BeanUtils.list(getRoleid());
		WorkCenterProcessBo processBo = new WorkCenterProcessBo();
		processBo.setPage(processForm.getPage());
		processBo.setPageCount(processForm.getPageCount());
		processBo.setEnterpriseid(getEnterpriseid());
		processBo.setStatuss(statuss);
		processBo.setNextHandlers(userids);
		processBo.setNextCarbonCopyers(userids);
		processBo.setNextHandlerRoles(roleids);
		processBo.setNextCarbonCopyerRoles(roleids);
		return iProcessService.page(processBo);
	}

}
