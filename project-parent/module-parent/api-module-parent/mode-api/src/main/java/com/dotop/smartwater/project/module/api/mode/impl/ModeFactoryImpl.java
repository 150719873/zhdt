package com.dotop.smartwater.project.module.api.mode.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.mode.IModeFactory;
import com.dotop.smartwater.project.module.api.mode.inf.DongXinInf;
import com.dotop.smartwater.project.module.api.mode.inf.HatInf;
import com.dotop.smartwater.project.module.core.water.bo.customize.DownLinkDataBo;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.module.core.water.form.customize.DownlinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 各种通讯方式接口实现
 *
 * @date 2019/10/14.
 */
@Component
public class ModeFactoryImpl implements IModeFactory {

    private static final Logger LOGGER = LogManager.getLogger(ModeFactoryImpl.class);

    @Autowired
    private DongXinInf dongXinInf;

    @Autowired
    private HatInf hatInf;

    @Override
    public Map<String, String> dxDeviceTx(DeviceVo deviceVo, Integer command, String mode, DownlinkForm params) {
        Integer expire;
        DownLinkDataBo downLinkDataBo;
        switch (mode) {
            case ModeConstants.DX_LORA:
            case ModeConstants.DX_NB_DX:
            case ModeConstants.DX_NB_YD:
            case ModeConstants.DX_NB_LT:
                if (StringUtils.isNotBlank(params.getExpire())) {
                    expire = Integer.parseInt(params.getExpire());
                } else {
                    expire = 60 * 60 * 24;
                }
                downLinkDataBo = new DownLinkDataBo();
                switch (command) {
                    case TxCode.OpenCommand:
                    case TxCode.CloseCommand:
                    case TxCode.GetWaterCommand:
                        break;
                    case TxCode.MeterOper:
                        if (StringUtils.isBlank(params.getMeasurementMethods())
                                || StringUtils.isBlank(params.getMeasurementType())
                                || StringUtils.isBlank(params.getMeasurementUnit())
                                || StringUtils.isBlank(params.getNetworkInterval())) {
                            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR,
                                    BaseExceptionConstants.getMessage(BaseExceptionConstants.UNDEFINED_ERROR));
                        }
                        downLinkDataBo.setMeasureMethod(params.getMeasurementMethods());
                        downLinkDataBo.setMeasureType(params.getMeasurementType());
                        downLinkDataBo.setMeasureUnit(params.getMeasurementUnit());
                        downLinkDataBo.setMeasureValue(params.getMeasurementValue());
                        downLinkDataBo.setNetworkInterval(params.getNetworkInterval());
                        downLinkDataBo.setReason(params.getReason());
                        break;
                    case TxCode.Reset:
                        break;
                    case TxCode.SetLifeStatus:
                        VerificationUtils.string("life", params.getLife());
                        downLinkDataBo.setLife(params.getLife());
                        break;
                    case TxCode.ResetPeriod:
                        VerificationUtils.string("period", params.getPeriod());
                        downLinkDataBo.setPeriod(params.getPeriod());
                        break;
                    default:
                        throw new FrameworkRuntimeException(ResultCode.Fail, "东信水表不支持的下发指令");
                }
                break;
            default:
                throw new FrameworkRuntimeException(ResultCode.Fail, "没接入的通讯方式");
        }
        return dongXinInf.deviceDownLink(deviceVo, command, params.getValue(), expire, downLinkDataBo);
    }

    @Override
    public Map<String, String> hatDeviceTx(DeviceVo deviceVo, Integer command, String mode, String value) {
        return hatInf.deviceDownLink(deviceVo, command, mode, value);
    }
}
