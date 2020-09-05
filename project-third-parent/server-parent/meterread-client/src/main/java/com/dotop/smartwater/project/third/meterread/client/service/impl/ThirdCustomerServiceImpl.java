package com.dotop.smartwater.project.third.meterread.client.service.impl;

import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.dto.RemoteCustomerDto;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteCustomerVo;
import com.dotop.smartwater.project.third.meterread.client.dao.third.IThirdDao;
import com.dotop.smartwater.project.third.meterread.client.service.IThirdCustomerService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdCustomerServiceImpl implements IThirdCustomerService {

    @Autowired
    private IThirdDao iThirdDao;

    @Override
    public RemoteCustomerVo add(RemoteCustomerBo remoteCustomerBo) throws FrameworkRuntimeException {
        return null;
    }

    @Override
    public RemoteCustomerVo edit(RemoteCustomerBo remoteCustomerBo) throws FrameworkRuntimeException {
        return null;
    }

    @Override
    public RemoteCustomerVo editDevice(RemoteCustomerBo remoteCustomerBo) throws FrameworkRuntimeException {
        return null;
    }

    /*@Override
    public Pagination<RemoteCustomerVo> page(RemoteCustomerBo remoteCustomerBo) throws FrameworkRuntimeException {
        Page<Object> pageHelper = PageHelper.startPage(remoteCustomerBo.getPage(), remoteCustomerBo.getPageCount());
        List<RemoteCustomerVo> list = iThirdDao.getCustomerList(remoteCustomerBo);
        Pagination<RemoteCustomerVo> remoteCustomerVoPagination = new Pagination<>(remoteCustomerBo.getPage(), remoteCustomerBo.getPageCount(), list, pageHelper.getTotal());
        return remoteCustomerVoPagination;
    }*/

    /**
     * 通过ownerId去成聪库的ExtendData1字段去查找
     * @param ownerIdList
     * @return
     */
    @Override
    public List<RemoteCustomerVo> findOwnerByIdList(List<String> ownerIdList, Integer factoryId) {
        return iThirdDao.findOwnerByIdList(ownerIdList, factoryId);
    }

    /**
     * 批量增加用户信息
     * @param customerList
     */
    @Override
    public void addAll(List<RemoteCustomerBo> customerList) {
        try{
            List<RemoteCustomerDto> customerDtoList = BeanUtils.copy(customerList, RemoteCustomerDto.class);
            iThirdDao.insertBatch(customerDtoList);
        }catch (DataAccessException e){
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 批量修改用户信息
     * @param updateDustomerList
     */
    @Override
    public void updateAll(List<RemoteCustomerBo> updateDustomerList, Integer factoryId) {

        try{
            List<RemoteCustomerDto> customerDtoList = BeanUtils.copy(updateDustomerList, RemoteCustomerDto.class);
            iThirdDao.batchUpdate(customerDtoList,factoryId);
        }catch (DataAccessException e){
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<RemoteCustomerVo> findOwnerByFactorId(RemoteCustomerBo customerBo) {
        return iThirdDao.findOwnerByFactorId(customerBo);
    }

    @Override
    public void batchDelete(List<String> delExtendData1List, Integer factoryId) {
        iThirdDao.batchDelete(delExtendData1List, factoryId);
    }


}
