package com.dotop.smartwater.view.server.api.factory.view;


import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.device.form.WaterWmLogForm;
import com.dotop.smartwater.view.server.core.device.vo.WaterWmLogVo;

/**
 * 水质日志
 */
public interface IWaterWmFactory extends BaseFactory<WaterWmLogForm, WaterWmLogVo> {

    /**
     * 水质记录定时器
     *
     * @param waterWmLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    String updateTask(WaterWmLogForm waterWmLogForm) throws FrameworkRuntimeException;

    /**
     * 水质记录
     *
     * @param waterWmLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<WaterWmLogVo> page(WaterWmLogForm waterWmLogForm) throws FrameworkRuntimeException;

}
