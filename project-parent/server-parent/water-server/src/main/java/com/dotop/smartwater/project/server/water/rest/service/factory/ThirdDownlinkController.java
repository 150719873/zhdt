package com.dotop.smartwater.project.server.water.rest.service.factory;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.mode.inf.DongXinInf;
import com.dotop.smartwater.project.module.core.third.form.middleware.WaterDownLoadForm;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.DownLinkDataBo;
import com.dotop.smartwater.project.module.core.water.constants.*;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.service.device.IDeviceDownlinkService;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**

 * @description 第三方接口（下行数据）
 * @date 2019-10-16
 */
@RestController

@RequestMapping("/device/third")
public class ThirdDownlinkController extends FoundationController implements BaseController<DeviceUplinkForm> {

    private final static String NOT_CHANGE = "00";
    //绝对计量
    private final static String ABSOLUTE_CAL = "02";

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdDownlinkController.class);

    @Autowired
    private DongXinInf dongXinInf;

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Autowired
    private IDeviceDownlinkService iDeviceDownlinkService;

    @PostMapping(value = "/downlink", produces = GlobalContext.PRODUCES)
    public String downlinkByThird(@RequestBody WaterDownLoadForm form) {
        LOGGER.info(LogMsg.to("msg:", "第三方发送下行开始", "waterDownLoadForm", form));
        // 校验
        String devid = form.getDevid();
        String water = form.getWater();
        String cycle = form.getCycle();
        Integer command = form.getCommand();
        VerificationUtils.string("devid", devid);
        VerificationUtils.integer("command", command);

        DeviceVo deviceVo = iDeviceFactory.findByDevId(devid);
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, ResultCode.getMessage(ResultCode.NOTFINDWATER));
        }
        String mode = DictionaryCode.getChildValue(deviceVo.getMode());

        DownLinkDataBo downLinkDataBo;
        Integer expire = 60 * 60 * 24;
        switch (mode) {
            case ModeConstants.DX_LORA:
            case ModeConstants.DX_NB_DX:
            case ModeConstants.DX_NB_YD:
                downLinkDataBo = new DownLinkDataBo();
                switch (command) {
                    case TxCode.OpenCommand:
                    case TxCode.CloseCommand:
                    case TxCode.GetWaterCommand:
                    case TxCode.Reset:
                        break;
                    case TxCode.SetLifeStatus:
                        VerificationUtils.string("life", form.getLife());
                        downLinkDataBo.setLife(form.getLife());
                        break;
                    case TxCode.ResetPeriod:
                        VerificationUtils.string("period", form.getPeriod());
                        downLinkDataBo.setPeriod(form.getPeriod());
                        break;
                    case TxCode.MeterOper:
                        if (StringUtils.isBlank(water)) {
                            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR,
                                    BaseExceptionConstants.getMessage(BaseExceptionConstants.UNDEFINED_ERROR));
                        }
                        downLinkDataBo.setMeasureMethod(ABSOLUTE_CAL);
                        downLinkDataBo.setMeasureValue(water);

                        downLinkDataBo.setMeasureType(NOT_CHANGE);
                        downLinkDataBo.setMeasureUnit(NOT_CHANGE);
                        downLinkDataBo.setNetworkInterval(NOT_CHANGE);
                        downLinkDataBo.setReason("第三方下发命令");
                        break;
                    default:
                        throw new FrameworkRuntimeException(ResultCode.Fail, "东信水表不支持的下发指令");
                }
                break;
            default:
                throw new FrameworkRuntimeException(ResultCode.Fail, "东信水表不支持的通讯方式");
        }

        Map<String, String> result = dongXinInf.deviceDownLink(deviceVo, command, null, expire, downLinkDataBo);
        if (ResultCode.Success.equals(result.get(ModeConstants.RESULT_CODE))) {
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } else {
            return resp(ResultCode.Fail, result.get(ModeConstants.RESULT_MSG), null);
        }
    }

    @PostMapping(value = "/downlink/result", produces = GlobalContext.PRODUCES)
    public String handleResult(@RequestBody Map<String,String> map) {
        String devid = map.get("devid");
        String clientid = map.get("clientid");
        String result = map.get("result");

        if(StringUtils.isBlank(devid)||StringUtils.isBlank(clientid)
                ||StringUtils.isBlank(result)){
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请传入必填参数");
        }

        DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
        deviceDownlinkBo.setClientid(clientid);
        List<DeviceDownlinkVo> deviceDownlinkList = iDeviceDownlinkService.findByClientId(deviceDownlinkBo);

        LOGGER.info("中间平台反馈 by client_id");
        if (deviceDownlinkList != null && deviceDownlinkList.size() > 0) {
            for (DeviceDownlinkVo deviceDownlink : deviceDownlinkList) {
                Integer re = Integer.parseInt(result);
                deviceDownlink.setStatus(re);
                deviceDownlink.setTxtime(new Date());
                deviceDownlinkBo = new DeviceDownlinkBo();
                BeanUtils.copyProperties(deviceDownlink, deviceDownlinkBo);
                iDeviceDownlinkService.update(deviceDownlinkBo);
                LOGGER.info("中间平台反馈 update downlink");
                //下行成功
                if(TxCode.DOWNLINK_STATUS_SUCCESS == re && StringUtils.isNotBlank(deviceDownlink.getDownlinkdata())){
                    DeviceForm deviceForm = new DeviceForm();
                    deviceForm.setDevid(devid);
                    switch (Integer.parseInt(deviceDownlink.getDownlinkdata())){
                        case TxCode.OpenCommand:
                            deviceForm.setTapstatus(DeviceCode.DEVICE_TAP_STATUS_ON);
                            iDeviceFactory.update(deviceForm);
                            break;
                        case TxCode.CloseCommand:
                            deviceForm.setTapstatus(DeviceCode.DEVICE_TAP_STATUS_OFF);
                            iDeviceFactory.update(deviceForm);
                            break;
                        //更新读数
                        case TxCode.MeterOper:
                            String reqData = deviceDownlink.getReqData();
                            if(StringUtils.isNotBlank(reqData)){
                                Map mapData = (Map) JSON.parse(reqData);
                                String water  = (String) (mapData.get("measureValue"));
                                if(StringUtils.isNotBlank(water)){
                                    deviceForm.setWater(Double.parseDouble(water));
                                    iDeviceFactory.update(deviceForm);
                                }
                            }
                            break;
                    }
                }
            }
        }
        map.put("result", "0");
        return JSONUtils.toJSONString(map);
    }
}
