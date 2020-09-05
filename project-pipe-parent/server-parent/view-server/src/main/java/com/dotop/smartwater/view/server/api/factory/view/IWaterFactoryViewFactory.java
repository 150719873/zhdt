package com.dotop.smartwater.view.server.api.factory.view;


import com.dotop.smartwater.view.server.core.maintain.form.MaintainLogForm;
import com.dotop.smartwater.view.server.core.maintain.vo.MaintainLogVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;

import java.util.List;
import java.util.Map;

/**
 * 大屏水厂相关
 */
public interface IWaterFactoryViewFactory extends BaseFactory<DeviceSummaryForm, DeviceSummaryVo> {

    /**
     * 大屏水厂数据定时器
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    String waterFactoryUpdateTask(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;


    /**
     * 初始化数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    String init(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    Map<String, Map<String, DeviceSummaryVo>> getCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    List<DeviceSummaryVo> listGroup(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    List<DeviceVo> devicelist(DeviceForm deviceForm) throws FrameworkRuntimeException;

    /**
     * 电力报表
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<Map<String, DeviceSummaryVo>> pagePower(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    /**
     * 设备设施保养记录
     *
     * @param maintainLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<MaintainLogVo> pageMaintain(MaintainLogForm maintainLogForm) throws FrameworkRuntimeException;

    /**
     * * 进出水比例
     * *
     * * @param deviceSummaryForm
     * * @return
     * * @throws FrameworkRuntimeException
     */
    Map<String, List<DeviceSummaryVo>> inOutWaterList(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    Map<String, List<DeviceSummaryVo>> getMapByTypes(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    Pagination<Map<String, DeviceSummaryVo>> pageInOutWater(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    DeviceSummaryVo getSummary(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;
}
