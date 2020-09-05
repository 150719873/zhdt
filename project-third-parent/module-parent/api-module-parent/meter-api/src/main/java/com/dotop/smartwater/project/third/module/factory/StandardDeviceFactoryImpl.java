package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.module.api.factory.IStandardDeviceFactory;
import com.dotop.smartwater.project.third.module.api.service.IStandardDeviceService;
import com.dotop.smartwater.project.third.module.core.third.standard.form.DataForm;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardDeviceVo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
public class StandardDeviceFactoryImpl implements IStandardDeviceFactory {

    private final static Logger LOGGER = LogManager.getLogger(StandardDeviceFactoryImpl.class);

    @Autowired
    private IStandardDeviceService iStandardDeviceService;


    @Override
    public Pagination<StandardDeviceVo> pageDevice(DataForm dataForm) throws FrameworkRuntimeException {
        try {
            // TODO 根据月份或水表号查询当前时间的最新读数
            DeviceBo deviceBo = new DeviceBo();
            // TODO 登录缓存获取
            deviceBo.setEnterpriseid(dataForm.getEnterpriseid());
            if (dataForm.getDevno() != null) {
                deviceBo.setDevno(dataForm.getDevno());
            }
            Pagination<DeviceVo> devicePagination = iStandardDeviceService.pageDevice(deviceBo, dataForm.getPage(), dataForm.getPageCount());
            List<DeviceVo> devices = devicePagination.getData();
            long totalPageSize = devicePagination.getTotalPageSize();
            List<StandardDeviceVo> standardDevices = new ArrayList<>();
            devices.forEach(device -> {
                StandardDeviceVo standardDeviceVo = new StandardDeviceVo();
                standardDeviceVo.setDevaddr(device.getDevaddr() == null ? "" : device.getDevaddr());
                standardDeviceVo.setDeveui(device.getDeveui());
                standardDeviceVo.setDevno(device.getDevno());
                standardDeviceVo.setTapStatus(device.getTapstatus());
                standardDevices.add(standardDeviceVo);
            });
            Pagination<StandardDeviceVo> pagination = new Pagination<>(dataForm.getPage(), dataForm.getPageCount(), standardDevices, totalPageSize);
            return pagination;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
    }

}
