package com.dotop.smartwater.project.third.concentrator.dao;

import com.dotop.smartwater.project.third.concentrator.core.dto.CollectorDto;
import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorFileDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorFileVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采集器数据库层接口
 *
 *
 */
public interface IConcentratorFileDao extends BaseDao<CollectorDto, CollectorVo> {


    List<ConcentratorFileVo> findByConcentratorCode(ConcentratorFileDto concentratorFileDto);

    void delBatch(@Param("oldConcentratorFileList") List<ConcentratorFileVo> oldConcentratorFileList, @Param("enterpriseid") String enterpriseid);

    void insertBatch(List<ConcentratorFileDto> concentratorFileDtos);
}
