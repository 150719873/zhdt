package com.dotop.smartwater.view.server.service.device;


import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface IDeviceSummaryService {

    List<DeviceSummaryVo> list(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    List<DeviceSummaryVo> listFactory(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    List<DeviceSummaryVo> listFactoryNotGroup(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    List<DeviceSummaryVo> listCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    List<DeviceSummaryVo> listFactoryCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    Boolean isInit(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    void adds(List<DeviceSummaryForm> deviceSummaryForms) throws FrameworkRuntimeException;

    void addCuurs(List<DeviceSummaryForm> deviceSummaryForms) throws FrameworkRuntimeException;

    void edits(List<DeviceSummaryForm> deviceSummaryForms) throws FrameworkRuntimeException;

    void dels(List<DeviceSummaryForm> deviceSummaryForms) throws FrameworkRuntimeException;

    void delByType(List<String> list, Date date) throws FrameworkRuntimeException;

    /**
     * 获取大屏水厂当天最新数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DeviceSummaryVo> getCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    /**
     * 分组统计 年 月 周
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DeviceSummaryVo> listGroup(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    List<DeviceSummaryVo> inOutWaterListGroup(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    /**
     * 分组 分页 查询
     *
     * @param deviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<Map<String, DeviceSummaryVo>> pagePower(DeviceSummaryForm deviceForm) throws FrameworkRuntimeException;

    Pagination<Map<String, DeviceSummaryVo>> pageInOutWater(DeviceSummaryForm deviceForm) throws FrameworkRuntimeException;

    DeviceSummaryVo getSummary(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;
}
