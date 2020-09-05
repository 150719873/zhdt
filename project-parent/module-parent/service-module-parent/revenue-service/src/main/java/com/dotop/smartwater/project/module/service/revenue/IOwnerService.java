package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerChangeBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerRecordBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OwnerDeviceVo;

import java.util.List;
import java.util.Set;

/**

 */
public interface IOwnerService extends BaseService<OwnerBo, OwnerVo> {

    @Override
    OwnerVo add(OwnerBo ownerBo);
    
    boolean batchAdd(List<OwnerBo> list);
    
    boolean batchEdit(List<OwnerBo> list);
    
    boolean batchChange(List<OwnerChangeBo> list);

    int checkByOwnerNo(OwnerBo ownerBo);
    
    int checkByBarCode(OwnerBo ownerBo);

    OwnerVo findOwnerByDevNo(OwnerBo ownerBo);

    String findDevIdByDevNo(OwnerBo owner);

    DeviceVo findDevByDevNo(OwnerBo owner);

    List<String> userNoCheck(Set<String> userNoSet);
    
    void openOwner(OwnerBo owner);

    int createAndopenOwner(OwnerBo owner);

    OwnerVo findByOwnerId(OwnerBo ownerBo);
    
    OwnerVo getOwnerById(OwnerBo ownerBo);

    void webUpdate(OwnerBo ownerBo);

    void delete(OwnerBo ownerBo);

    OwnerVo checkOwnerIsExist(OwnerBo ownerBo);

    OwnerVo getUserNoOwner(OwnerBo ownerBo);

    OwnerVo getkeyWordOwner(OwnerBo ownerBo);

    void addOwnerRecord(OwnerRecordBo ownerRecord);

    void cancelOwner(OwnerBo ownerBo);

    void updateDevid(OwnerVo owner);

    OwnerVo findByOwnerUserno(OwnerBo ownerBo);

    Pagination<OwnerVo> getOwners(OwnerBo ownerBo);

    List<OwnerVo> getCommunityOwner(OwnerBo ownerBo);

    Pagination<OwnerVo> getOwnerList(OwnerBo ownerBo);

    OwnerVo findByOwnerDetail(OwnerBo ownerBo);

    Pagination<OwnerRecordVo> getRecordList(OwnerBo ownerBo);

    void batchUpdateOwner(OwnerBo ownerBo);

    int getOwnerCount(OwnerBo ownerBo);

    OwnerVo getOwnerUser(OwnerBo ownerBo);

    OwnerVo getOwnerUserByDevId(String devid);

    List<OwnerVo> findByCommunity(String areaId);

    Long getUserCount(String enterpriseId);
    
    Long userOpenCount(String enterpriseId);
    
    Long areaCount(String enterpriseId);

    List<OwnerDeviceVo> findOwnerByCommunityIds(String communityIds, String enterpriseid);

    List<OwnerDeviceVo> findOwnerByOwnernos(String usernos, String enterpriseid);

    OwnerVo findByDevId(String devId);

    OwnerVo getByOwner(OwnerBo ownerBo);

    /**
     * lsc用到 有问题请联系
     *
     * @param devNoSet
     * @return @
     */
    List<DeviceVo> findDevByDevNos(Set<String> devNoSet);

    /**
     * 李士成用到 有问题请联系
     *
     * @param list
     * @return @
     */
    List<String> checkOwnerNo(Set<String> list);

    /**
     * 李士成用到 有问题请联系
     *
     * @param devNoSet
     * @return @
     */
    List<String> devNoCheck(Set<String> devNoSet);

    List<OwnerVo> getByIds(List<String> ownerids);

    /**
     * 微信调用 更改账户余额信息
     *
     * @param alreadypay
     * @param ownerid
     */
    void updateOwnerAccount(Double alreadypay, String ownerid);

    void webaddList(List<OwnerBo> list);

    int updateStatusAndAlreadypay(OwnerBo ownerBo);

    List<OwnerVo> findBusinessHallOwners(String enterpriseid, List<String> keyWords);

    List<String> getOwnersByTypeId(String typeid, Integer ownerStatusCreate);
}
