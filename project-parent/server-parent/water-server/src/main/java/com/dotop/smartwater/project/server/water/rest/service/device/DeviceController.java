package com.dotop.smartwater.project.server.water.rest.service.device;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.mode.IModeFactory;
import com.dotop.smartwater.project.module.core.auth.form.SettlementForm;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.DeviceDownlinkForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.customize.DownlinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
 * 设备
 *

 * @date 2019/2/26.
 */
@RestController

@RequestMapping("/Device")
public class DeviceController extends FoundationController implements BaseController<DeviceForm> {
    private final static Logger LOG = LogManager.getLogger(DeviceController.class);
    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Autowired
    private IModeFactory iModeFactory;

    private static final String DEVNO = "devno";

    private static final String DEVEUI = "deveui";

    /**
     * 实时关阀
     *
     * @param deviceForm
     * @return
     */
    @PostMapping(value = "/Close", produces = GlobalContext.PRODUCES)
    public String close(@RequestBody DeviceForm deviceForm) {

        // 校验
        String devno = deviceForm.getDevno();
        VerificationUtils.string(DEVNO, devno);
        // 操作
        iDeviceFactory.openOrClose(deviceForm, TxCode.CloseCommand);
        auditLog(OperateTypeEnum.METER_MANAGEMENT, "实时关阀", "设备编号", devno);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);

    }

    /**
     * 实时开阀
     *
     * @param deviceForm
     * @return
     */
    @PostMapping(value = "/Open", produces = GlobalContext.PRODUCES)
    public String open(@RequestBody DeviceForm deviceForm) {
        // 校验
        String devno = deviceForm.getDevno();
        VerificationUtils.string(DEVNO, devno);
        // 操作
        iDeviceFactory.openOrClose(deviceForm, TxCode.OpenCommand);
        auditLog(OperateTypeEnum.METER_MANAGEMENT, "实时开阀", "设备编号", devno);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 实时抄表
     *
     * @param deviceForm
     * @return
     */
    @PostMapping(value = "/getDeviceWater", produces = GlobalContext.PRODUCES)
    public String getDeviceWater(@RequestBody DeviceForm deviceForm) {
        // 校验
        String devno = deviceForm.getDevno();
        VerificationUtils.string(DEVNO, devno);
        // 操作
        iDeviceFactory.getDeviceWater(deviceForm);
        auditLog(OperateTypeEnum.METER_MANAGEMENT, "实时抄表", "设备编号", devno);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }


    /**
     * 根据关键字查询设备信息
     *
     * @param deviceForm
     * @return
     */
    @PostMapping(value = "/getDeviceKeywords", produces = GlobalContext.PRODUCES)
    public String getDeviceKeywords(@RequestBody DeviceForm form) {
        // 校验
        VerificationUtils.string("keywords", form.getKeywords());
        DeviceVo vo = iDeviceFactory.getkeyWordDevice(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
    }


    /**
     * 水表信息查找
     *
     * @param deviceForm
     * @return
     */
    @PostMapping(value = "/getDeviceDetail", produces = GlobalContext.PRODUCES)
    public String getDeviceDetail(@RequestBody DeviceForm deviceForm) {

        String devno = deviceForm.getDevno();
        VerificationUtils.string(DEVNO, devno);
        DeviceVo deviceVo = iDeviceFactory.findByDevNo(devno);
        return resp(ResultCode.Success, ResultCode.SUCCESS, deviceVo);

    }

    /**
     * 水表信息查找
     *
     * @param deviceForm
     * @return
     */
    @PostMapping(value = "/getDeviceInfo", produces = GlobalContext.PRODUCES)
    public String getDeviceInfo(@RequestBody DeviceForm deviceForm) {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceFactory.getDeviceInfo(deviceForm));
    }

    /**
     * 校准上行历史查询
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/getUpCorrectionDatas", produces = GlobalContext.PRODUCES)
    public String getUpCorrectionDatas(@RequestBody Map<String, String> params) {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceFactory.getUpCorrectionDatas(params));
    }

    /**
     * 校准下行历史查询
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/getDownCorrectionDatas", produces = GlobalContext.PRODUCES)
    public String getDownCorrectionDatas(@RequestBody Map<String, String> params) {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceFactory.getDownCorrectionDatas(params));
    }

    /**
     * 校准下发
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/downCorrectionOper", produces = GlobalContext.PRODUCES)
    public String downCorrectionOper(@RequestBody DeviceDownlinkForm params) {
        // 参数校验
        VerificationUtils.string(DEVEUI, params.getDeveui());
        VerificationUtils.string("measurementMethods", params.getMeasurementMethods());
        VerificationUtils.string("measurementType", params.getMeasurementType());
        VerificationUtils.string("measurementUnit", params.getMeasurementUnit());
        VerificationUtils.string("networkInterval", params.getNetworkInterval(), true);
        if (params.getNetworkInterval() == null) {
            // lora设置不变
            params.setNetworkInterval("00");
        }

        iDeviceFactory.downCorrectionOper(params);
        auditLog(OperateTypeEnum.EQUIPMENT_CALIBRATION, "校准下发", "设备EUI", params.getDeveui());
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 复位下发
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/downRest", produces = GlobalContext.PRODUCES)
    public String downRest(@RequestBody DeviceDownlinkForm params) {
        VerificationUtils.string(DEVEUI, params.getDeveui());
        iDeviceFactory.downRest(params);
        auditLog(OperateTypeEnum.EQUIPMENT_CALIBRATION, "复位下发", "设备EUI", params.getDeveui());
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 设置生命状态
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/setLifeStatus", produces = GlobalContext.PRODUCES)
    public String setLifeStatus(@RequestBody DeviceDownlinkForm params) {
        VerificationUtils.string(DEVEUI, params.getDeveui());
        VerificationUtils.string("life", params.getLife());
        iDeviceFactory.setLifeStatus(params);
        auditLog(OperateTypeEnum.EQUIPMENT_CALIBRATION, "设置生命状态", "设备EUI", params.getDeveui(), "生命状态", params.getLife());
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 设置定时重启周期
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/resetPeriod", produces = GlobalContext.PRODUCES)
    public String resetPeriod(@RequestBody DeviceDownlinkForm params) {
        VerificationUtils.string(DEVEUI, params.getDeveui());
        VerificationUtils.string("period", params.getPeriod());

        iDeviceFactory.resetPeriod(params);
        auditLog(OperateTypeEnum.EQUIPMENT_CALIBRATION, "设置定时重启周期", "设备EUI", params.getDeveui(), "周期", params.getPeriod());
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody DeviceForm deviceForm) {
        // 校验
        String devno = deviceForm.getDevno();
        VerificationUtils.string(DEVNO, devno);

        iDeviceFactory.addDeviceByWeb(deviceForm);
        auditLog(OperateTypeEnum.METER_MANAGEMENT, "新增设备", "设备编号", devno);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @PostMapping(value = "/offLineCheck", produces = GlobalContext.PRODUCES)
    public String offLineCheck(@RequestBody SettlementForm settlementForm) {
        // 校验
        Integer offday = settlementForm.getOffday();
        VerificationUtils.integer("offday", offday);

        iDeviceFactory.offLineCheck(settlementForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 水表管理统一下行入口
     * @param params
     * @return
     */
    @PostMapping(value = "/downLinkEntrance", produces = GlobalContext.PRODUCES)
    public String downLinkEntrance(@RequestBody DownlinkForm params) {
        // 校验
        LOG.info(JSONUtils.toJSONString(params));
        Integer command = params.getCommand();
        VerificationUtils.integer("command", command);
        DeviceVo deviceVo = iDeviceFactory.findByDevId(params.getDevid());
        String mode = iDeviceFactory.verificationData(deviceVo, command);
        Map<String, String> result;
        switch (mode) {
            case ModeConstants.DX_LORA:
            case ModeConstants.DX_NB_DX:
            case ModeConstants.DX_NB_YD:
            case ModeConstants.DX_NB_LT:
                result = iModeFactory.dxDeviceTx(deviceVo, command, mode, params);
                break;
            case ModeConstants.HAT_NB:
            case ModeConstants.HAT_LORA:
                result = iModeFactory.hatDeviceTx(deviceVo, command, mode, params.getValue());
                break;
            //TODO 暂时没有接入别的协议
            default:
                throw new FrameworkRuntimeException(ResultCode.Fail, "没接入的通讯方式");
        }

        if (ResultCode.Success.equals(result.get(ModeConstants.RESULT_CODE))) {
            auditLog(OperateTypeEnum.METER_MANAGEMENT, TxCode.TxCodeMap.get(command), "设备编号", deviceVo.getDevno());
            return resp(ResultCode.Success, ResultCode.SUCCESS, null);
        } else {
            return resp(ResultCode.Fail, result.get(ModeConstants.RESULT_MSG), null);
        }
    }
}
