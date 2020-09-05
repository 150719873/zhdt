package com.dotop.smartwater.project.third.server.meterread.client3.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.OperationBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

import java.util.List;

public interface IThirdFactory {

    /**
     * 检查业主信息
     *
     * @throws FrameworkRuntimeException
     */
    void checkOwners( ) throws FrameworkRuntimeException;

    /**
     * 刷新设备上行读数
     *
     * @throws FrameworkRuntimeException
     */
    void refreshDeviceUplinks() throws FrameworkRuntimeException;


    /**
     * 检查设备下发命令
     *
     * @throws FrameworkRuntimeException
     */
    void checkDeviceValveCommands() throws FrameworkRuntimeException;


    /**
     * 检查设备下发命令的状态
     *
     * @throws FrameworkRuntimeException
     */
    void checkDeviceValveCommandStatuss() throws FrameworkRuntimeException;

    /**
     * 下发开关阀命令
     *
     * @param operationBo
     * @return
     * @throws FrameworkRuntimeException
     */
    String downlink(OperationBo operationBo) throws FrameworkRuntimeException;

    /**
     * 通过clientId获取上行数据结果
     *
     * @param clientIds
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DeviceDownlinkVo> getDownlinks(List<String> clientIds) throws FrameworkRuntimeException;

    /**
     * 获取设备信息
     *
     * @param deviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<DeviceVo> getDevices(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 获取最新数据
     *
     * @param deviceUplinkBo
     * @return
     */
    Pagination<DeviceUplinkVo> getDatas(DeviceUplinkBo deviceUplinkBo);
}
