package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.LadderDto;
import com.dotop.smartwater.project.module.core.water.vo.LadderVo;

import java.util.List;

public interface ILadderDao extends BaseDao<LadderDto, LadderVo> {
	LadderVo getMaxLadder(LadderDto ladderDto);

	void insert(LadderDto ladderDto);

	void update(LadderDto ladderDto);

	List<LadderVo> getLadders(String typeid);

	void deleteLadders(String typeid);

	void delete(String id);
}