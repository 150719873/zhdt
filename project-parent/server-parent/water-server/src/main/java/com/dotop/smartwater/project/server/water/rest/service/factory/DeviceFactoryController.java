package com.dotop.smartwater.project.server.water.rest.service.factory;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 * @description 第三方接口（批量新增、修改、获取设备）
 * @date 2019-10-11
 */
@RestController

@RequestMapping("/device")
public class DeviceFactoryController extends FoundationController implements BaseController<DeviceForm> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceFactoryController.class);

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @PostMapping(value = "/adds", produces = GlobalContext.PRODUCES)
    public String adds(@RequestBody List<DeviceForm> deviceForms) {
        LOGGER.info(LogMsg.to("msg:", "批量新增设备开始", "deviceForms", deviceForms));
        // 校验
        if (deviceForms == null || deviceForms.isEmpty()) {
            return resp(ResultCode.Fail, "设备列表不能为空", null);
        }
        for (DeviceForm deviceForm : deviceForms) {
            String devno = deviceForm.getDevno();
            String devid = deviceForm.getDevid();
            String typeid = deviceForm.getTypeid();
            String mode = deviceForm.getMode();
            String kind = deviceForm.getKind();
            String factory = deviceForm.getFactory();
            String deveui = deviceForm.getDeveui();
            String productName = deviceForm.getProductName();

            VerificationUtils.string("devno", devno);
            VerificationUtils.string("devid", devid);
            VerificationUtils.string("typeid", typeid);
            VerificationUtils.string("mode", mode);
            VerificationUtils.string("kind", kind);
            VerificationUtils.string("factory", factory);
            VerificationUtils.string("deveui", deveui);
            VerificationUtils.string("productName", productName);
        }
        Integer count = iDeviceFactory.addDeviceByThird(deviceForms);
        auditLog(OperateTypeEnum.METER_MANAGEMENT, "批量新增设备", "新增设备数量", count);
        LOGGER.info(LogMsg.to("msg:", "批量新增设备结束", "deviceForms", deviceForms));
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @PostMapping(value = "/edits", produces = GlobalContext.PRODUCES)
    public String edits(@RequestBody List<DeviceForm> deviceForms) {
        LOGGER.info(LogMsg.to("msg:", "批量编辑设备开始", "deviceForms", deviceForms));
        // 校验
        if (deviceForms == null || deviceForms.isEmpty()) {
            return resp(ResultCode.Fail, "设备列表不能为空", null);
        }
        for (DeviceForm deviceForm : deviceForms) {
            String devid = deviceForm.getDevid();
            VerificationUtils.string("devid", devid);
        }
        List<String> list = iDeviceFactory.editDeviceByThird(deviceForms);
        auditLog(OperateTypeEnum.METER_MANAGEMENT, "批量修改设备", "修改设备数量", deviceForms.size());
        LOGGER.info(LogMsg.to("msg:", "批量编辑设备结束", "deviceForms", deviceForms));
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }

    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody DeviceForm deviceForm) {
        LOGGER.info(LogMsg.to("msg:", "查询设备开始", "deviceForm", deviceForm));
        Integer page = deviceForm.getPage();
        Integer pageCount = deviceForm.getPageCount();
        String devno = deviceForm.getDevno();
        String deveui = deviceForm.getDeveui();
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageCount", pageCount);
        VerificationUtils.string("devno", devno, true);
        VerificationUtils.string("deveui", deveui, true);

        Pagination<DeviceVo> pagination = iDeviceFactory.pageByThird(deviceForm);
        LOGGER.info(LogMsg.to("msg:", "查询设备结束", "pagination", pagination));
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String delByThird(@RequestBody DeviceForm deviceForm) {
        LOGGER.info(LogMsg.to("msg:", "第三方删除设备开始", "deviceForm", deviceForm));
        // 校验
        String deveui = deviceForm.getDeveui();
        VerificationUtils.string("deveui", deveui);

        String devid = iDeviceFactory.deleteByThird(deviceForm);
        auditLog(OperateTypeEnum.METER_MANAGEMENT, "第三方修改设备", "设备ID", devid);
        LOGGER.info(LogMsg.to("msg:", "第三方删除设备结束", "deviceForm", deviceForm));
        return resp(ResultCode.Success, ResultCode.SUCCESS, devid);
    }

}
