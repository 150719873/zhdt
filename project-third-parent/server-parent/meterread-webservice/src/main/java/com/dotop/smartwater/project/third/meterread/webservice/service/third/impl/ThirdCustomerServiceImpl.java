package com.dotop.smartwater.project.third.meterread.webservice.service.third.impl;

import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.dto.RemoteCustomerDto;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.vo.RemoteCustomerVo;
import com.dotop.smartwater.project.third.meterread.webservice.dao.third.IThirdDao;
import com.dotop.smartwater.project.third.meterread.webservice.service.third.IThirdCustomerService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThirdCustomerServiceImpl implements IThirdCustomerService {

    @Autowired
    private IThirdDao iThirdDao;

    /**
     * 通过ownerId去成聪库的ExtendData1字段去查找
     */
    @Override
    public List<RemoteCustomerVo> findOwnerByIdList(List<String> ownerIdList, String enterpriseid, Integer factoryId) {
        return iThirdDao.findOwnerByIdList(ownerIdList, enterpriseid, factoryId);
    }

    /**
     * 批量增加用户信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void addAll(List<RemoteCustomerBo> customerList) {
        try {
            List<RemoteCustomerDto> customerDtoList = BeanUtils.copy(customerList, RemoteCustomerDto.class);
            iThirdDao.insertBatch(customerDtoList);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 批量修改用户信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateAll(List<RemoteCustomerBo> updateDustomerList) {
        try {
            List<RemoteCustomerDto> customerDtoList = BeanUtils.copy(updateDustomerList, RemoteCustomerDto.class);
            iThirdDao.batchUpdate(customerDtoList);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<RemoteCustomerVo> findOwnerByFactorId(RemoteCustomerBo customerBo) {
        return iThirdDao.findOwnerByFactorId(customerBo);
    }
}
