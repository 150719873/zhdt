package com.dotop.pipe.web.api.factory.mark;

import com.dotop.pipe.core.form.MarkForm;
import com.dotop.pipe.core.vo.mark.MarkVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IMarkFactory extends BaseFactory<MarkForm, MarkVo> {

    @Override
    MarkVo add(MarkForm markForm) throws FrameworkRuntimeException;

    MarkVo share(MarkForm markForm) throws FrameworkRuntimeException;

    @Override
    MarkVo get(MarkForm markForm) throws FrameworkRuntimeException;

    @Override
    Pagination<MarkVo> page(MarkForm markForm) throws FrameworkRuntimeException;

    @Override
    List<MarkVo> list(MarkForm markForm) throws FrameworkRuntimeException;

    @Override
    MarkVo edit(MarkForm markForm) throws FrameworkRuntimeException;

    @Override
    String del(MarkForm markForm) throws FrameworkRuntimeException;
}
