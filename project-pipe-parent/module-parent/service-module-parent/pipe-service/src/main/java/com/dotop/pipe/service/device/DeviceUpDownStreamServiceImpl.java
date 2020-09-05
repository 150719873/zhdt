package com.dotop.pipe.service.device;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.device.IDeviceUpDownStreamDao;
import com.dotop.pipe.api.service.device.IDeviceUpDownStreamService;
import com.dotop.pipe.core.bo.device.DeviceUpDownStreamBo;
import com.dotop.pipe.core.dto.decive.DeviceUpDownStreamDto;
import com.dotop.pipe.core.vo.device.DeviceUpDownStreamVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class DeviceUpDownStreamServiceImpl implements IDeviceUpDownStreamService {

    private static final Logger logger = LogManager.getLogger(DeviceUpDownStreamServiceImpl.class);

    @Autowired
    private IDeviceUpDownStreamDao iDeviceUpDownStreamDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public DeviceUpDownStreamVo add(DeviceUpDownStreamBo deviceUpDownStreamBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            DeviceUpDownStreamDto deviceUpDownStreamDto = BeanUtils.copyProperties(deviceUpDownStreamBo,
                    DeviceUpDownStreamDto.class);
            deviceUpDownStreamDto.setIsDel(isDel);
            deviceUpDownStreamDto.setId(UuidUtils.getUuid());
            iDeviceUpDownStreamDao.add(deviceUpDownStreamDto);
            return null;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }


    public void editAlarmProperty(DeviceUpDownStreamBo deviceUpDownStreamBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            DeviceUpDownStreamDto deviceUpDownStreamDto = BeanUtils.copyProperties(deviceUpDownStreamBo,
                    DeviceUpDownStreamDto.class);
            deviceUpDownStreamDto.setIsDel(isDel);
            iDeviceUpDownStreamDao.editAlarmProperty(deviceUpDownStreamDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(DeviceUpDownStreamBo deviceUpDownStreamBo) {
        try {
            Integer isDel = RootModel.DEL;
            DeviceUpDownStreamDto deviceUpDownStreamDto = BeanUtils.copyProperties(deviceUpDownStreamBo,
                    DeviceUpDownStreamDto.class);
            deviceUpDownStreamDto.setIsDel(isDel);
            iDeviceUpDownStreamDao.del(deviceUpDownStreamDto);
            return null;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> getParent(DeviceUpDownStreamBo deviceUpDownStreamBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            DeviceUpDownStreamDto deviceUpDownStreamDto = BeanUtils.copyProperties(deviceUpDownStreamBo,
                    DeviceUpDownStreamDto.class);
            deviceUpDownStreamDto.setIsDel(isDel);
            return iDeviceUpDownStreamDao.getParent(deviceUpDownStreamDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> getChild(DeviceUpDownStreamBo deviceUpDownStreamBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            DeviceUpDownStreamDto deviceUpDownStreamDto = BeanUtils.copyProperties(deviceUpDownStreamBo,
                    DeviceUpDownStreamDto.class);
            deviceUpDownStreamDto.setIsDel(isDel);
            List<DeviceVo> list = iDeviceUpDownStreamDao.getChild(deviceUpDownStreamDto);
            return list;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<DeviceUpDownStreamVo> page(DeviceUpDownStreamBo deviceUpDownStreamBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceUpDownStreamDto deviceUpDownStreamDto = BeanUtils.copyProperties(deviceUpDownStreamBo,
                    DeviceUpDownStreamDto.class);
            deviceUpDownStreamDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(deviceUpDownStreamBo.getPage(),
                    deviceUpDownStreamBo.getPageSize());
            // 请求产品
            // Map<String, ProductVo> productVoMap =
            // iWaterProductClient.storeMap(deviceBo.getProductCategory());
            // deviceDto.setProductIds(new ArrayList<>(productVoMap.keySet()));
            List<DeviceUpDownStreamVo> list = iDeviceUpDownStreamDao.list(deviceUpDownStreamDto);
            // 组装产品
            // WaterProductUtils.build(productVoMap, list, deviceBo.getProductCategory());
            Pagination<DeviceUpDownStreamVo> pagination = new Pagination<>(deviceUpDownStreamBo.getPageSize(),
                    deviceUpDownStreamBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }


    @Override
    public DeviceVo getForecast(DeviceUpDownStreamBo deviceUpDownStreamBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            DeviceUpDownStreamDto deviceUpDownStreamDto = BeanUtils.copyProperties(deviceUpDownStreamBo,
                    DeviceUpDownStreamDto.class);
            deviceUpDownStreamDto.setIsDel(isDel);
            return iDeviceUpDownStreamDao.getForecast(deviceUpDownStreamDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }
}
