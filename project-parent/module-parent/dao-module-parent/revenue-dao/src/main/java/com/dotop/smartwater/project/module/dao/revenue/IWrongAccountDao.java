package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WrongAccountDto;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;

import java.util.List;

public interface IWrongAccountDao extends BaseDao<WrongAccountDto, WrongAccountVo> {

	@Override
	List<WrongAccountVo> list(WrongAccountDto wrongAccountDto);

	@Override
	WrongAccountVo get(WrongAccountDto wrongAccountDto);

	boolean isexist(WrongAccountDto wrongAccountDto);

	@Override
	void add(WrongAccountDto wrongAccountDto);

	void update(WrongAccountDto wrongAccountDto);

	List<WrongAccountVo> getStatus(WrongAccountDto wrongAccountDto);
}