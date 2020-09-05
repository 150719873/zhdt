package com.dotop.smartwater.project.third.meterread.client.service;

import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteCustomerVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

/**
 * 对接第三方系统的客户资料接口
 *
 *
 */
public interface IThirdCustomerService extends BaseService<RemoteCustomerBo, RemoteCustomerVo> {

    /**
     * 新增业主
     *
     * @param remoteCustomerBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    RemoteCustomerVo add(RemoteCustomerBo remoteCustomerBo) throws FrameworkRuntimeException;

    /**
     * 修改业主
     *
     * @param remoteCustomerBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    RemoteCustomerVo edit(RemoteCustomerBo remoteCustomerBo) throws FrameworkRuntimeException;

    /**
     * 修改业主换表
     *
     * @param remoteCustomerBo
     * @return
     * @throws FrameworkRuntimeException
     */
    RemoteCustomerVo editDevice(RemoteCustomerBo remoteCustomerBo) throws FrameworkRuntimeException;


    /**
     * 通过ownerId去成聪库的ExtendData1字段去查找
     * @param ownerIdList
     * @return
     */
    List<RemoteCustomerVo> findOwnerByIdList(List<String> ownerIdList, Integer factoryId);

    /**
     * 批量增加客户信息
     * @param customerList
     */
    void addAll(List<RemoteCustomerBo> customerList);

    /**
     * 批量修改用户信息
     * @param updateDustomerList
     */
    void updateAll(List<RemoteCustomerBo> updateDustomerList, Integer factoryId);

    List<RemoteCustomerVo> findOwnerByFactorId(RemoteCustomerBo customerBo);

    void batchDelete(List<String> delExtendData1List, Integer factoryId);
}
