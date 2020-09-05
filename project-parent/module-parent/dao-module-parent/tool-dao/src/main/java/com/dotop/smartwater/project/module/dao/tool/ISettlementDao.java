package com.dotop.smartwater.project.module.dao.tool;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.project.module.core.auth.dto.SettlementDto;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public interface ISettlementDao {

	SettlementVo getSettlement(@Param("enterpriseid") String enterpriseid);

	int addSettlement(SettlementDto settlement);

	int editSettlement(SettlementDto settlement);
	
	int saveTestIot(SettlementDto settlement);
	
	List<SettlementVo> getSettlements();
}
