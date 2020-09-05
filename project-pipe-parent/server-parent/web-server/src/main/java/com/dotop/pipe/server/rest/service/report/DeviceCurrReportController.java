package com.dotop.pipe.server.rest.service.report;


import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.report.DeviceCurrVo;
import com.dotop.pipe.web.api.factory.report.IDeviceCurrFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController()
@RequestMapping("/report/device/curr")
public class DeviceCurrReportController implements BaseController<DeviceForm> {

    private final static Logger logger = LogManager.getLogger(DeviceCurrReportController.class);

    @Autowired
    IDeviceCurrFactory iDeviceCurrFactory;

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody DeviceForm deviceForm) throws FrameworkRuntimeException {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", deviceForm));
        Pagination<DeviceCurrVo> page = iDeviceCurrFactory.page(deviceForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(page);
    }
}
