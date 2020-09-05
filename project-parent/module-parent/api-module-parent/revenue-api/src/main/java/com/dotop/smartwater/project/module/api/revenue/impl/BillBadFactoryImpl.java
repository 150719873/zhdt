package com.dotop.smartwater.project.module.api.revenue.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.IBillBadFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.BillBadBo;
import com.dotop.smartwater.project.module.core.water.form.BillBadForm;
import com.dotop.smartwater.project.module.core.water.vo.BillBadVo;
import com.dotop.smartwater.project.module.service.revenue.IBillBadService;

/**
 * 账单对账
 * 

 * @date 2019年2月23日
 */

@Component
public class BillBadFactoryImpl implements IBillBadFactory {

	@Autowired
	private IBillBadService iBillBadService;

	@Override
	public Pagination<BillBadVo> page(BillBadForm billBadForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		BillBadBo billBadBo = new BillBadBo();
		BeanUtils.copyProperties(billBadForm, billBadBo);
		billBadBo.setEnterpriseid(operEid);
		Pagination<BillBadVo> pagination = iBillBadService.page(billBadBo);
		return pagination;
	}

	@Override
	public Pagination<BillBadVo> getBillBadPage(BillBadForm billBadForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		BillBadBo billBadBo = new BillBadBo();
		BeanUtils.copyProperties(billBadForm, billBadBo);
		billBadBo.setEnterpriseid(operEid);
		Pagination<BillBadVo> pagination = iBillBadService.getBillBadPage(billBadBo);
		return pagination;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String markBadBill(BillBadForm billBadForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		BillBadBo billBadBo = new BillBadBo();
		BeanUtils.copyProperties(billBadForm, billBadBo);
		billBadBo.setEnterpriseid(operEid);
		iBillBadService.markBadBill(billBadBo);
		return null;
	}

}
