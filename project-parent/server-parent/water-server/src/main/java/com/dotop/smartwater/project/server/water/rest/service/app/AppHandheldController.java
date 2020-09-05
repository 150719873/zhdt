package com.dotop.smartwater.project.server.water.rest.service.app;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.app.IAppHandheldFactory;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.revenue.IOwnerFactory;
import com.dotop.smartwater.project.module.api.tool.IAppVersionFactory;
import com.dotop.smartwater.project.module.api.tool.IDeviceBatchFactory;
import com.dotop.smartwater.project.module.api.tool.IDeviceParametersFactory;
import com.dotop.smartwater.project.module.core.app.vo.AppHandheldVo;
import com.dotop.smartwater.project.module.core.third.form.iot.DeviceReplaceForm;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterDataForm;
import com.dotop.smartwater.project.module.core.third.form.iot.UserEntryForm;
import com.dotop.smartwater.project.module.core.water.constants.AppCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.*;
import com.dotop.smartwater.project.module.core.water.vo.AppVersionVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 * @Date 2019年2月26日
 * <p>
 * 原手持机登录 com.dotop.water.rest.IotServerRest
 */
@RestController

@RequestMapping(value = "/rest")
public class AppHandheldController implements BaseController<BaseForm> {

    private static final Logger LOGGER = LogManager.getLogger(AppHandheldController.class);

    @Autowired
    private IAppHandheldFactory iAppHandheldFactory;

    @Autowired
    private IAppVersionFactory iAppVersionFactory;

    @Autowired
    private IOwnerFactory iOwnerFactory;

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Autowired
    private IDeviceParametersFactory iDeviceParametersFactory;
    
    @Autowired
	private IDeviceBatchFactory iDeviceBatchFactory;

    /**
     * 获取用户、设备信息
     *【根据不同的查询条件（即keywords）获得用户、设备信息】
     * @param ownerForm
     * @return
     */
    @PostMapping(value = "/getDevice", produces = GlobalContext.PRODUCES)
    public String getDevice(@RequestBody OwnerForm ownerForm) {
        VerificationUtils.string("keywords", ownerForm.getKeywords()); //【这是在干嘛？】
        Map<String, Object> data = new HashMap<>();
        OwnerVo owner = iOwnerFactory.getkeyWordOwner(ownerForm);
        DeviceForm dform = new DeviceForm();
        dform.setKeywords(ownerForm.getKeywords());
        DeviceVo device = iDeviceFactory.getkeyWordDevice(dform);
        data.put("owner", owner);
        data.put("device", device);
        return resp(ResultCode.Success, ResultCode.SUCCESS, data);
    }
    
    
    /**
     * 获取用户、设备信息
     *
     * @param
     * @return
     */
    @PostMapping(value = "/findByNfcTagDev", produces = GlobalContext.PRODUCES)
    public String findByNfcTagDev(@RequestBody DeviceForm form) {
        VerificationUtils.string("nfcTag", form.getNfcTag());
        DeviceVo vo = iDeviceFactory.findByNfcTagDev(form);
        return JSONUtils.toJSONString(new AppHandheldVo(AppCode.SUCCESS, "success", vo));
    }
    
    

    // 获取水司
    @PostMapping(value = "/getEnterprise", produces = GlobalContext.PRODUCES)
    public String getEnterprise() {
        LOGGER.info("[getEnterprise] request ");
        AppHandheldVo appHandheldVo = iAppHandheldFactory.getEnterprise();
        return JSONUtils.toJSONString(appHandheldVo);
    }

    // 登录
    @PostMapping(value = "/login", produces = GlobalContext.PRODUCES)
    public String login(@RequestBody UserEntryForm userEntryForm) {
        LOGGER.info("[login] request :{}", userEntryForm);
        if (userEntryForm == null) {
            return resp(AppCode.REQUEST_DATA_NULL, AppCode.getMessage(AppCode.REQUEST_DATA_NULL), null);
        } else {
            if (StringUtils.isEmpty(userEntryForm.getAccount()) || StringUtils.isEmpty(userEntryForm.getPassword())) {
                return resp(AppCode.ACCOUNT_OR_PASSWORD_NULL, AppCode.getMessage(AppCode.ACCOUNT_OR_PASSWORD_NULL),
                        null);
            }
            if (userEntryForm.getEnterpriseid() == null) {
                return resp(AppCode.ENTERPRISEID_NULL, AppCode.getMessage(AppCode.ENTERPRISEID_NULL), null);
            }
            AppHandheldVo appHandheldVo = iAppHandheldFactory.login(userEntryForm);
            return JSONUtils.toJSONString(appHandheldVo);
        }
    }

    // 接收 手持机 设备eui , 水表id 和 水表 初始读数
    @PostMapping(value = "/register", produces = GlobalContext.PRODUCES)
    public String register(@RequestBody MeterDataForm meterDataForm) {
        LOGGER.info("[register] request :{}", meterDataForm);
        if (meterDataForm == null) {
            return resp(AppCode.REQUEST_DATA_NULL, AppCode.getMessage(AppCode.REQUEST_DATA_NULL), null);
        } else if (StringUtils.isEmpty(meterDataForm.getDeveui())) {
            return resp(AppCode.DEVEUI_NULL, AppCode.getMessage(AppCode.DEVEUI_NULL), null);
        } else if (StringUtils.isEmpty(meterDataForm.getDevno())) {
            return resp(AppCode.DEVNO_NULL, AppCode.getMessage(AppCode.DEVNO_NULL), null);
        } else if (StringUtils.isEmpty(meterDataForm.getMeter())) {
            return resp(AppCode.METER_NULL, AppCode.getMessage(AppCode.METER_NULL), null);
        } 
        AppHandheldVo appHandheldVo = iAppHandheldFactory.register(meterDataForm);
        return JSONUtils.toJSONString(appHandheldVo);
    }

    // 获取水表是否已绑定
    @PostMapping(value = "/isBind", produces = GlobalContext.PRODUCES)
    public String isBind(@RequestBody MeterDataForm meterDataForm) {
        LOGGER.info("[isBind] request :{}", meterDataForm);
        if (meterDataForm == null) {
            return resp(AppCode.REQUEST_DATA_NULL, AppCode.getMessage(AppCode.REQUEST_DATA_NULL), null);
        }
        if (StringUtils.isEmpty(meterDataForm.getDeveui())) {
            return resp(AppCode.DEVEUI_NULL, AppCode.getMessage(AppCode.DEVEUI_NULL), null);
        }
        AppHandheldVo appHandheldVo = iAppHandheldFactory.isBind(meterDataForm);
        return JSONUtils.toJSONString(appHandheldVo);
    }

    // 接收 手持机 手动抄表 上传数据 设备eui 和 水表读数
    @PostMapping(value = "/manualMeterRead", produces = GlobalContext.PRODUCES)
    public String manualMeterRead(@RequestBody MeterDataForm meterDataForm) {
        LOGGER.info(LogMsg.to("[manualMeterRead] request :{}", meterDataForm));
        if (meterDataForm == null) {
            return resp(AppCode.REQUEST_DATA_NULL, AppCode.getMessage(AppCode.REQUEST_DATA_NULL), null);
        } else {
            if (StringUtils.isEmpty(meterDataForm.getDeveui())) {
                return resp(AppCode.DEVEUI_NULL, AppCode.getMessage(AppCode.DEVEUI_NULL), null);
            } else if (StringUtils.isEmpty(meterDataForm.getMeter())) {
                return resp(AppCode.METER_NULL, AppCode.getMessage(AppCode.METER_NULL), null);
            } else if (StringUtils.isEmpty(meterDataForm.getToken())) {
                return resp(AppCode.TOKEN_NULL, AppCode.getMessage(AppCode.TOKEN_NULL), null);
            } else if (StringUtils.isEmpty(meterDataForm.getUserid())) {
                return resp(AppCode.USERID_NULL, AppCode.getMessage(AppCode.USERID_NULL), null);
            }

            AppHandheldVo appHandheldVo = iAppHandheldFactory.manualMeterRead(meterDataForm);
            return JSONUtils.toJSONString(appHandheldVo);
        }

    }

    // 解除水表与表号的绑定
    @PostMapping(value = "/unBind", produces = GlobalContext.PRODUCES)
    public String unBind(@RequestBody MeterDataForm meterDataForm) {
        String logMsg = LogMsg.to("[unBind] request :{}", meterDataForm);
        LOGGER.info(logMsg);

        if (meterDataForm == null) {
            return resp(AppCode.REQUEST_DATA_NULL, AppCode.getMessage(AppCode.REQUEST_DATA_NULL), null);
        }
        if (StringUtils.isEmpty(meterDataForm.getDeveui())) {
            return resp(AppCode.DEVEUI_NULL, AppCode.getMessage(AppCode.DEVEUI_NULL), null);
        }

        AppHandheldVo appHandheldVo = iAppHandheldFactory.unBind(meterDataForm);
        return JSONUtils.toJSONString(appHandheldVo);
    }

    /**
     * 获取app最新版本数据
     *
     * @param appVersionForm
     * @return
     */
    @PostMapping(value = "/getLatest", produces = GlobalContext.PRODUCES)
    public String getLatest(@RequestBody AppVersionForm appVersionForm) {
        String logMsg = LogMsg.to("[getLatest] request :{}", appVersionForm);
        LOGGER.info(logMsg);
        String code = appVersionForm.getCode();
        VerificationUtils.string("code", code);

        AppVersionVo appVersionVo = iAppVersionFactory.getLatest(appVersionForm);
        if (appVersionVo != null && StringUtils.isNotBlank(appVersionVo.getId())) {
            return resp(ResultCode.Success, ResultCode.SUCCESS, appVersionVo);
        } else {
            return resp(ResultCode.NO_SEARCH_DATA, ResultCode.getMessage(ResultCode.NO_SEARCH_DATA), null);
        }
    }

    
    /**
     * 设备批次列表（“生产”接口）
     * <p>
     * 对接app接口
     */
    @PostMapping(value = "/deviceBatchList", produces = GlobalContext.PRODUCES)
    public String deviceBatchList(DeviceBatchForm form) {
        String logMsg = LogMsg.to("[deviceBatchList] request :{}", form);
        LOGGER.info(logMsg);
        // 验证
        List<DeviceParametersVo> list = iDeviceBatchFactory.noEndList(form);
        LOGGER.info(LogMsg.to("msg:", "查询列表结束"));
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }
    
    
    /**
     * 设备参数列表查询（“样品”接口）
     * <p>
     * 对接app接口
     */
    @PostMapping(value = "/paremetersList", produces = GlobalContext.PRODUCES)
    public String paremetersList(DeviceParametersForm deviceParametersForm) {
        String logMsg = LogMsg.to("[paremetersList] request :{}", deviceParametersForm);
        LOGGER.info(logMsg);
        // 验证
        List<DeviceParametersVo> list = iDeviceParametersFactory.list(deviceParametersForm);
        LOGGER.info(LogMsg.to("msg:", "查询列表结束"));
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }

    /**
     * 校准下发
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/down", produces = GlobalContext.PRODUCES)
    public String downCorrectionOper(@RequestBody DeviceDownlinkForm params) {
        // 参数校验
        VerificationUtils.string("deveui", params.getDeveui());
        VerificationUtils.string("measurementMethods", params.getMeasurementMethods());
        VerificationUtils.string("measurementValue", params.getMeasurementValue());
        VerificationUtils.string("measurementType", params.getMeasurementType());
        VerificationUtils.string("measurementUnit", params.getMeasurementUnit());
        VerificationUtils.string("networkInterval", params.getNetworkInterval());
        iDeviceFactory.downCorrectionOper(params);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }
    
    
    /**
     * 更换水表（更换已开户水表）
     * @param meterDataForm
     * @return
     */
    @PostMapping(value = "/replace", produces = GlobalContext.PRODUCES)
    public String replace(@RequestBody DeviceReplaceForm form) {
        LOGGER.info("[register] request :{}", form);
        if (form == null) {
            return resp(AppCode.REQUEST_DATA_NULL, AppCode.getMessage(AppCode.REQUEST_DATA_NULL), null);
        } else if (StringUtils.isEmpty(form.getScDeveui()) || StringUtils.isEmpty(form.getNwDeveui())) {
            return resp(AppCode.DEVEUI_NULL, AppCode.getMessage(AppCode.DEVEUI_NULL), null);
        } else if (form.getNwWater() == null || form.getNwWater() <= 0) {
            return resp(AppCode.WATER_METER_IS_NULL, AppCode.getMessage(AppCode.WATER_METER_IS_NULL), null);
        } else if (StringUtils.isEmpty(form.getReason())) {
            return resp(AppCode.IMSI_NULL, AppCode.getMessage(AppCode.IMSI_NULL), null);
        }
        AppHandheldVo appHandheldVo = iAppHandheldFactory.replace(form);
        return JSONUtils.toJSONString(appHandheldVo);
    }
    
    
    /**
     * 更换水表-记录（分页）
     * @param meterDataForm
     * @return
     */
    @PostMapping(value = "/replacePage", produces = GlobalContext.PRODUCES)
    public String replacePage(@RequestBody DeviceChangeForm form) {
        LOGGER.info("[replacePage] request :{}", form);
        if (form == null) {
            return resp(AppCode.REQUEST_DATA_NULL, AppCode.getMessage(AppCode.REQUEST_DATA_NULL), null);
        }
        VerificationUtils.integer("page", form.getPage());
		VerificationUtils.integer("pageCount", form.getPageCount());
		Pagination<DeviceChangeVo> pagination = iAppHandheldFactory.replacePage(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }
    
}
