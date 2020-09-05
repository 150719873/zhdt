package com.dotop.pipe.api.service.mark;

import com.dotop.pipe.core.bo.mark.MarkBo;
import com.dotop.pipe.core.vo.mark.MarkVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;


public interface IMarkService extends BaseService<MarkBo, MarkVo> {

    @Override
    MarkVo add(MarkBo markBo) throws FrameworkRuntimeException;

    @Override
    MarkVo get(MarkBo markBo) throws FrameworkRuntimeException;

    @Override
    Pagination<MarkVo> page(MarkBo markBo) throws FrameworkRuntimeException;

    @Override
    List<MarkVo> list(MarkBo markBo) throws FrameworkRuntimeException;

    @Override
    MarkVo edit(MarkBo markBo) throws FrameworkRuntimeException;

    @Override
    String del(MarkBo markBo) throws FrameworkRuntimeException;
}
