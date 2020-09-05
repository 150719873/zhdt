package com.dotop.pipe.web.api.factory.brustpipe;


import com.dotop.pipe.core.form.BrustPipeForm;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IBrustPipeFactory extends BaseFactory<BrustPipeForm, BrustPipeVo> {

    @Override
    BrustPipeVo add(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException;

    @Override
    BrustPipeVo get(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException;

    @Override
    Pagination<BrustPipeVo> page(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException;

    @Override
    List<BrustPipeVo> list(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException;

    @Override
    BrustPipeVo edit(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException;

    @Override
    String del(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException;

    BrustPipeVo editStatus(BrustPipeForm brustPipeForm);
}
