package com.dotop.pipe.server.rest.service.report;


import com.dotop.pipe.core.form.NightReadingForm;
import com.dotop.pipe.core.vo.report.NightReadingVo;
import com.dotop.pipe.web.api.factory.report.INightReadingFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@RestController()
@RequestMapping("/report/device/night-reading")
public class NightReadingReportController implements BaseController<NightReadingForm> {

    private final static Logger logger = LogManager.getLogger(NightReadingReportController.class);

    @Autowired
    INightReadingFactory iNightReadingFactory;

    /**
     * 根据设备列表查询每个设备的范围时间内的读数及夜间读数
     *
     * @param nightReadingForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/list-devices", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String listDevice(@RequestBody NightReadingForm nightReadingForm) throws FrameworkRuntimeException {
        logger.info(LogMsg.to("msg:", " 列表查询开始", "nightReadingForm", nightReadingForm));
        List<String> deviceIds = nightReadingForm.getDeviceIds();
        if (deviceIds == null || deviceIds.size() == 0) {
            return resp(new ArrayList<>());
        }
        List<NightReadingVo> nightReadingVos = iNightReadingFactory.listByDevices(nightReadingForm);
        logger.info(LogMsg.to("msg:", " 列表查询结束"));
        return resp(nightReadingVos);
    }
}
