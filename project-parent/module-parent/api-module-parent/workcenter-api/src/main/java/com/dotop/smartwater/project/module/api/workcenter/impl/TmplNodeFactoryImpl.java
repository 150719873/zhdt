package com.dotop.smartwater.project.module.api.workcenter.impl;

import com.dotop.smartwater.project.module.api.workcenter.ITmplNodeFactory;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterTmplNodeGraphVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplNodeBo;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterTmplNodeForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeVo;
import com.dotop.smartwater.project.module.service.workcenter.ITmplNodeService;

@Component("IWorkCenterTmplNodeFactory")
public class TmplNodeFactoryImpl implements ITmplNodeFactory, IAuthCasClient {

	@Autowired
	private ITmplNodeService iTmplNodeService;

	@Override
	public Pagination<WorkCenterTmplNodeVo> page(WorkCenterTmplNodeForm tmplNodeForm) {
		WorkCenterTmplNodeBo tmplNodeBo = BeanUtils.copy(tmplNodeForm, WorkCenterTmplNodeBo.class);
		tmplNodeBo.setEnterpriseid(getEnterpriseid());
		return iTmplNodeService.page(tmplNodeBo);
	}

	@Override
	public WorkCenterTmplNodeGraphVo graph(WorkCenterTmplNodeForm workCenterTmplNodeForm) {
		WorkCenterTmplNodeBo tmplNodeBo = BeanUtils.copy(workCenterTmplNodeForm, WorkCenterTmplNodeBo.class);
		tmplNodeBo.setEnterpriseid(getEnterpriseid());
		return iTmplNodeService.graph(tmplNodeBo);
	}

	@Override
	public WorkCenterTmplNodeVo get(WorkCenterTmplNodeForm tmplNodeForm) {
		WorkCenterTmplNodeBo tmplNodeBo = new WorkCenterTmplNodeBo();
		tmplNodeBo.setId(tmplNodeForm.getId());
		tmplNodeBo.setEnterpriseid(getEnterpriseid());
		return iTmplNodeService.get(tmplNodeBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplNodeVo add(WorkCenterTmplNodeForm tmplNodeForm) {
		// 新增数据源
		WorkCenterTmplNodeBo tmplNodeBo = BeanUtils.copy(tmplNodeForm, WorkCenterTmplNodeBo.class);
		tmplNodeBo.setEnterpriseid(getEnterpriseid());
		tmplNodeBo.setUserBy(getName());
		tmplNodeBo.setCurr(getCurr());
		return iTmplNodeService.add(tmplNodeBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplNodeVo edit(WorkCenterTmplNodeForm tmplNodeForm) {
		// 编辑数据源
		WorkCenterTmplNodeBo tmplNodeBo = BeanUtils.copy(tmplNodeForm, WorkCenterTmplNodeBo.class);
		tmplNodeBo.setEnterpriseid(getEnterpriseid());
		tmplNodeBo.setUserBy(getName());
		tmplNodeBo.setCurr(getCurr());
		iTmplNodeService.edit(tmplNodeBo);
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterTmplNodeForm tmplNodeForm) {
		WorkCenterTmplNodeBo tmplNodeBo = new WorkCenterTmplNodeBo();
		tmplNodeBo.setId(tmplNodeForm.getId());
		tmplNodeBo.setEnterpriseid(getEnterpriseid());
		iTmplNodeService.del(tmplNodeBo);
		return null;
	}

}
