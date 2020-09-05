package com.dotop.smartwater.project.third.meterread.client.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceDownlinkForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.meterread.client.api.IThirdFactory;
import com.dotop.smartwater.project.third.meterread.client.thread.CheckDeviceValveCommandStatussRunnable;
import com.dotop.smartwater.project.third.meterread.client.thread.CheckDeviceValveCommandsRunnable;
import com.dotop.smartwater.project.third.meterread.client.thread.CheckOwnersRunnable;
import com.dotop.smartwater.project.third.meterread.client.thread.RefreshDeviceUplinksRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方系统业务入口层
 *
 *
 */
@RestController
@RequestMapping("/third")
public class ThirdController implements BaseController<BaseForm> {

    private final static Logger LOGGER = LogManager.getLogger(TestController.class);

    @Autowired
    private IThirdFactory iThirdFactory;


    /**
     * 检查业主信息，参数包括企业id、业主id(可选)、分页(可选)
     *
     * @param ownerForm
     * @return
     */
    @PostMapping(value = "/checkOwners", produces = GlobalContext.PRODUCES)
    public String checkOwners(OwnerForm ownerForm) {
        if (CheckOwnersRunnable.temp){
            return "更新中,请稍后再更新";
        }
        LOGGER.info(LogMsg.to("ownerForm", ownerForm));
        String enterpriseid = ownerForm.getEnterpriseid();
        String ownerid = ownerForm.getOwnerid();
        Integer page = ownerForm.getPage();
        Integer pageCount = ownerForm.getPageCount();
        VerificationUtils.string("enterpriseid", enterpriseid);
        VerificationUtils.string("ownerid", ownerid, true);
        VerificationUtils.integer("page", page, true);
        VerificationUtils.integer("pageCount", pageCount, true);
        // 暂时不考虑异步处理
        iThirdFactory.checkOwners(enterpriseid);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 刷新设备上行读数，参数包括企业id、deveui(可选)、分页(可选)
     *
     * @param deviceUplinkForm
     * @return
     */
    @PostMapping(value = "/refreshDeviceUplinks", produces = GlobalContext.PRODUCES)
    public String refreshDeviceUplinks(DeviceUplinkForm deviceUplinkForm) {
        if (RefreshDeviceUplinksRunnable.temp){
            return "定时器更新中,请稍后再更新";
        }
        LOGGER.info(LogMsg.to("deviceUplinkForm", deviceUplinkForm));
        String enterpriseid = deviceUplinkForm.getEnterpriseid();
        String deveui = deviceUplinkForm.getDeveui();
        String devno = deviceUplinkForm.getDevno();
        Integer page = deviceUplinkForm.getPage();
        Integer pageCount = deviceUplinkForm.getPageCount();
        VerificationUtils.string("enterpriseid", enterpriseid);
        VerificationUtils.string("deveui", deveui, true);
        VerificationUtils.string("devno", devno, true);
        VerificationUtils.integer("page", page, true);
        VerificationUtils.integer("pageCount", pageCount, true);
        // 暂时不考虑异步处理
        iThirdFactory.refreshDeviceUplinks(enterpriseid);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }


    /**
     * 检查设备下发命令
     *
     * @param deviceDownlinkForm
     * @return
     */
    @GetMapping(value = "/checkDeviceValveCommands", produces = GlobalContext.PRODUCES)
    public String checkDeviceValveCommands(DeviceDownlinkForm deviceDownlinkForm) {
        if (CheckDeviceValveCommandsRunnable.temp){
            return "更新中,请稍后再更新";
        }
        String enterpriseid = deviceDownlinkForm.getEnterpriseid();
        String deveui = deviceDownlinkForm.getDeveui();
        Integer page = deviceDownlinkForm.getPage();
        Integer pageCount = deviceDownlinkForm.getPageCount();
        iThirdFactory.checkDeviceValveCommands(enterpriseid);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 检查设备下发命令的状态
     *
     * @param deviceDownlinkForm
     * @return
     */
    @GetMapping(value = "/checkDeviceValveCommandStatuss", produces = GlobalContext.PRODUCES)
    public String checkDeviceValveCommandStatuss(DeviceDownlinkForm deviceDownlinkForm) {
        if (CheckDeviceValveCommandStatussRunnable.temp){
            return "更新中,请稍后再更新";
        }
        String enterpriseid = deviceDownlinkForm.getEnterpriseid();
        iThirdFactory.checkDeviceValveCommandStatuss(enterpriseid);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }
}
