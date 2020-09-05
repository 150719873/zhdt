package com.dotop.smartwater.view.server.rest.service;

import com.dotop.smartwater.view.server.api.factory.view.*;
import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.form.WaterWmLogForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;
import com.dotop.smartwater.view.server.core.device.vo.WaterWmLogVo;
import com.dotop.smartwater.view.server.core.maintain.form.MaintainLogForm;
import com.dotop.smartwater.view.server.core.maintain.vo.MaintainLogVo;
import com.dotop.smartwater.view.server.core.monitor.form.LiquidLevelForm;
import com.dotop.smartwater.view.server.core.monitor.form.PondAlarmForm;
import com.dotop.smartwater.view.server.core.monitor.vo.LiquidLevelVo;
import com.dotop.smartwater.view.server.core.monitor.vo.PondAlarmVo;
import com.dotop.smartwater.view.server.core.security.vo.SecurityLogVo;
import com.dotop.smartwater.view.server.core.security.vo.SecuritySwitchVo;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.api.factory.view.*;
import com.dotop.smartwater.view.server.core.security.form.SecurityLogForm;
import com.dotop.smartwater.view.server.core.security.form.SecuritySwitchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("/view/waterfactory")
public class WaterFactoryViewController implements BaseController<DeviceSummaryForm> {

    @Autowired
    IViewFactory iViewFactory;
    @Autowired
    IAuthCasWeb iAuthCasWeb;
    @Autowired
    IAreaFactory iAreaFactory;

    @Autowired
    IWaterFactoryViewFactory iWaterFactoryViewFactory;

    @Autowired
    IWaterWmFactory iWaterWmFactory;

    @Autowired
    ISecurityFactory iSecurityFactory;

    @Autowired
    IMonitorFactory iMonitorFactory;

    /**
     * 初始化数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/init", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String init(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return resp(iWaterFactoryViewFactory.init(deviceSummaryForm));
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
        List<DeviceSummaryVo> list = iWaterFactoryViewFactory.listGroup(deviceSummaryForm);
        return resp(list);
    }

    /**
     * 电力报表分组
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/pagePower", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String pagePower(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        Pagination<Map<String, DeviceSummaryVo>> pagination = iWaterFactoryViewFactory.pagePower(deviceSummaryForm);
        return resp(pagination);
    }

    @PostMapping(value = "/devicelist", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String deviceList(@RequestBody DeviceForm deviceForm) throws FrameworkRuntimeException {
        List<DeviceVo> list = iWaterFactoryViewFactory.devicelist(deviceForm);
        return resp(list);
    }

    /**
     * 当前最新数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/getCurr", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getCurr(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return resp(iWaterFactoryViewFactory.getCurr(deviceSummaryForm));
    }

    // 求和 最大值 最小值 等
    @PostMapping(value = "/getSummary", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getSummary(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return resp(iWaterFactoryViewFactory.getSummary(deviceSummaryForm));
    }


    // 设备设施保养记录
    @PostMapping(value = "/pageMaintain", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String pageMaintain(@RequestBody MaintainLogForm maintainLogForm) throws FrameworkRuntimeException {
        Pagination<MaintainLogVo> pagination = iWaterFactoryViewFactory.pageMaintain(maintainLogForm);
        return resp(pagination);
    }

    /**
     * 进出水比例
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/inOutWater", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String inOutWaterList(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        Map<String, List<DeviceSummaryVo>> map = iWaterFactoryViewFactory.inOutWaterList(deviceSummaryForm);
        return resp(map);
    }

    @PostMapping(value = "/pageInOutWater", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String pageInOutWater(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        Pagination<Map<String, DeviceSummaryVo>> pagination = iWaterFactoryViewFactory.pageInOutWater(deviceSummaryForm);
        return resp(pagination);
    }


    @PostMapping(value = "/getMapByTypes", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getMapByTypes(@RequestBody DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        Map<String, List<DeviceSummaryVo>> map = iWaterFactoryViewFactory.getMapByTypes(deviceSummaryForm);
        return resp(map);
    }


    // 水质记录
    @PostMapping(value = "/waterWmPage", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String waterWmPage(@RequestBody WaterWmLogForm waterWmLogForm) throws FrameworkRuntimeException {
        Pagination<WaterWmLogVo> pagination = iWaterWmFactory.page(waterWmLogForm);
        return resp(pagination);
    }

    // 安防开关列表
    @PostMapping(value = "/securitySwitchList", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String securitySwitchList(@RequestBody SecuritySwitchForm securitySwitchForm) throws FrameworkRuntimeException {
        Pagination<SecuritySwitchVo> list = iSecurityFactory.list(securitySwitchForm);
        return resp(list);
    }

    // 安防开关修改
    @PostMapping(value = "/securitySwitchEdit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String securitySwitchEdit(@RequestBody SecuritySwitchForm securitySwitchForm) throws FrameworkRuntimeException {
        String list = iSecurityFactory.edit(securitySwitchForm);
        return resp(list);
    }


    // 安防开关修改
    @PostMapping(value = "/securitySwitchInit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String securitySwitchInit(@RequestBody SecuritySwitchForm securitySwitchForm) throws FrameworkRuntimeException {
        iSecurityFactory.init("1f8b23a9-a614-4d41-8410-2de4bdec4cdb");
        return resp();
    }

    // 安防开关列表
    @PostMapping(value = "/securityLogList", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String securityLogList(@RequestBody SecurityLogForm securityLogForm) throws FrameworkRuntimeException {
        Pagination<SecurityLogVo> list = iSecurityFactory.logList(securityLogForm);
        return resp(list);
    }


    // 液位监控分页
    @PostMapping(value = "/monitorLiquidPage", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String monitorLiquidPage(@RequestBody LiquidLevelForm liquidLevelForm) throws FrameworkRuntimeException {
        Pagination<LiquidLevelVo> list = iMonitorFactory.liquidPage(liquidLevelForm);
        return resp(list);
    }


    // 水池预警监控
    @PostMapping(value = "/monitorPondPage", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String monitorPondPage(@RequestBody PondAlarmForm pondAlarmForm) throws FrameworkRuntimeException {
        Pagination<PondAlarmVo> list = iMonitorFactory.pondAlarmPage(pondAlarmForm);
        return resp(list);
    }

}
