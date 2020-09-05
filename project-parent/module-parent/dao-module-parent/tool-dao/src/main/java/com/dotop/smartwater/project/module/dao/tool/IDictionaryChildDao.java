package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DictionaryChildDto;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;

import java.util.List;

public interface IDictionaryChildDao extends BaseDao<DictionaryChildDto, DictionaryChildVo> {

	void deleteByDictionaryId(DictionaryChildDto dictionaryChildDto);

	@Override
	void add(DictionaryChildDto dictionaryChildDto);

	@Override
	Integer edit(DictionaryChildDto dictionaryChildDto);

	@Override
	Integer del(DictionaryChildDto dictionaryChildDto);

	@Override
	List<DictionaryChildVo> list(DictionaryChildDto dictionaryChildDto);

	@Override
	Boolean isExist(DictionaryChildDto dictionaryChildDto);

	@Override
	DictionaryChildVo get(DictionaryChildDto dictionaryChildDto);

}