package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;

import java.util.Map;

/**
 *
 */
public interface IThirdObtainFactory {

    /**
     * 更新设备信息
     *
     * @param dockingForm
     * @param loginDocking
     * @throws FrameworkRuntimeException
     */
    default void updateDevice(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
    }

    /**
     * 更新设备信息(已有数据)
     *
     * @param loginDocking
     * @param deviceForm
     * @throws FrameworkRuntimeException
     */
    default void updateDevice(DockingForm loginDocking, DeviceForm deviceForm) throws FrameworkRuntimeException {
    }

    /**
     * 更新抄表信息
     *
     * @param dockingForm
     * @param loginDocking
     * @throws FrameworkRuntimeException
     */
    default void updateDeviceUplink(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
    }

    /**
     * 更新业主信息
     *
     * @param dockingForm
     * @param loginDocking
     * @throws FrameworkRuntimeException
     */
    default void updateOwner(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
    }

    /**
     * 发送命令
     *
     * @throws FrameworkRuntimeException
     */
    default CommandVo sendCommand(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 发送需要等待的命令
     *
     * @throws FrameworkRuntimeException
     */
    default void sendCommandWait(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) throws FrameworkRuntimeException {
    }

    /**
     * 发送命令
     *
     * @throws FrameworkRuntimeException
     */
    default CommandVo cancelCommand(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 更新任务状态
     *
     * @throws FrameworkRuntimeException
     */
    default void updateCommandStatus(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) throws FrameworkRuntimeException {
    }

    /**
     * 同步单个设备
     *
     * @param loginDocking
     * @param deviceForm
     * @param deviceVoMap
     * @throws FrameworkRuntimeException
     */
    default void deviceSingelSync(DockingForm loginDocking, DeviceForm deviceForm, Map<String, DeviceVo> deviceVoMap) throws FrameworkRuntimeException {
    }

    /**
     * 同步单个业主
     *
     * @param deviceForm
     * @param loginDocking
     * @param ownerVoMap
     * @throws FrameworkRuntimeException
     */
    default void ownerSingleSync(DeviceForm deviceForm, DockingForm loginDocking, Map<String, OwnerVo> ownerVoMap) throws FrameworkRuntimeException {
    }

    /**
     * 更新单个设备上报数据
     *
     * @param dockingForm
     * @param loginDocking
     * @param deviceVo
     * @param deviceUplinkVoMap
     * @throws FrameworkRuntimeException
     */
    default void deviceUplinkSingelSync(DockingForm dockingForm, DockingForm loginDocking, DeviceVo deviceVo, Map<String, DeviceUplinkVo> deviceUplinkVoMap) throws FrameworkRuntimeException {
    }
}
