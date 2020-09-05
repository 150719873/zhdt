package com.dotop.smartwater.project.module.api.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryBo;
import com.dotop.smartwater.project.module.core.water.form.DictionaryForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;

/**
 * 字典配置
 *

 * @date 2019年3月4日
 */
public interface IDictionaryFactory extends BaseFactory<DictionaryForm, DictionaryVo> {

	@Override
	Pagination<DictionaryVo> page(DictionaryForm dictionaryForm) ;

	@Override
	List<DictionaryVo> list(DictionaryForm dictionaryForm) ;

	@Override
	DictionaryVo get(DictionaryForm dictionaryForm) ;

	@Override
	DictionaryVo add(DictionaryForm dictionaryForm) ;

	@Override
	DictionaryVo edit(DictionaryForm dictionaryForm) ;

	@Override
	String del(DictionaryForm dictionaryForm) ;

	List<DictionaryChildVo> getChildren(DictionaryForm dictionaryForm) ;

	List<DictionaryChildVo> getWechatChildren(DictionaryForm dictionaryForm) ;

	/**
	 * 获取可以同步的字典列表
	 * 
	 * @param dictionaryForm
	 * @return
	 */
	List<DictionaryVo> syncList(DictionaryForm dictionaryForm) ;

	void sync(DictionaryForm dictionaryForm) ;
	/**
	 * 在新建水司时初始化数据字典
	 * @param dictionaryForm
	 */
	void initialize(DictionaryForm dictionaryForm) ;

	/**
	 * 复制私有的字典类别和字典类型集合到目标水司
	 */
	void copy(List<DictionaryBo> dictionaryBos, String targetEnterpriseid) ;
}
