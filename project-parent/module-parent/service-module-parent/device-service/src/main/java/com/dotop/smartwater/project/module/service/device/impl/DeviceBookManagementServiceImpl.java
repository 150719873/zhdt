package com.dotop.smartwater.project.module.service.device.impl;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBookManagementBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBookManagementDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookManagementVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceBookManagementDao;
import com.dotop.smartwater.project.module.service.device.IDeviceBookManagementService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: project-parent
 * @description: 表册管理接口

 * @create: 2019-03-06 16:32
 **/
@Service
public class DeviceBookManagementServiceImpl implements IDeviceBookManagementService {

    private static final Logger LOGGER = LogManager.getLogger(DeviceBookManagementServiceImpl.class);

    @Autowired
    private IDeviceBookManagementDao iDeviceBookManagementDao;

    @Override
    public DeviceBookManagementVo add(DeviceBookManagementBo deviceBookManagementBo) {
        try {
            DeviceBookManagementDto deviceBookManagementDto = BeanUtils.copy(deviceBookManagementBo,
                    DeviceBookManagementDto.class);
            deviceBookManagementDto.setBookId(UuidUtils.getUuid());
            deviceBookManagementDto.setIsDel(RootModel.NOT_DEL);
            iDeviceBookManagementDao.add(deviceBookManagementDto);
            return BeanUtils.copy(deviceBookManagementDto, DeviceBookManagementVo.class);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceBookManagementVo edit(DeviceBookManagementBo deviceBookManagementBo) {
        try {
            // 参数转换
            DeviceBookManagementDto deviceBookManagementDto = BeanUtils.copy(deviceBookManagementBo,
                    DeviceBookManagementDto.class);
            iDeviceBookManagementDao.edit(deviceBookManagementDto);
            return BeanUtils.copy(deviceBookManagementDto, DeviceBookManagementVo.class);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(DeviceBookManagementBo deviceBookManagementBo) {
        try {
            DeviceBookManagementDto deviceBookManagementDto = new DeviceBookManagementDto();
            deviceBookManagementDto.setBookId(deviceBookManagementBo.getBookId());
            return String.valueOf(iDeviceBookManagementDao.del(deviceBookManagementDto));
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceBookManagementVo get(DeviceBookManagementBo deviceBookManagementBo) {
        try {
            DeviceBookManagementDto deviceBookManagementDto = new DeviceBookManagementDto();
            deviceBookManagementDto.setBookId(deviceBookManagementBo.getBookId());
            return iDeviceBookManagementDao.get(deviceBookManagementDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<DeviceBookManagementVo> page(DeviceBookManagementBo deviceBookManagementBo) {
        try {
            Page<Object> pageHelper = PageHelper.startPage(deviceBookManagementBo.getPage(),
                    deviceBookManagementBo.getPageCount());
            List<DeviceBookManagementVo> list = iDeviceBookManagementDao
                    .list(BeanUtils.copy(deviceBookManagementBo, DeviceBookManagementDto.class));
            Pagination<DeviceBookManagementVo> pagination = new Pagination<>(deviceBookManagementBo.getPageCount(),
                    deviceBookManagementBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceBookManagementVo> list(DeviceBookManagementBo deviceBookManagementBo) {
        try {

            return iDeviceBookManagementDao.list(BeanUtils.copy(deviceBookManagementBo, DeviceBookManagementDto.class));

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExist(DeviceBookManagementBo deviceBookManagementBo) {
        try {
            boolean flag = false;
            List<DeviceBookManagementVo> list = iDeviceBookManagementDao
                    .list(BeanUtils.copy(deviceBookManagementBo, DeviceBookManagementDto.class));
            if (list != null && !list.isEmpty()) {
                flag = true;
            }
            return flag;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    public Integer judgeIfExistWorker(DeviceBookManagementBo deviceBookManagementBo) {
        try {
            DeviceBookManagementDto deviceBookManagementDto = new DeviceBookManagementDto();
            deviceBookManagementDto.setBookId(deviceBookManagementBo.getBookId());
            deviceBookManagementDto.setEnterpriseid(deviceBookManagementBo.getEnterpriseid());
            return iDeviceBookManagementDao.judgeIfExistWorker(deviceBookManagementDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    public Integer judgeIfExistOwner(DeviceBookManagementBo deviceBookManagementBo) {
        try {
            DeviceBookManagementDto deviceBookManagementDto = new DeviceBookManagementDto();
            deviceBookManagementDto.setBookId(deviceBookManagementBo.getBookId());
            deviceBookManagementDto.setEnterpriseid(deviceBookManagementBo.getEnterpriseid());
            return iDeviceBookManagementDao.judgeIfExistOwner(deviceBookManagementDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<String> findReadersbyAreas(List<String> list, String enterpriseid) {
        try {
            return iDeviceBookManagementDao.findReadersbyAreas(list, enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<String> getAreaIdsByUserid(String userid, String enterpriseid) {
        try {
            return iDeviceBookManagementDao.getAreaIdsByUserid(userid, enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
