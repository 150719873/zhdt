package com.dotop.smartwater.project.third.concentrator.dao;

import com.dotop.smartwater.project.third.concentrator.core.dto.UpLinkLogDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.UpLinkLogVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IUpLinkLogDao extends BaseDao<UpLinkLogDto, UpLinkLogVo> {


    /**
     * 查询上行日志列表
     *
     * @param uplinklogDto
     * @return
     */
    @Override
    List<UpLinkLogVo> list(UpLinkLogDto uplinklogDto);


    void insertBatch(@Param("upLinkLogDtoList") List<UpLinkLogDto> upLinkLogDtoList, @Param("thisMonth") String thisMonth);

    List<UpLinkLogVo> getUplinkLogDateList(UpLinkLogDto upLinkLogDto);

    List<UpLinkLogVo> getUplinkLogMonthList(List<UpLinkLogDto> upLinkLogDtos);
}
