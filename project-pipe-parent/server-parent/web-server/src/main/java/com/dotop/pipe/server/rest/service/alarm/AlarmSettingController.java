package com.dotop.pipe.server.rest.service.alarm;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.core.form.AlarmSettingForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmSettingFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
@RestController()
@RequestMapping("/alarmSetting")
public class AlarmSettingController implements BaseController<AlarmSettingForm> {

    private final static Logger logger = LogManager.getLogger(AlarmSettingController.class);

    @Autowired
    private IAlarmSettingFactory iAlarmSettingFactory;

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody AlarmSettingForm alarmSettingForm) {
        logger.info(LogMsg.to("msg:", "预警值设置新增开始", "alarmSettingForm", alarmSettingForm));
        VerificationUtils.objList("properties", alarmSettingForm.getProperties());
        VerificationUtils.objList("deviceIds", alarmSettingForm.getDeviceIds());
        AlarmSettingVo alarmSettingVo = iAlarmSettingFactory.add(alarmSettingForm);
        logger.info(LogMsg.to("msg:", "预警值设置新增结束", "更新数据", alarmSettingVo));
        return resp(alarmSettingVo);
    }

    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", "预警设置分页查询开始", "deviceForm", deviceForm));
        Integer page = deviceForm.getPage();
        Integer pageSize = deviceForm.getPageSize();
        String productCategory = deviceForm.getProductCategory();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        // VerificationUtils.string("productCategory", productCategory);
        Pagination<DeviceVo> pagination = iAlarmSettingFactory.page(deviceForm);
        logger.info(LogMsg.to("msg:", "预警设置分页查询结束"));
        return resp(pagination);
    }

    /**
     * 预警设置详情
     */
    @Override
    @GetMapping(value = "/{deviceId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(AlarmSettingForm alarmSettingForm) {
        logger.info(LogMsg.to("msg:", "预警设置详情查询开始"));
        logger.info(LogMsg.to("id", alarmSettingForm.getDeviceId()));
        // 验证
        VerificationUtils.string("id", alarmSettingForm.getDeviceId());
        AlarmSettingVo alarmVo = iAlarmSettingFactory.get(alarmSettingForm);
        logger.info(LogMsg.to("msg:", "预警设置详情查询结束"));
        return resp(alarmVo);
    }

    /**
     * 上传execl 文件 执行导入操作
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public String upload(@RequestParam("uploadFile") MultipartFile file) {
        try {
            String filePath = this.iAlarmSettingFactory.importDate(file);
            return resp(filePath);
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("code", "exception");
            json.put("msg", "设备不存在或数据异常");
            return resp(json.toString());
        }
    }

}
