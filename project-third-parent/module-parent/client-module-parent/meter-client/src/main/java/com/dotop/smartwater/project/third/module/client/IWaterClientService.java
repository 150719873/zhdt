package com.dotop.smartwater.project.third.module.client;

import com.dotop.smartwater.project.third.module.core.water.bo.*;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.*;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;

import java.util.List;

/**
 *
 */
public interface IWaterClientService {


    /**
     * 新增客户接口
     */
    String ownerAdds(List<OwnerBo> owners, DockingBo dockingBo) throws FrameworkRuntimeException;

    /**
     * 编辑客户接口
     */
    String ownerEdits(List<OwnerBo> owners, DockingBo dockingBo) throws FrameworkRuntimeException;

    /**
     * 换表客户接口
     */
    String ownerChanges(List<OwnerChangeBo> ownerChanges, DockingBo dockingBo) throws FrameworkRuntimeException;

    /**
     * 新增设备接口
     */
    String deviceAdds(List<DeviceBo> devices, DockingBo dockingBo) throws FrameworkRuntimeException;

    /**
     * 编辑设备接口
     */
    String deviceEdits(List<DeviceBo> devices, DockingBo dockingBo) throws FrameworkRuntimeException;

    /**
     * 推送读数接口
     */
    String uplinks(List<DeviceUplinkBo> waters, DockingBo dockingBo) throws FrameworkRuntimeException;

    /**
     * TODO 新增下发命令接口
     */
    CommandVo downlink(CommandBo command, String ticket, DockingBo dockingBo) throws FrameworkRuntimeException;

    /**
     * TODO 编辑命令结果接口
     */
    String downlinkEdit(CommandBo command, String ticket, DockingBo dockingBo) throws FrameworkRuntimeException;

}
