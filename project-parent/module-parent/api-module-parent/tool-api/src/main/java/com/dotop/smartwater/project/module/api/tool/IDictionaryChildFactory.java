package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DictionaryChildForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;

/**
 * 字典配置
 *

 * @date 2019年3月4日
 */
public interface IDictionaryChildFactory extends BaseFactory<DictionaryChildForm, DictionaryChildVo> {

    @Override
    Pagination<DictionaryChildVo> page(DictionaryChildForm dictionaryChildForm);

    @Override
    DictionaryChildVo get(DictionaryChildForm dictionaryChildForm) ;

    @Override
    DictionaryChildVo add(DictionaryChildForm dictionaryChildForm) ;

    @Override
    DictionaryChildVo edit(DictionaryChildForm dictionaryChildForm) ;

    @Override
    String del(DictionaryChildForm dictionaryChildForm) ;



}
