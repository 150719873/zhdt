package com.dotop.smartwater.project.third.meterread.webservice.dao.third;

import com.dotop.smartwater.project.third.meterread.webservice.core.third.dto.RemoteCustomerDto;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.vo.RemoteCustomerVo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.vo.RemoteDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IThirdDao extends BaseDao<BaseDto, BaseVo> {


    /**
     * 根据水务库中的ownerId的集合去数据库中查找对应的客户信息
     *
     * @param ownerIdList
     * @return
     */
    List<RemoteCustomerVo> findOwnerByIdList(@Param("ownerIdList") List<String> ownerIdList, @Param("enterpriseid") String enterpriseid, @Param("factoryId") Integer factoryId);

    /**
     * 批量插入客户信息
     *
     * @param customerDtoList
     */
    void insertBatch(List<RemoteCustomerDto> customerDtoList);

    /**
     * 批量修改客户信息
     *
     * @param customerDtoList
     */
    void batchUpdate(@Param("customerDtoList") List<RemoteCustomerDto> customerDtoList);

    List<RemoteCustomerVo> findOwnerByFactorId(RemoteCustomerBo customerBo);


    /**
     * 根据水表编号和公司
     *
     * @param deviceIds
     * @param factoryId
     * @return
     */
    List<RemoteDataVo> getList(@Param("deviceIds") List<String> deviceIds, @Param("enterpriseid") String enterpriseid, @Param("factoryId") int factoryId);

    /**
     * 批量更新抄表数据
     *
     * @param remoteDataBos
     */
    void updates(@Param("remoteDataBos") List<RemoteDataBo> remoteDataBos);

    /**
     * 批量插入抄表数据
     *
     * @param remoteDataBos
     */
    void inserts(@Param("remoteDataBos") List<RemoteDataBo> remoteDataBos);

}
