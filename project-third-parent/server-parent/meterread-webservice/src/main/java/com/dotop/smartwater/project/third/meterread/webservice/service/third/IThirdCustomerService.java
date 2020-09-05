package com.dotop.smartwater.project.third.meterread.webservice.service.third;

import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.vo.RemoteCustomerVo;
import com.dotop.smartwater.dependence.core.common.BaseService;

import java.util.List;

/**
 * 对接第三方系统的客户资料接口
 *
 *
 */
public interface IThirdCustomerService extends BaseService<RemoteCustomerBo, RemoteCustomerVo> {

    /**
     * 通过ownerId去成聪库的ExtendData1字段去查找
     */
    List<RemoteCustomerVo> findOwnerByIdList(List<String> ownerIdList, String enterpriseid, Integer factoryId);

    /**
     * 批量增加客户信息
     */
    void addAll(List<RemoteCustomerBo> customerList);

    /**
     * 批量修改用户信息
     */
    void updateAll(List<RemoteCustomerBo> updateDustomerList);

    List<RemoteCustomerVo> findOwnerByFactorId(RemoteCustomerBo customerBo);

}
