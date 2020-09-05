package com.dotop.smartwater.project.module.service.device.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceSubscribeBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceSubscribeDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceSubscribeVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceSubscribeDao;
import com.dotop.smartwater.project.module.service.device.IDeviceSubscribeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**

 * @date 2019/2/25.
 */
@Service
public class DeviceSubscribeServiceImpl implements IDeviceSubscribeService {

    private static final Logger LOGGER = LogManager.getLogger(DeviceSubscribeServiceImpl.class);

    @Autowired
    private IDeviceSubscribeDao iDeviceSubscribeDao;

    @Override
    public DeviceSubscribeVo get(DeviceSubscribeBo bo) {
        try {
            return iDeviceSubscribeDao.get(BeanUtils.copy(bo, DeviceSubscribeDto.class));
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceSubscribeVo add(DeviceSubscribeBo bo) {
        try {
            iDeviceSubscribeDao.add(BeanUtils.copy(bo, DeviceSubscribeDto.class));
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(DeviceSubscribeBo bo) {
        try {
            iDeviceSubscribeDao.del(BeanUtils.copy(bo, DeviceSubscribeDto.class));
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
