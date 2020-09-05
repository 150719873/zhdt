package com.dotop.pipe.server.rest.service.report;


import com.dotop.pipe.core.form.DeviceBrustPipeForm;
import com.dotop.pipe.core.vo.report.DeviceBrustPipeVo;
import com.dotop.pipe.web.api.factory.report.IDeviceBrustPipeFactory;
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
@RequestMapping("/report/device/brust-pipe")
public class BrustPipeReportController implements BaseController<DeviceBrustPipeForm> {

    private final static Logger logger = LogManager.getLogger(BrustPipeReportController.class);

    @Autowired
    IDeviceBrustPipeFactory iDeviceBrustPipeFactory;

    /**
     * 根据管道划分爆管信息，并分页
     * @param deviceBrustPipeForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/pipe/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String pipePage(@RequestBody DeviceBrustPipeForm deviceBrustPipeForm) throws FrameworkRuntimeException {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "deviceBrustPipeForm", deviceBrustPipeForm));
        Pagination<DeviceBrustPipeVo> page = iDeviceBrustPipeFactory.pagePipe(deviceBrustPipeForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(page);
    }

    /**
     * 根据区域划分爆管信息，并分页
     * @param deviceBrustPipeForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/area/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String areaPage(@RequestBody DeviceBrustPipeForm deviceBrustPipeForm) throws FrameworkRuntimeException {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "deviceBrustPipeForm", deviceBrustPipeForm));
        Pagination<DeviceBrustPipeVo> page = iDeviceBrustPipeFactory.pageArea(deviceBrustPipeForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(page);
    }
}
