package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryBo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;

/**
 * 字典配置
 *

 * @date 2019年2月23日
 */
public interface IDictionaryService extends BaseService<DictionaryBo, DictionaryVo> {

	List<DictionaryVo> getByDictionaryCode(DictionaryBo dictionaryBo);

	DictionaryVo getByDictionaryId(String dictionaryId);

	@Override
	List<DictionaryVo> list(DictionaryBo dictionaryBo);

}
