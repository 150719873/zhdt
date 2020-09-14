package com.dotop.smartwater.project.third.concentrator.service;

import com.dotop.smartwater.project.third.concentrator.core.vo.DownLinkTaskLogVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.concentrator.core.bo.DownLinkTaskLogBo;

/**
 * 下发任务日志获取层接口
 *
 *
 */
public interface IDownLinkTaskLogService extends BaseService<DownLinkTaskLogBo, DownLinkTaskLogVo> {

    @Override
    DownLinkTaskLogVo add(DownLinkTaskLogBo downLinkTaskLogBo) throws FrameworkRuntimeException;

    @Override
    DownLinkTaskLogVo edit(DownLinkTaskLogBo downLinkTaskLogBo) throws FrameworkRuntimeException;

    @Override
    DownLinkTaskLogVo get(DownLinkTaskLogBo downLinkTaskLogBo) throws FrameworkRuntimeException;

    /**
     * 获取下发任务列表并分页
     * @param downLinkTaskLogBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    Pagination<DownLinkTaskLogVo> page(DownLinkTaskLogBo downLinkTaskLogBo) throws FrameworkRuntimeException;
}
