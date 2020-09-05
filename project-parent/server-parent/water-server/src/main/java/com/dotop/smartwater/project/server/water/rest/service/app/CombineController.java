package com.dotop.smartwater.project.server.water.rest.service.app;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.tool.IAppVersionFactory;
import com.dotop.smartwater.project.module.api.tool.IDeviceBatchFactory;
import com.dotop.smartwater.project.module.api.tool.IDeviceParametersFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.DeviceCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.*;
import com.dotop.smartwater.project.module.core.water.vo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**

 * @Date 2019年2月26日
 */
@RestController
@RequestMapping(value = "/combine")

public class CombineController implements BaseController<BaseForm> {

    private static final Logger LOGGER = LogManager.getLogger(CombineController.class);

    @Autowired
    private IDeviceFactory iDeviceFactory;

    private static final Integer USER_TYPE_PRODUCT = 3;
    private static final Integer USER_TYPE_ONLINE = 1;

    private static final String RESULT_CODE_PRODUCTED = "produced";

    /**
     * 判断设备是否存在
     *
     * @param form
     * @return
     */
    @PostMapping(value = "/isExist", produces = GlobalContext.PRODUCES)
    public String isExist(@RequestBody DeviceForm form) {
        VerificationUtils.string("devno", form.getDevno());

        DeviceVo vo = iDeviceFactory.findByDevNo(form.getDevno());

        if (vo == null) {
            return resp(ResultCode.Success, ResultCode.SUCCESS, null);
        } else {
            if (vo.getProcessStatus() == null) {
                return resp(ResultCode.Fail, "水表号已经被使用", null);
            }

            switch (vo.getProcessStatus()) {
                case DeviceCode.DEVICE_PROCESS_STATUS_PRODUCT:
                    return resp(RESULT_CODE_PRODUCTED, "该号的水表已经被生产了", null);
                case DeviceCode.DEVICE_PROCESS_STATUS_ONLINE:
                    return resp(ResultCode.Fail, "水表号已经上线了", null);
                default:
                    return resp(ResultCode.Fail, "没知的生产过程状态", null);
            }
        }
    }

    /**
     * 生产绑定错误时，解绑
     *
     * @param form
     * @return
     */
    @PostMapping(value = "/unBind", produces = GlobalContext.PRODUCES)
    public String unBind(@RequestBody DeviceForm form) {
        VerificationUtils.string("devno", form.getDevno());

        DeviceVo vo = iDeviceFactory.findByDevNo(form.getDevno());
        if (vo == null) {
            return resp(ResultCode.Fail, "水表不存在不能解绑", null);
        } else {
            if (vo.getProcessStatus() == null) {
                return resp(ResultCode.Fail, "过程状态为空,不能解绑", null);
            }
            switch (vo.getProcessStatus()) {
                case DeviceCode.DEVICE_PROCESS_STATUS_PRODUCT:
                    //生产状态才可以解绑
                    form.setDevid(vo.getDevid());
                    form.setFlag(DeviceCode.DEVICE_FLAG_DELETE);
                    iDeviceFactory.update(form);
                    return resp(ResultCode.Success, ResultCode.SUCCESS, null);
                case DeviceCode.DEVICE_PROCESS_STATUS_ONLINE:
                    return resp(ResultCode.Fail, "水表已经上线了,不能解绑", null);
                default:
                    return resp(ResultCode.Fail, "没知的生产过程状态，", null);
            }
        }
    }

    /**
     * 录入设备信息
     *
     * @param form
     * @return
     */
    @PostMapping(value = "/setDeviceInfo", produces = GlobalContext.PRODUCES)
    public String setDeviceInfo(@RequestBody DeviceForm form) {
        VerificationUtils.doubles("water", form.getWater());
        VerificationUtils.string("devno", form.getDevno());
        VerificationUtils.string("unit", form.getUnit());
        VerificationUtils.string("mode", form.getMode());
        VerificationUtils.string("iccid", form.getIccid());
        VerificationUtils.longs("RSSI", form.getRssi(),false,-999999L,999999L);
        VerificationUtils.doubles("SNR", form.getLsnr(),false,-999999d,999999d);
        VerificationUtils.string("deveui", form.getDeveui());
        VerificationUtils.string("reportingCycle", form.getReportingCycle());

        UserVo user = AuthCasClient.getUser();
        if (USER_TYPE_PRODUCT.equals(user.getUsertype())) {
            DeviceVo vo = iDeviceFactory.findByDevNo(form.getDevno());
            if (vo == null) {
                vo = iDeviceFactory.findByDevEui(form.getDeveui());
                if (vo != null) {
                    throw new FrameworkRuntimeException(ResultCode.Fail, "IMEI号已存在");
                }

                //需要补全的数据
                form.setBeginvalue(form.getWater());
                form.setTypeid(DeviceCode.DEVICE_TYPE_ELECTRONIC);
                form.setTapstatus(DeviceCode.DEVICE_TAP_STATUS_ON);
                form.setTaptype(DeviceCode.DEVICE_TAP_TYPE_NO_TAP);
                //生产阶段（离线）
                form.setStatus(DeviceCode.DEVICE_STATUS_OFFLINE);

                form.setKind(DeviceCode.DEVICE_KIND_REAL);
                form.setPid(DeviceCode.DEVICE_PARENT);
                form.setFlag(DeviceCode.DEVICE_FLAG_EDIT);

                form.setProcessStatus(DeviceCode.DEVICE_PROCESS_STATUS_PRODUCT);
                form.setAccesstime(new Date());
                iDeviceFactory.add(form);
                return resp(ResultCode.Success, ResultCode.SUCCESS, null);
            } else {
                if (vo.getProcessStatus() == null) {
                    return resp(ResultCode.Fail, "水表号已经被使用", null);
                }
                switch (vo.getProcessStatus()) {
                    case DeviceCode.DEVICE_PROCESS_STATUS_PRODUCT:
                        return resp(ResultCode.Fail, "该号的水表已经被生产了", null);
                    case DeviceCode.DEVICE_PROCESS_STATUS_ONLINE:
                        return resp(ResultCode.Fail, "水表已经上线了", null);
                    default:
                        return resp(ResultCode.Fail, "没知的生产过程状态", null);
                }
            }
        } else {
            return resp(ResultCode.Fail, "用户类型是生产才有这权限", null);
        }
    }

    /**
     * 上线
     *
     * @param form
     * @return
     */
    @PostMapping(value = "/online", produces = GlobalContext.PRODUCES)
    public String online(@RequestBody DeviceForm form) {
        VerificationUtils.doubles("water", form.getWater());
        VerificationUtils.string("devno", form.getDevno());
        VerificationUtils.string("unit", form.getUnit());
        VerificationUtils.string("reportingCycle", form.getReportingCycle());

        UserVo user = AuthCasClient.getUser();
        if (USER_TYPE_ONLINE.equals(user.getUsertype())) {
            DeviceVo vo = iDeviceFactory.findByDevNo(form.getDevno());
            if (vo != null) {
                switch (vo.getProcessStatus()) {
                    case DeviceCode.DEVICE_PROCESS_STATUS_PRODUCT:
                        //需要补全的数据
                        form.setBeginvalue(form.getWater());
                        form.setDevid(vo.getDevid());
                        form.setProcessStatus(DeviceCode.DEVICE_PROCESS_STATUS_ONLINE);
                        iDeviceFactory.update(form);
                        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
                    case DeviceCode.DEVICE_PROCESS_STATUS_ONLINE:
                        return resp(ResultCode.Fail, "水表已经上线了", null);
                    default:
                        return resp(ResultCode.Fail, "没知的生产过程状态", null);
                }
            } else {
                return resp(ResultCode.Fail, "水表不存在", null);
            }
        } else {
            return resp(ResultCode.Fail, "用户类型是水司才有这权限", null);
        }
    }

}
