package com.dotop.smartwater.project.server.water.rest.service.third;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.revenue.IThirdDeviceDataPushFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.customize.OperationForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**

 * 第三方对接下发水表命令接口
 */
/*@RestController
@RequestMapping("/third")*/
@Deprecated
public class DownLinkController implements BaseController<BaseForm> {

    private final static Logger LOGGER = LogManager.getLogger(DownLinkController.class);
    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Autowired
    private IThirdDeviceDataPushFactory iThirdDeviceDataPushFactory;

    @PostMapping(value = "/downlink", produces = GlobalContext.PRODUCES)
    public String downlink(@RequestBody OperationForm operationForm) {
        // 校验
        String devno = operationForm.getDevNo();
        VerificationUtils.string("devNo", devno);

        String enterpriseid = operationForm.getEnterpriseid();
        VerificationUtils.string("enterpriseid", enterpriseid);

        Integer type = operationForm.getType();
        VerificationUtils.integer("type", type);

        if (type.equals(TxCode.CloseCommand) || type.equals(TxCode.OpenCommand)) {
            // 操作
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setDevno(devno);
            deviceForm.setEnterpriseid(enterpriseid);
            String clientId = iDeviceFactory.thirdOpenOrClose(operationForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, clientId);
        }
        return resp(ResultCode.Fail, "unknown command", null);
    }

    /*@PostMapping(value = "/uplinks", produces = GlobalContext.PRODUCES)
    public String uplinks(
            @RequestHeader("ticket") String ticket,
            @RequestHeader("enterpriseid") String enterpriseid,
            @RequestBody List<WaterUploadForm> list) {

        LOGGER.info(LogMsg.to("msg:", "uplinks", "list", list));

        // TODO 校验Ticket
        //VerificationUtils.string("ticket", ticket);


        VerificationUtils.string("enterpriseid", enterpriseid);
        VerificationUtils.objList("list", list);

        iThirdDeviceDataPushFactory.batchUplink(enterpriseid, list);

        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }*/

}
