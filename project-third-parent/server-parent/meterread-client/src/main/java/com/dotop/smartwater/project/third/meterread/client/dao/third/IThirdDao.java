package com.dotop.smartwater.project.third.meterread.client.dao.third;

import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteCustomerVo;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteDataVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteValveBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.dto.RemoteCustomerDto;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteValveVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IThirdDao extends BaseDao<BaseDto, BaseVo> {

    /**
     * 根据水表编号和公司
     * @param meterAddrs
     * @param factoryId
     * @return
     */
    List<RemoteDataVo> getList(@Param("meterAddrs") String[] meterAddrs, @Param("factoryId") int factoryId);

    /**
     * 批量更新抄表数据
     * @param remoteDataBos
     */
    void updates(@Param("remoteDataBos") List<RemoteDataBo> remoteDataBos);

    /**
     * 批量插入抄表数据
     * @param remoteDataBos
     */
    void inserts(@Param("remoteDataBos") List<RemoteDataBo> remoteDataBos);

    /**
     * 获取用户列表
     * @param remoteCustomerBo
     * @return
     */

    /**
     * 根据水务库中的ownerId的集合去数据库中查找对应的客户信息
     * @param ownerIdList
     * @return
     */
    List<RemoteCustomerVo> findOwnerByIdList(@Param("ownerIdList") List<String> ownerIdList, @Param("factoryId") Integer factoryId);

    /**
     * 批量插入客户信息
     * @param customerDtoList
     */
    void insertBatch(List<RemoteCustomerDto> customerDtoList);

    /**
     * 批量修改客户信息
     * @param customerDtoList
     */
    void batchUpdate(@Param("customerDtoList") List<RemoteCustomerDto> customerDtoList, @Param("factoryId") Integer factoryId);

    /**
     * 查询开关阀控制列表
     * @param remoteValveBo
     * @return
     */
    List<RemoteValveVo> getRemoteValveList(RemoteValveBo remoteValveBo);

    /**
     * 更新下发
     * @param remoteValveBo
     */
    void editRemoteValve(RemoteValveBo remoteValveBo);

    List<RemoteCustomerVo> findOwnerByFactorId(RemoteCustomerBo customerBo);

    /**
     * 批量删除水务库中没有的用户
     * @param delExtendData1List
     */
    void batchDelete(@Param("delExtendData1List") List<String> delExtendData1List, @Param("factoryId") Integer factoryId);
}
