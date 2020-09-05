package com.dotop.smartwater.project.third.meterread.webservice.service.water.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerRecordBo;
import com.dotop.smartwater.project.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerRecordDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.third.meterread.webservice.dao.water.IWaterDao;
import com.dotop.smartwater.project.third.meterread.webservice.service.water.IWaterOwnerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterOwnerServiceImpl implements IWaterOwnerService {

    private static final Logger LOGGER = LogManager.getLogger(WaterOwnerServiceImpl.class);

    @Autowired
    private IWaterDao waterDao;

    @Override
    public List<OwnerVo> list(OwnerBo owernBo) throws FrameworkRuntimeException {
        try {
            OwnerDto ownerDto = BeanUtils.copy(owernBo, OwnerDto.class);
            List<OwnerVo> list = waterDao.getOwnerList(ownerDto);
            return list;
        }catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo get(OwnerBo ownerBo) throws FrameworkRuntimeException {
        try {
            return waterDao.findByOwnerId(ownerBo.getOwnerid());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<OwnerVo> page(OwnerBo ownerBo) throws FrameworkRuntimeException {
        OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
        Page<Object> pageHelper = PageHelper.startPage(ownerBo.getPage(), ownerBo.getPageCount());
        List<OwnerVo> list = waterDao.getOwnerList(ownerDto);
        Pagination<OwnerVo> ownerVoPagination = new Pagination<>(ownerBo.getPage(), ownerBo.getPageCount(), list, pageHelper.getTotal());
        return ownerVoPagination;
    }

    @Override
    public List<OwnerRecordVo> ownerRecords(OwnerRecordBo ownerRecordBo) throws FrameworkRuntimeException {
        try {
            OwnerRecordDto ownerRecordDto = BeanUtils.copy(ownerRecordBo, OwnerRecordDto.class);
            List<OwnerRecordVo> list = waterDao.ownerRecords(ownerRecordDto);
            return list;
        }catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
