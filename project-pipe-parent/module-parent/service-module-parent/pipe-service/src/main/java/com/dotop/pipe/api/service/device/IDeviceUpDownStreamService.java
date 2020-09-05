package com.dotop.pipe.api.service.device;

import java.util.List;

import com.dotop.pipe.core.bo.device.DeviceUpDownStreamBo;
import com.dotop.pipe.core.vo.device.DeviceUpDownStreamVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

// 设备上下游
public interface IDeviceUpDownStreamService extends BaseService<DeviceUpDownStreamBo, DeviceUpDownStreamVo> {

    DeviceUpDownStreamVo add(DeviceUpDownStreamBo deviceUpDownStreamBo);

    void editAlarmProperty(DeviceUpDownStreamBo deviceUpDownStreamBo);

    String del(DeviceUpDownStreamBo deviceUpDownStreamBo);

    List<DeviceVo> getParent(DeviceUpDownStreamBo deviceUpDownStreamBo);

    List<DeviceVo> getChild(DeviceUpDownStreamBo deviceUpDownStreamBo);

    Pagination<DeviceUpDownStreamVo> page(DeviceUpDownStreamBo deviceUpDownStreamBo);

    /**
     * 获取预测值的范围  以及上游设备的最新属性值
     *
     * @param deviceUpDownStreamBo
     * @return
     */
    DeviceVo getForecast(DeviceUpDownStreamBo deviceUpDownStreamBo);


}
