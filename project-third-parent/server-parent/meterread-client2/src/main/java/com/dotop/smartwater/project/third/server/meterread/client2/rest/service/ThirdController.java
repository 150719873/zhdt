package com.dotop.smartwater.project.third.server.meterread.client2.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.server.meterread.client2.api.IThirdFactory;
import com.dotop.smartwater.project.third.server.meterread.client2.thread.RefreshDeviceUplinksRunnable;
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
 * http://localhost:35559/meterread-client/third/refreshDeviceUplinks
 *
 */
@RestController
@RequestMapping("/third")
public class ThirdController implements BaseController<BaseForm> {

    private final static Logger LOGGER = LogManager.getLogger(TestController.class);

    @Autowired
    private IThirdFactory iThirdFactory;


    /**
     * 刷新设备上行读数，参数包括企业id、deveui(可选)、分页(可选)
     * <p>
     * //     * @param deviceUplinkForm
     *
     * @return
     */
    @GetMapping(value = "/refreshDeviceUplinks")
    public String refreshDeviceUplinks() {
        if (RefreshDeviceUplinksRunnable.temp) {
            return "定时器更新中,请稍后再更新";
        }
//        LOGGER.info(LogMsg.to("deviceUplinkForm", deviceUplinkForm));
//        String enterpriseid = deviceUplinkForm.getEnterpriseid();
//        VerificationUtils.string("enterpriseid", enterpriseid);
        // 暂时不考虑异步处理
        iThirdFactory.refreshDeviceUplinks();
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }


}
