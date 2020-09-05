package com.dotop.pipe.api.service.brustpipe;

import com.dotop.pipe.core.bo.brustpipe.BrustPipeBo;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 爆管管理
 *
 */
public interface IBrustPipeService extends BaseService<BrustPipeBo, BrustPipeVo> {


    @Override
    BrustPipeVo add(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException;

    @Override
    BrustPipeVo get(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException;

    @Override
    Pagination<BrustPipeVo> page(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException;

    @Override
    List<BrustPipeVo> list(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException;

    @Override
    BrustPipeVo edit(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException;

    @Override
    String del(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException;

    BrustPipeVo editStatus(BrustPipeBo brustPipeBo);


}
