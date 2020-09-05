package com.dotop.smartwater.project.module.dao.revenue;


import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.LadderDto;
import com.dotop.smartwater.project.module.core.water.dto.LadderPriceDto;
import com.dotop.smartwater.project.module.core.water.vo.LadderPriceVo;

import java.util.List;

public interface ILadderPriceDao extends BaseDao<LadderPriceDto, LadderPriceVo> {

	void insert(LadderPriceDto lp);

	void deleteCompriseLadder(LadderDto ladderDto);

	List<LadderPriceVo> findPrices(String ladderid);

	void deleteLadderPrice(String typeid);

	void deletePrices(String ladderid);
}