package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardOwnerVo;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.third.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 */
public interface IWaterOwnerDao extends BaseDao<OwnerDto, OwnerVo> {

    @Override
    List<OwnerVo> list(OwnerDto ownerDto);

    List<OwnerVo> findWaterOwnerByNoList(@Param("ccidList") List<String> ccidList, @Param("enterpriseid") String enterpriseid);

    @Override
    OwnerVo get(OwnerDto ownerDto) throws DataAccessException;

    List<StandardOwnerVo> listInfo(DeviceUplinkDto deviceUplinkDto);
}
