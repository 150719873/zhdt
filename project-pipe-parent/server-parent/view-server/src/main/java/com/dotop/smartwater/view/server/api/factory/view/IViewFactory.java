package com.dotop.smartwater.view.server.api.factory.view;

import com.dotop.pipe.core.form.AlarmForm;
import com.dotop.pipe.core.form.BrustPipeForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.WorkOrderForm;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.brustpipe.vo.Brust;
import com.dotop.smartwater.view.server.core.brustpipe.vo.BrustPipeOperationsVo;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;
import com.dotop.smartwater.view.server.core.workorder.vo.WorkOrderSummaryVo;

import java.util.List;

/**
 *
 */
public interface IViewFactory extends BaseFactory<DeviceSummaryForm, DeviceSummaryVo> {

    /**
     * 查询统计列表
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    List<DeviceSummaryVo> list(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    /**
     * 查询统计列表curr表
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DeviceSummaryVo> listCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    /**
     * 水厂数据分页
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<DeviceSummaryVo> pageFactory(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    /**
     * 初始化数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    String init(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    /**
     * 是否已被初始化
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Boolean isInit(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    /**
     * 定时更新数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    String updateTask(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException;

    List<WorkOrderSummaryVo> workOrderProcessing(WorkOrderForm workOrderForm) throws FrameworkRuntimeException;

    Pagination<AlarmVo> pageAlarm(AlarmForm alarmForm) throws FrameworkRuntimeException;

    BrustPipeOperationsVo brustPipe(DeviceForm deviceForm) throws FrameworkRuntimeException;

    List<Brust> brustPipeList(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException;

    DevicePropertyVo waterMeterData(DeviceForm deviceForm) throws FrameworkRuntimeException;

    List<String> brustPipeUnHandler(DeviceForm deviceForm) throws FrameworkRuntimeException;
}
