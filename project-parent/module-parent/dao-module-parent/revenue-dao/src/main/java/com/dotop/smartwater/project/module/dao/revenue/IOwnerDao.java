package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.OwnerChangeDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerExtDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OwnerDeviceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**

 */
public interface IOwnerDao extends BaseDao<OwnerDto, OwnerVo> {

    /**
     * 插入业主
     *
     * @param ownerDto
     * @
     */
    @Override
    void add(OwnerDto ownerDto);

    int batchAdd(@Param("list") List<OwnerDto> owners);

    int batchAddOwnerExt(@Param("list") List<OwnerDto> listDto);

    int batchEdit(@Param("list") List<OwnerDto> owners);

    int batchChange(@Param("list") List<OwnerChangeDto> changes);

    int checkByOwnerNo(OwnerDto ownerDto);

    int checkBybarCode(OwnerDto ownerDto);

    OwnerVo findOwnerByDevNo(String devno);

    String findDevIdByDevNo(String devno);

    void openOwner(OwnerDto ownerDto);

    int createAndopenOwner(OwnerDto ownerDto);

    OwnerVo findByOwnerId(OwnerExtDto ownerExtDto);
    
    OwnerVo getOwnerById(OwnerExtDto ownerExtDto);

    void updateForNotOpen(OwnerDto o);

    void webUpdate(OwnerDto o);

    void delete(String ownerid);

    void updateStatusAndAlreadyPay(OwnerDto ownerDto);

    void updateDevId(OwnerDto ownerDto);

    OwnerVo findByOwnerUserNo(@Param("userno") String userno, @Param("ownerid") String ownerid);

    List<OwnerVo> getOwners(OwnerDto ownerDto);

    List<OwnerVo> getCommunityOwner(OwnerDto ownerDto);

    List<OwnerVo> getOwnerList(OwnerDto ownerDto);

    OwnerVo findByOwnerDetail(String ownerid);

    void batchUpdateOwner(OwnerDto ownerDto);

    int getOwnerCount(OwnerDto ownerDto);

    OwnerVo getOwnerUser(OwnerDto ownerDto);

    OwnerVo getOwnerUserByDevId(String devid);

    List<OwnerVo> findOwnerByCommunity(String areaId);

    Long getUserCount(String enterpriseId);
    
    Long userOpenCount(String enterpriseId);
    
    Long areaCount(String enterpriseId);

    List<OwnerDeviceVo> findOwnerByCommunityIds(@Param("str") String str, @Param("enterpriseid") String enterpriseid);

    List<OwnerDeviceVo> findOwnerByOwnernos(@Param("str") String str, @Param("enterpriseid") String enterpriseid);

    OwnerVo findByDevId(String devId);

    List<String> checkOwnerNo(@Param("list") Set<String> list);

    List<String> userNoCheck(@Param("value") String userNos);

    List<String> devNoCheck(@Param("value") String devNos);

    OwnerVo getByOwner(OwnerDto ownerDto);

    OwnerVo getUserNoOwner(OwnerDto ownerDto);

    List<OwnerVo> getkeyWordOwner(OwnerDto ownerDto);

    OwnerVo checkOwnerIsExist(OwnerDto ownerDto);

    int updateOwnerAccount(@Param("alreadypay") Double alreadypay, @Param("ownerid") String ownerid);

    int updateStatusAndAlreadypay(OwnerDto ownerDto);

    List<OwnerVo> getByIds(@Param("list") List<String> ownerids);

    void updateAlreadyPay(@Param("ownerid") String ownerid, @Param("afterMoney") Double afterMoney);

    List<OwnerVo> findBusinessHallOwners(@Param("enterpriseid") String enterpriseid,
                                         @Param("list") List<String> keyWords);

    List<String> getOwnersByTypeId(@Param("typeId")String typeId, @Param("status")Integer status);
}
