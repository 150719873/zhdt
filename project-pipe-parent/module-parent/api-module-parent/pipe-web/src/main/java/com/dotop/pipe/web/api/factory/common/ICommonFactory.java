package com.dotop.pipe.web.api.factory.common;

import java.util.Map;

import com.dotop.pipe.core.form.DictionaryForm;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 */
public interface ICommonFactory {

	/**
	 * 根据字典类型 查询字典
	 * 
	 * @param type
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	DictionaryVo getByType(String type) throws FrameworkRuntimeException;

	/**
	 * 新增字典
	 * 
	 * @param dictionaryForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	DictionaryVo add(DictionaryForm dictionaryForm) throws FrameworkRuntimeException;

	/**
	 * 获取字典分页
	 * 
	 * @param dictionaryForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<DictionaryVo> page(DictionaryForm dictionaryForm) throws FrameworkRuntimeException;

	/**
	 * 删除字典
	 * 
	 * @param dictionaryForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	String del(DictionaryForm dictionaryForm) throws FrameworkRuntimeException;

	/**
	 * 根据设备类型查找最大的编码
	 * 
	 * @param type
	 * @return
	 */
	Map<String, String> getMaxCode(Map<String, Object> queryParams);

}
