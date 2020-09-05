package com.dotop.smartwater.project.third.server.meterread.client3.service;


import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.server.meterread.client3.core.third.bo.SbDtBo;
import com.dotop.smartwater.project.third.server.meterread.client3.core.third.vo.SbDtVo;

import java.util.List;


/**
 * 对接第三方系统的开关阀命令接口
 *
 *
 */
public interface IThirdCommandService extends BaseService<SbDtBo, SbDtVo> {

    /**
     * 过滤判断ProcessFlag是否存在没处理的开关阀控制
     *
     * @param sbDtBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    List<SbDtVo> list(SbDtBo sbDtBo) throws FrameworkRuntimeException;

    /**
     * 查询水表数据列表
     *
     * @param sbDtBos
     * @return
     * @throws FrameworkRuntimeException
     */
    List<SbDtVo> list(List<SbDtBo> sbDtBos) throws FrameworkRuntimeException;


    /**
     * 编辑
     *
     * @param sbDtBo
     * @return
     * @throws FrameworkRuntimeException
     */
    void editSbDt(SbDtBo sbDtBo) throws FrameworkRuntimeException;


    /***
     * 批量添加抄表数据
     * @param sbDtBos
     * @throws FrameworkRuntimeException
     */
    @Override
    void adds(List<SbDtBo> sbDtBos) throws FrameworkRuntimeException;

    /**
     * 批量编辑抄表数据
     *
     * @param sbDtBos
     * @throws FrameworkRuntimeException
     */
    @Override
    void edits(List<SbDtBo> sbDtBos) throws FrameworkRuntimeException;
}
