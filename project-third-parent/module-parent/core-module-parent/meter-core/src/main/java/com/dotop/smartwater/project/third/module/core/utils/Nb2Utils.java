package com.dotop.smartwater.project.third.module.core.utils;

import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.DataBackForm;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.UserSynForm;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Nb2Utils {

    public static DockingForm getUserInfo(DataBackForm dataBackForm) {
        DockingForm dockingForm = new DockingForm();
        dockingForm.setCategory(DockingConstants.NB2);
        dockingForm.setType(DockingConstants.REMOTE_NB_USER_INFO);
        dockingForm.setPassword(dataBackForm.getPassword());
        dockingForm.setUsername(dataBackForm.getUname());
        dockingForm.setCode(dataBackForm.getUnitcode());
        return dockingForm;
    }

    public static DockingForm getUserInfo(UserSynForm userSynForm) {
        DockingForm dockingForm = new DockingForm();
        dockingForm.setCategory(DockingConstants.NB2);
        dockingForm.setType(DockingConstants.REMOTE_NB_USER_INFO);
        dockingForm.setPassword(userSynForm.getPassword());
        dockingForm.setUsername(userSynForm.getUname());
        dockingForm.setCode(userSynForm.getUnitcode());
        return dockingForm;
    }

    public static DeviceUplinkForm dataBackToDeviceUplink(DataBackForm dataBackForm) {
        DeviceUplinkForm deviceUplinkForm = new DeviceUplinkForm();
        if (StringUtils.isNotBlank(dataBackForm.getYf())) {
            deviceUplinkForm.setUplinkDate(DateUtils.parse(dataBackForm.getYf(), DateUtils.YYYYMM));
        }
        if (StringUtils.isNotBlank(dataBackForm.getMeterStime()) && StringUtils.isNotBlank(dataBackForm.getMeterEtime())) {
            deviceUplinkForm.setStartDate(DateUtils.parseDate(dataBackForm.getMeterStime()));
            deviceUplinkForm.setEndDate(DateUtils.parseDate(dataBackForm.getMeterEtime()));
            deviceUplinkForm.setUserno(dataBackForm.getUserid());
        }
        deviceUplinkForm.setDevno(dataBackForm.getMeterNo());
        return deviceUplinkForm;
    }

    public static List<com.dotop.smartwater.project.third.module.core.third.nb2.vo.DeviceUplinkVo> waterToNb2(List<DeviceUplinkVo> deviceUplinkVos) {
        List<com.dotop.smartwater.project.third.module.core.third.nb2.vo.DeviceUplinkVo> deviceUplinkVoList = new ArrayList<>();
        deviceUplinkVos.forEach(deviceUplinkVo -> {
            com.dotop.smartwater.project.third.module.core.third.nb2.vo.DeviceUplinkVo deviceUplinkVo1 = new com.dotop.smartwater.project.third.module.core.third.nb2.vo.DeviceUplinkVo();
            deviceUplinkVo1.setReadDate(DateUtils.formatDate(deviceUplinkVo.getUplinkDate()));
            deviceUplinkVo1.setMeterDegrees(deviceUplinkVo.getWater() == null? "0": (deviceUplinkVo.getWater() + ""));
            deviceUplinkVo1.setMeterno(deviceUplinkVo.getDevno());
            deviceUplinkVo1.setUserAddr(deviceUplinkVo.getDevaddr());
            deviceUplinkVo1.setValveStatus(deviceUplinkVo.getTapstatus());
            deviceUplinkVo1.setUsername(deviceUplinkVo.getUsername());
            deviceUplinkVoList.add(deviceUplinkVo1);
        });
        return deviceUplinkVoList;
    }

    public static OwnerForm userSynToOwner(UserSynForm userSynForm) {
        OwnerForm ownerForm = new OwnerForm();
        ownerForm.setUserno(userSynForm.getUserid());
        ownerForm.setUseraddr(userSynForm.getUserAddr());
        ownerForm.setUsername(userSynForm.getUsername());
        return ownerForm;
    }
}
