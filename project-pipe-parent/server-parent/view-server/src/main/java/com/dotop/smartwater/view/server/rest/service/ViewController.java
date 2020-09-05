package com.dotop.smartwater.view.server.rest.service;

import com.dotop.smartwater.view.server.api.factory.view.IAreaFactory;
import com.dotop.smartwater.view.server.api.factory.view.IViewFactory;
import com.dotop.smartwater.view.server.core.area.form.AreaForm;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.core.form.AlarmForm;
import com.dotop.pipe.core.form.BrustPipeForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.WorkOrderForm;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.view.server.core.brustpipe.vo.Brust;
import com.dotop.smartwater.view.server.core.brustpipe.vo.BrustPipeOperationsVo;
import com.dotop.smartwater.view.server.core.workorder.vo.WorkOrderSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/view")
public class ViewController implements BaseController<DeviceSummaryForm> {

    @Autowired
    IViewFactory iViewFactory;
    @Autowired
    IAuthCasWeb iAuthCasWeb;
    @Autowired
    IAreaFactory iAreaFactory;

    /**
     * 初始化数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/init", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String init(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return resp(iViewFactory.init(deviceSummaryForm));
    }

    /**
     * 手动刷新数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/updateTask", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String updateTask(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        deviceSummaryForm.setEnterpriseId(iAuthCasWeb.get().getEnterpriseId());
        return resp(iViewFactory.updateTask(deviceSummaryForm));
    }

    /**
     * 查询列表
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        deviceSummaryForm.setEnterpriseId(iAuthCasWeb.get().getEnterpriseId());
        return resp(iViewFactory.list(deviceSummaryForm));
    }

    /**
     * 查询列表curr表
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/listCurr", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String listCurr(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        deviceSummaryForm.setEnterpriseId(iAuthCasWeb.get().getEnterpriseId());
        return resp(iViewFactory.listCurr(deviceSummaryForm));
    }


    /**
     * 查询水厂数据分页
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/pageFactory", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String pageFactory(@RequestBody DeviceSummaryForm deviceSummaryForm) {
        // 验证
        VerificationUtils.integer("page", deviceSummaryForm.getPage());
        VerificationUtils.integer("pageSize", deviceSummaryForm.getPageSize());
        deviceSummaryForm.setEnterpriseId(iAuthCasWeb.get().getEnterpriseId());
        Pagination<DeviceSummaryVo> pagination = iViewFactory.pageFactory(deviceSummaryForm);
        return resp(pagination);
    }

    /**
     * 查询dma分区
     *
     * @param areaForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/listDma", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String listDma(@RequestBody AreaForm areaForm) throws FrameworkRuntimeException {
        return resp(iAreaFactory.listDma(areaForm));
    }

    @PostMapping(value = "/workorder", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String workOrderProcessing(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        List<WorkOrderSummaryVo> list = iViewFactory.workOrderProcessing(workOrderForm);
        return resp(list);
    }

    @PostMapping(value = "/alarm", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody AlarmForm alarmForm) {
        // 验证
        VerificationUtils.integer("page", alarmForm.getPage());
        VerificationUtils.integer("pageSize", alarmForm.getPageSize());
        Pagination<AlarmVo> pagination = iViewFactory.pageAlarm(alarmForm);
        return resp(pagination);
    }

    @PostMapping(value = "/brustpipe", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String brustPipe(@RequestBody DeviceForm deviceForm) {
        BrustPipeOperationsVo brustPipeOperationsVo = iViewFactory.brustPipe(deviceForm);
        return resp(brustPipeOperationsVo);
    }

    @PostMapping(value = "/brustpipeList", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String brustPipeList(@RequestBody BrustPipeForm brustPipeForm) {
        List<Brust> brustPipeVoList = iViewFactory.brustPipeList(brustPipeForm);
        return resp(brustPipeVoList);
    }


    @PostMapping(value = "/watermeter", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String waterMeterData(@RequestBody DeviceForm deviceForm) {
        DevicePropertyVo deviceProperty = iViewFactory.waterMeterData(deviceForm);
        return resp(deviceProperty);
    }

    @PostMapping(value = "/brustpipeunhandler", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String brustPipeUnHandler(@RequestBody DeviceForm deviceForm) {
        List<String> list = iViewFactory.brustPipeUnHandler(deviceForm);
        return resp(list);
    }
}
