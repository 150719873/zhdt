package com.dotop.smartwater.project.third.server.meterread.client3.rest.service;

import com.dotop.smartwater.project.third.server.meterread.client3.api.IThirdFactory;
import com.dotop.smartwater.project.third.server.meterread.client3.thread.CheckDeviceValveCommandStatussRunnable;
import com.dotop.smartwater.project.third.server.meterread.client3.thread.CheckDeviceValveCommandsRunnable;
import com.dotop.smartwater.project.third.server.meterread.client3.thread.CheckOwnersRunnable;
import com.dotop.smartwater.project.third.server.meterread.client3.thread.RefreshDeviceUplinksRunnable;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final static Logger LOGGER = LogManager.getLogger(ThirdController.class);

    @Autowired
    private IThirdFactory iThirdFactory;

    /**
     * 刷新设备上行读数，参数包括企业id、deveui(可选)、分页(可选)
     * <p>
     * //     * @param deviceUplinkForm
     *
     * @return
     */
    @GetMapping(value = "/checkOwners")
    public String checkOwners() {
        if (CheckOwnersRunnable.temp) {
            return "定时器更新中,请稍后再更新";
        }
        iThirdFactory.checkOwners();
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 刷新设备上行读数，参数包括企业id、deveui(可选)、分页(可选)
     *
     * @return
     */
    @GetMapping(value = "/refreshDeviceUplinks", produces = GlobalContext.PRODUCES)
    public String refreshDeviceUplinks() {
        if (RefreshDeviceUplinksRunnable.temp) {
            return "定时器更新中,请稍后再更新";
        }
        // 暂时不考虑异步处理
        iThirdFactory.refreshDeviceUplinks();
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }


    /**
     * 检查设备下发命令
     *
     * @return
     */
    @GetMapping(value = "/checkDeviceValveCommands", produces = GlobalContext.PRODUCES)
    public String checkDeviceValveCommands() {
        if (CheckDeviceValveCommandsRunnable.temp) {
            return "更新中,请稍后再更新";
        }
        iThirdFactory.checkDeviceValveCommands();
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 检查设备下发命令的状态
     *
     * @return
     */
    @GetMapping(value = "/checkDeviceValveCommandStatuss", produces = GlobalContext.PRODUCES)
    public String checkDeviceValveCommandStatuss() {
        if (CheckDeviceValveCommandStatussRunnable.temp) {
            return "更新中,请稍后再更新";
        }
        iThirdFactory.checkDeviceValveCommandStatuss();
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }
}
