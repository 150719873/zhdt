package com.dotop.smartwater.project.third.meterread.client.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.OperationBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;

import java.util.List;

public interface IThirdFactory {


    /**
     * 检查业主信息
     *
     * @throws FrameworkRuntimeException
     */
    void checkOwners(String enterpriseid) throws FrameworkRuntimeException;


    /**
     * 刷新设备上行读数
     * @param enterpriseid
     * @throws FrameworkRuntimeException
     */
    void refreshDeviceUplinks(String enterpriseid) throws FrameworkRuntimeException;

    /**
     * 获取上行分页数据
     * @param enterpriseid
     * @param page
     * @param pageCount
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<DeviceUplinkVo> getUplinkPage(String enterpriseid, Integer page, Integer pageCount) throws FrameworkRuntimeException;

    /**
     * 检查设备下发命令
     *
     * @throws FrameworkRuntimeException
     */
    void checkDeviceValveCommands(String enterpriseid) throws FrameworkRuntimeException;


    /**
     * 检查设备下发命令的状态
     *
     * @throws FrameworkRuntimeException
     */
    void checkDeviceValveCommandStatuss(String enterpriseid) throws FrameworkRuntimeException;

    /**
     * 下发开关阀命令
     * @param operationBo
     * @return
     * @throws FrameworkRuntimeException
     */
    String downlink(OperationBo operationBo) throws FrameworkRuntimeException;

    /**
     * 通过clientId获取上行数据结果
     * @param deviceDownlinkBos
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DeviceDownlinkVo> getDownlinks(List<DeviceDownlinkBo> deviceDownlinkBos) throws FrameworkRuntimeException;

    /**
     * 检测企业id是否存在
     * @return
     */
    boolean checkEnterpriseIdExit();
}
