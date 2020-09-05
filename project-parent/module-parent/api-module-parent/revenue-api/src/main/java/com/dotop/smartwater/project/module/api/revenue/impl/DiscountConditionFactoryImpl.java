package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.IDiscountConditionFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.ConditionBo;
import com.dotop.smartwater.project.module.core.water.form.ConditionForm;
import com.dotop.smartwater.project.module.core.water.vo.ConditionVo;
import com.dotop.smartwater.project.module.service.revenue.IDiscountConditionService;

@Component
public class DiscountConditionFactoryImpl implements IDiscountConditionFactory {

	@Autowired
	private IDiscountConditionService iDiscountConditionServcie;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void savecondition(ConditionForm conditionForm) throws FrameworkRuntimeException {
		Date curr = AuthCasClient.getCurr();

		ConditionBo conditionBo = new ConditionBo();
		BeanUtils.copyProperties(conditionForm, conditionBo);

		conditionBo.setCreatetime(DateUtils.formatDatetime(curr));
		iDiscountConditionServcie.savecondition(conditionBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void editcondition(ConditionForm conditionForm) throws FrameworkRuntimeException {
		ConditionBo conditionBo = new ConditionBo();
		BeanUtils.copyProperties(conditionForm, conditionBo);

		iDiscountConditionServcie.editcondition(conditionBo);
	}

	@Override
	public ConditionVo getCondition(ConditionForm conditionForm) throws FrameworkRuntimeException {
		ConditionBo conditionBo = new ConditionBo();
		conditionBo.setId(conditionForm.getId());

		ConditionVo conditionVo = iDiscountConditionServcie.getCondition(conditionBo);
		return conditionVo;
	}

	@Override
	public void deleteCond(ConditionForm conditionForm) throws FrameworkRuntimeException {
		ConditionBo conditionBo = new ConditionBo();
		conditionBo.setId(conditionForm.getId());

		iDiscountConditionServcie.deleteCond(conditionBo);

	}

	@Override
	public Pagination<ConditionVo> findcondition(ConditionForm conditionForm) throws FrameworkRuntimeException {

		ConditionBo conditionBo = new ConditionBo();
		BeanUtils.copyProperties(conditionForm, conditionBo);

		Pagination<ConditionVo> pagination = iDiscountConditionServcie.findcondition(conditionBo);
		return pagination;
	}

}
