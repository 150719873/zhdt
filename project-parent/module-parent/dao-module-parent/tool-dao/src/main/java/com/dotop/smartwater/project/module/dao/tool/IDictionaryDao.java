package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DictionaryDto;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;

import java.util.List;

/**

 */
public interface IDictionaryDao extends BaseDao<DictionaryDto, DictionaryVo> {

	int isExistDictionaryCode(DictionaryDto dictionaryDto);

	List<DictionaryVo> getByDictionaryCode(DictionaryDto dictionaryDto);

	@Override
	DictionaryVo get(DictionaryDto dictionaryDto);

	@Override
	List<DictionaryVo> list(DictionaryDto dictionaryDto);

	List<DictionaryVo> listByPage(DictionaryDto dictionaryDto);

	@Override
	Integer del(DictionaryDto dictionaryDto);

	@Override
	void add(DictionaryDto dictionaryDto);

	@Override
	Integer edit(DictionaryDto dictionaryDto);

}