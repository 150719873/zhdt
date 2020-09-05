package com.dotop.pipe.server.rest.service.device;

import com.dotop.pipe.core.form.DeviceMappingForm;
import com.dotop.pipe.core.vo.device.DeviceMappingVo;
import com.dotop.pipe.web.api.factory.device.IDeviceMappingFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/device/mapping")
public class DeviceMappingController implements BaseController<DeviceMappingForm> {

    private static final Logger logger = LogManager.getLogger(DeviceMappingController.class);

    @Autowired
    private IDeviceMappingFactory iDeviceMappingFactory;

    /**
     * 进出口设置保存  其中包含新增或者删除   操作的对象是list集合
     *
     * @param deviceMappingForm
     * @return
     */
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody DeviceMappingForm deviceMappingForm) {
        logger.info(LogMsg.to("msg:", "新出口设备正反流量计设备保存", "deviceMappingForm", deviceMappingForm));
        VerificationUtils.string("deviceId", deviceMappingForm.getDeviceId());
        iDeviceMappingFactory.add(deviceMappingForm);
        logger.info(LogMsg.to("msg:", "新出口设备正反流量计设备保存", "更新数据"));
        return resp();
    }

    /**
     * @param deviceMappingForm
     * @return
     */
    @PostMapping(value = "/areaMappingList", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String mappingList(@RequestBody DeviceMappingForm deviceMappingForm) {
        logger.info(LogMsg.to("msg:", "查询设备开始", "deviceMappingForm", deviceMappingForm));
        VerificationUtils.string("deviceId", deviceMappingForm.getDeviceId());
        List<DeviceMappingVo> list = iDeviceMappingFactory.mappingList(deviceMappingForm);
        logger.info(LogMsg.to("msg:", "查询设备结束", "更新数据"));
        return resp(list);
    }

    /**
     * @param deviceMappingForm
     * @return
     */
    @PostMapping(value = "/regionMappingList", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String regionMappingList(@RequestBody DeviceMappingForm deviceMappingForm) {
        logger.info(LogMsg.to("msg:", "查询设备开始", "deviceMappingForm", deviceMappingForm));
        VerificationUtils.string("deviceId", deviceMappingForm.getDeviceId());
        List<DeviceMappingVo> list = iDeviceMappingFactory.regionMappingList(deviceMappingForm);
        logger.info(LogMsg.to("msg:", "查询设备结束", "更新数据"));
        return resp(list);
    }

}
