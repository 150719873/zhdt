package com.dotop.smartwater.project.third.meterread.client.service;


import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteValveBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteValveVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;


/**
 * 对接第三方系统的开关阀命令接口
 *
 *
 */
public interface IThirdCommandService extends BaseService<RemoteValveBo, RemoteValveVo> {

    /**
     * 过滤判断IfProcess是否存在没处理的开关阀控制
     *
     * @param remoteValveBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    List<RemoteValveVo> list(RemoteValveBo remoteValveBo) throws FrameworkRuntimeException;

    /**
     * 下发成功后【成聪库】记录返回的device_downlink的clientid到扩展字段，【成聪库】的IfProcess修改为【正在处理】和处理时间等等
     *
     * @param remoteValveBo
     * @return
     * @throws FrameworkRuntimeException
     */
    void editRemoteValve(RemoteValveBo remoteValveBo) throws FrameworkRuntimeException;

    /**
     * 根据查询【成聪库】的device_downlink的clientid，并查询【水务库】的status结果。如果结果为【成功】或【失败】，则更新【成聪库】的IfProcess修改为【已处理】等等；如果没有结果则不处理，并将执行次数增加1，如果执行超过【3次】，则标记为【失败】，其他的等待下一次轮询
     *
     * @param remoteValveBo
     * @return
     * @throws FrameworkRuntimeException
     */
    void editResult(RemoteValveBo remoteValveBo) throws FrameworkRuntimeException;
}
