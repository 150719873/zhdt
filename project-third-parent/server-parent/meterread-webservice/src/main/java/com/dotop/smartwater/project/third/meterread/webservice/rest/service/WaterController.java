package com.dotop.smartwater.project.third.meterread.webservice.rest.service;

import com.dotop.smartwater.project.third.meterread.webservice.api.IWaterFactory;
import com.dotop.smartwater.project.third.meterread.webservice.schedule.CheckOwnersScheduled;
import com.dotop.smartwater.project.third.meterread.webservice.schedule.RefreshDeviceUplinksScheduled;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 水务系统业务入口层，主要用生成主动触发
 *
 *
 */
@RestController
@RequestMapping("/water")
public class WaterController implements BaseController<BaseForm> {

    private final static Logger LOGGER = LogManager.getLogger(WaterController.class);

    @Autowired
    private IWaterFactory iWaterFactory;

    /**
     * 检查业主信息
     */
    @PostMapping(value = "/checkOwners", produces = GlobalContext.PRODUCES)
    public String checkOwners(@RequestBody Map<String, String> params) {
        if (CheckOwnersScheduled.temp) {
            return "定时器更新中,请稍后再更新";
        }
        LOGGER.info(LogMsg.to("params", params));
        String ownerid = params.get("ownerid");
        String enterpriseid = params.get("enterpriseid");
        String factoryId = params.get("factoryId");
        VerificationUtils.string("enterpriseid", enterpriseid);
        VerificationUtils.string("factoryId", enterpriseid);
        // 暂时不考虑异步处理
        iWaterFactory.checkOwners(enterpriseid, Integer.valueOf(factoryId), ownerid, null, null);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 刷新设备上行读数
     */
    @PostMapping(value = "/refreshDeviceUplinks", produces = GlobalContext.PRODUCES)
    public String refreshDeviceUplinks(@RequestBody Map<String, String> params) {
        if (RefreshDeviceUplinksScheduled.temp) {
            return "定时器更新中,请稍后再更新";
        }
        LOGGER.info(LogMsg.to("params", params));
        String deveui = params.get("deveui");
        String devno = params.get("devno");
        String enterpriseid = params.get("enterpriseid");
        String factoryId = params.get("factoryId");
        VerificationUtils.string("enterpriseid", enterpriseid);
        VerificationUtils.string("factoryId", enterpriseid);
        // 暂时不考虑异步处理
        iWaterFactory.refreshDeviceUplinks(enterpriseid, Integer.valueOf(factoryId), deveui, devno, null, null);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }
}
