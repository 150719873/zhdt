package com.dotop.pipe.server.rest.service.devicedata;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.data.receiver.api.factory.IKBLReceiveFactory;
import com.dotop.pipe.web.api.factory.devicedata.IMeterReadingFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;

@RestController()
@RequestMapping("/meterReading")
public class MeterReadingController implements BaseController<BaseForm> {
    private static final Logger logger = LogManager.getLogger(MeterReadingController.class);

    @Autowired
    private IKBLReceiveFactory iKBLReceiveFactory;

    @Autowired
    private IMeterReadingFactory iMeterReadingFactory;

    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody String rdata) {
        logger.debug(LogMsg.to("rdata", rdata));
        JSONObject rddata = com.alibaba.fastjson.JSON.parseObject(rdata);
        String sensorCode = rddata.getString("device_code");
        if (StringUtils.isNotBlank(sensorCode)) {
            logger.debug(LogMsg.to("device_code", sensorCode));
            iKBLReceiveFactory.handle(sensorCode, rddata);
        }
        return resp();
    }

    /**
     * 分页查询
     */
    @PostMapping(value = "/devicePage", produces = GlobalContext.PRODUCES)
    public String meterReadingDevicePage(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", deviceForm));
        Integer page = deviceForm.getPage();
        Integer pageSize = deviceForm.getPageSize();
        String productCategory = deviceForm.getProductCategory();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        // VerificationUtils.string("productCategory", productCategory);
        Pagination<DeviceVo> pagination = iMeterReadingFactory.devicePage(deviceForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(pagination);
    }

    /**
     * 分页查询
     */
    @PostMapping(value = "/areaPage", produces = GlobalContext.PRODUCES)
    public String meterReadingAreaPage(@RequestBody AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", areaForm));
        Integer page = areaForm.getPage();
        Integer pageSize = areaForm.getPageSize();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        Pagination<DeviceVo> pagination = iMeterReadingFactory.areaPage(areaForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(pagination);
    }

}