package com.dotop.smartwater.project.third.server.meterread.client2.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.server.meterread.client2.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.server.meterread.client2.core.third.vo.RemoteDataVo;

import java.util.List;

/**
 * 对接第三方系统的客户资料接口
 *
 *
 */
public interface IThirdDataService extends BaseService<RemoteDataBo, RemoteDataVo> {

    /***
     * 批量添加抄表数据
     * @param remoteDataBos
     * @throws FrameworkRuntimeException
     */
    @Override
    void adds(List<RemoteDataBo> remoteDataBos) throws FrameworkRuntimeException;

    /**
     * 批量编辑抄表数据
     * @param remoteDataBos
     * @throws FrameworkRuntimeException
     */
    @Override
    void edits(List<RemoteDataBo> remoteDataBos) throws FrameworkRuntimeException;

    /**
     * 查询水表数据列表
     * @param remoteDataBos
     * @param factoryId
     * @return
     * @throws FrameworkRuntimeException
     */
    List<RemoteDataVo> list(List<RemoteDataBo> remoteDataBos) throws FrameworkRuntimeException;
}
