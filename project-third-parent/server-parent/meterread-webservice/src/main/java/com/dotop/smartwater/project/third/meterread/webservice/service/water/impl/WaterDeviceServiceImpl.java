package com.dotop.smartwater.project.third.meterread.webservice.service.water.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.meterread.webservice.dao.water.IWaterDao;
import com.dotop.smartwater.project.third.meterread.webservice.service.water.IWaterDeviceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterDeviceServiceImpl implements IWaterDeviceService {

    private static final Logger LOGGER = LogManager.getLogger(WaterDeviceServiceImpl.class);

    @Autowired
    private IWaterDao iWaterDao;

    @Override
    public Pagination<DeviceVo> find(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageCount());
            List<DeviceVo> list = iWaterDao.getList(deviceDto);
            // 拼接数据返回
            return new Pagination<>(deviceBo.getPage(), deviceBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceVo get(DeviceBo deviceBo) {
        try {
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            DeviceVo device = iWaterDao.getDevice(deviceDto);
            return device;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
