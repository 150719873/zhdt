package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.INb2DeviceUplinkFactory;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceUplinkService;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.DataBackForm;
import com.dotop.smartwater.project.third.module.core.third.nb2.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.third.module.core.utils.Nb2Utils;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
public class Nb2DeviceUplinkFactoryImpl implements INb2DeviceUplinkFactory {

    private final static Logger LOGGER = LogManager.getLogger(Nb2DeviceUplinkFactoryImpl.class);

    @Autowired
    private IMeterDockingFactory iMeterDockingFactory;

    @Autowired
    private IWaterDeviceUplinkService iWaterDeviceUplinkService;

    @Override
    public List<DeviceUplinkVo> listDeviceUplink(DataBackForm dataBackForm) throws FrameworkRuntimeException {
        //转换上行
        DeviceUplinkForm deviceUplinkForm = Nb2Utils.dataBackToDeviceUplink(dataBackForm);
        DockingVo dockingVo = iMeterDockingFactory.get(Nb2Utils.getUserInfo(dataBackForm));
        deviceUplinkForm.setEnterpriseid(dockingVo.getEnterpriseid());
        DeviceUplinkBo deviceUplinkBo = BeanUtils.copy(deviceUplinkForm, DeviceUplinkBo.class);
        if (deviceUplinkBo.getUplinkDate() != null) {
            //根据月份查询
            deviceUplinkBo.setYearMonth(dataBackForm.getYf());
            return Nb2Utils.waterToNb2(iWaterDeviceUplinkService.listDegrees(deviceUplinkBo));
        } else if (deviceUplinkBo.getStartDate() != null && deviceUplinkBo.getEndDate() != null) {
            List<DeviceUplinkVo> deviceUplinkVos = new ArrayList<>();
            DateTime starDate = new DateTime(deviceUplinkBo.getStartDate().getTime());
            DateTime endDate = new DateTime(deviceUplinkBo.getEndDate().getTime());
            //计算开始时间和结束时间之间差了多少个月
            Integer between =  (endDate.getYear() - starDate.getYear()) * 12 + endDate.getMonthOfYear() - starDate.getMonthOfYear();
            deviceUplinkBo.setEndDate(DateUtils.day(deviceUplinkBo.getEndDate(), 1));
            for (Integer i = 0; i <= between; i++) {
                DateTime temp = new DateTime(DateUtils.month(starDate.toDate(), i));
                deviceUplinkBo.setYearMonth(DateUtils.format(temp.toDate(), DateUtils.YYYYMM));
                deviceUplinkVos.addAll(Nb2Utils.waterToNb2(iWaterDeviceUplinkService.listDegrees(deviceUplinkBo)));
            }
            return deviceUplinkVos;
        } else {
            LOGGER.error(LogMsg.to("dataBackForm:", dataBackForm));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "查询错误");
        }
    }
}
