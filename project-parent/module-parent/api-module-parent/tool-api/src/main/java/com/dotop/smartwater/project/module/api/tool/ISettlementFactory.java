package com.dotop.smartwater.project.module.api.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.auth.form.SettlementForm;
import com.dotop.smartwater.project.module.core.auth.sms.TemplateType;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;

public interface ISettlementFactory extends BaseFactory<SettlementForm, SettlementVo> {

	SettlementVo getSettlement(String enterpriseid);

	void addSettlement(SettlementForm settlementForm);
	
	void editSettlement(SettlementForm settlementForm);

	void saveTestIot(SettlementForm settlementForm);
	
	List<TemplateType> codeTypeList();
}
