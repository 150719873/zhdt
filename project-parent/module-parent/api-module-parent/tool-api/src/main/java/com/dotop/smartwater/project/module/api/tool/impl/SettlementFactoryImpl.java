package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.api.tool.ISettlementFactory;
import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.form.SettlementForm;
import com.dotop.smartwater.project.module.core.auth.sms.TemplateType;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;

@Component
public class SettlementFactoryImpl implements ISettlementFactory {

	@Autowired
	private ISettlementService iSettlementService;

	@Override
	public SettlementVo getSettlement(String enterpriseid)  {
		return iSettlementService.getSettlement(enterpriseid);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addSettlement(SettlementForm settlementForm)  {
		SettlementBo settlementBo = BeanUtils.copy(settlementForm, SettlementBo.class);
		iSettlementService.addSettlement(settlementBo);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void editSettlement(SettlementForm settlementForm) {
		SettlementBo settlementBo = BeanUtils.copy(settlementForm, SettlementBo.class);
		iSettlementService.editSettlement(settlementBo);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void saveTestIot(SettlementForm settlementForm) {
		SettlementBo settlementBo = BeanUtils.copy(settlementForm, SettlementBo.class);
		iSettlementService.saveTestIot(settlementBo);
	}

	@Override
	public List<TemplateType> codeTypeList()  {
		return SmsEnum.listAll();
	}

}
