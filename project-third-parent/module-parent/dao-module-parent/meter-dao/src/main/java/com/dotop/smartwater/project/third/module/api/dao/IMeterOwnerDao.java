package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 */
public interface IMeterOwnerDao extends BaseDao<OwnerDto, OwnerVo> {

    @Override
    List<OwnerVo> list(OwnerDto ownerDto) throws FrameworkRuntimeException;

    @Override
    void adds(List<OwnerDto> ownerDtos) throws FrameworkRuntimeException;


    void updateBatch(@Param("ownerDtos") List<OwnerDto> ownerDtos, @Param("enterpriseid") String enterpriseid) throws FrameworkRuntimeException;

    @Override
    OwnerVo get(OwnerDto ownerDto) throws DataAccessException;
}
