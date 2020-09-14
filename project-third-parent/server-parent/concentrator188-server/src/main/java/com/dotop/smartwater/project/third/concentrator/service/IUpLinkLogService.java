package com.dotop.smartwater.project.third.concentrator.service;

import com.dotop.smartwater.project.third.concentrator.core.bo.UpLinkLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.UpLinkLogVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 上行日志获取层接口
 *
 *
 */
public interface IUpLinkLogService extends BaseService<UpLinkLogBo, UpLinkLogVo> {

    @Override
    UpLinkLogVo add(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException;

    @Override
    Pagination<UpLinkLogVo> page(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException;

    @Override
    List<UpLinkLogVo> list(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException;

    List<UpLinkLogVo> getUplinkLogDateList(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException;

    List<UpLinkLogVo> getUplinkLogMonthList(UpLinkLogBo upLinkLogBo)throws FrameworkRuntimeException;
}
