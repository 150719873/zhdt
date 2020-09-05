package com.dotop.smartwater.project.module.api.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.DeviceBookManagementForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookManagementVo;

/**
 * 表册管理
 *

 * @date 2019年3月4日
 */
public interface IDeviceBookManagementFactory extends BaseFactory<DeviceBookManagementForm, DeviceBookManagementVo> {

    /**
     * @param deviceBookManagementForm 表册对象
     * @return
     * @
     */
    @Override
    List<DeviceBookManagementVo> list(DeviceBookManagementForm deviceBookManagementForm);

    /**
     * @param deviceBookManagementForm 表册对象
     * @return
     */
    @Override
    DeviceBookManagementVo get(DeviceBookManagementForm deviceBookManagementForm) ;

    /**
     * @param deviceBookManagementForm 表册对象
     * @return
     */
    @Override
    DeviceBookManagementVo add(DeviceBookManagementForm deviceBookManagementForm) ;

    /**
     * @param deviceBookManagementForm 表册对象
     * @return
     */
    @Override
    DeviceBookManagementVo edit(DeviceBookManagementForm deviceBookManagementForm) ;

    /**
     * @param deviceBookManagementForm 表册对象
     * @return
     */
    @Override
    String del(DeviceBookManagementForm deviceBookManagementForm) ;

}
