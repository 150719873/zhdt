package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;

public interface ISettlementService {

	SettlementVo getSettlement(String enterpriseid);

	void addSettlement(SettlementBo settlementBo);

	void editSettlement(SettlementBo settlementBo);
	
	void saveTestIot(SettlementBo settlementBo);
	
	List<SettlementVo> getSettlements();

}
