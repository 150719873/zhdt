package com.dotop.smartwater.project.third.server.meter.rest.service.nb2;


import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.INb2CommandFactory;
import com.dotop.smartwater.project.third.module.api.factory.INb2DeviceUplinkFactory;
import com.dotop.smartwater.project.third.module.api.factory.INb2OwnerFactory;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.DataBackForm;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.UserSynForm;
import com.dotop.smartwater.project.third.module.core.third.nb2.vo.DataBackVo;
import com.dotop.smartwater.project.third.module.core.third.nb2.vo.UserSynVo;
import com.dotop.smartwater.project.third.module.core.utils.Nb2Utils;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dotop.smartwater.project.third.module.core.constants.DockingConstants.*;

/**
 *
 */
@RestController
@RequestMapping("/nb2")
public class Nb2RemoteController implements BaseController<BaseForm> {

    private static final Logger logger = LogManager.getLogger(Nb2RemoteController.class);

    @Autowired
    INb2DeviceUplinkFactory iNb2DeviceUplinkFactory;
    @Autowired
    INb2OwnerFactory iNb2OwnerFactory;
    @Autowired
    IMeterDockingFactory iMeterDockingFactory;
    @Autowired
    INb2CommandFactory iNb2CommandFactory;

    @PostMapping(value = "/userSyn.action", produces = GlobalContext.PRODUCES)
    public String userSyn(UserSynForm userSynForm) {
        logger.info(LogMsg.to("userSynForm", userSynForm));
        VerificationUtils.string("unitcode", userSynForm.getUnitcode());
        VerificationUtils.string("uname", userSynForm.getUname());
        VerificationUtils.string("password", userSynForm.getPassword());
        VerificationUtils.string("meterNo", userSynForm.getMeterno());
        VerificationUtils.string("type", userSynForm.getType());
        UserSynVo userSynVo = new UserSynVo();
        DockingForm dockingForm = Nb2Utils.getUserInfo(userSynForm);
        if (iMeterDockingFactory.isRealUser(dockingForm)) {
            switch (userSynForm.getType()) {
                case REMOTE_USER_ADD:
                case REMOTE_USER_EDIT:
                    try {
                        VerificationUtils.string("userid", userSynForm.getUserid());
                        userSynVo = iNb2OwnerFactory.addOwner(userSynForm);
                    } catch (FrameworkRuntimeException e) {
                        userSynVo.setType(REMOTE_FAIL);
                    }
                    break;
                case REMOTE_DEVICE_OPEN:
                case REMOTE_DEVICE_CLOSE:
                    try {
                        userSynVo = iNb2CommandFactory.sendCommand(userSynForm);
                    } catch (FrameworkRuntimeException e) {
                        userSynVo.setType(REMOTE_FAIL);
                    }
                    break;
                default:
                    break;
            }
        } else {
            userSynVo.setType(REMOTE_FAIL);
        }
        logger.info(LogMsg.to("userSynVo", userSynVo));
        return resp(userSynVo);
    }

    @PostMapping(value = "/getDataBack.action", produces = GlobalContext.PRODUCES)
    public String getDataBack(DataBackForm dataBackForm) {
        logger.info(LogMsg.to("dataBackForm", dataBackForm));
        VerificationUtils.string("unitcode", dataBackForm.getUnitcode());
        VerificationUtils.string("uname", dataBackForm.getUname());
        VerificationUtils.string("password", dataBackForm.getPassword());
        VerificationUtils.string("yf", dataBackForm.getYf());
        DataBackVo dataBackVo = BeanUtils.copy(dataBackForm, DataBackVo.class);
        DockingForm dockingForm = Nb2Utils.getUserInfo(dataBackForm);
        if (iMeterDockingFactory.isRealUser(dockingForm)) {
            dataBackVo.setData(iNb2DeviceUplinkFactory.listDeviceUplink(dataBackForm));
        } else {
            return resp();
        }
        logger.info(LogMsg.to("dataBackVo", dataBackVo));
        return resp(dataBackVo);
    }

    @PostMapping(value = "/getDataBack2.action", produces = GlobalContext.PRODUCES)
    public String getDataBack2(DataBackForm dataBackForm) {
        logger.info(LogMsg.to("dataBackForm", dataBackForm));
        VerificationUtils.string("unitcode", dataBackForm.getUnitcode());
        VerificationUtils.string("uname", dataBackForm.getUname());
        VerificationUtils.string("password", dataBackForm.getPassword());
        VerificationUtils.string("meterNo", dataBackForm.getMeterNo());
        VerificationUtils.string("meterStime", dataBackForm.getMeterStime());
        VerificationUtils.string("meterEtime", dataBackForm.getMeterEtime());
        DataBackVo dataBackVo = BeanUtils.copy(dataBackForm, DataBackVo.class);
        DockingForm dockingForm = Nb2Utils.getUserInfo(dataBackForm);
        if (iMeterDockingFactory.isRealUser(dockingForm)) {
            dataBackVo.setData(iNb2DeviceUplinkFactory.listDeviceUplink(dataBackForm));
        } else {
            return resp();
        }
        logger.info(LogMsg.to("dataBackVo", dataBackVo));
        return resp(dataBackVo);
    }

}
