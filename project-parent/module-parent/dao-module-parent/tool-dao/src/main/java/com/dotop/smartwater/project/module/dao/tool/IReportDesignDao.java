package com.dotop.smartwater.project.module.dao.tool;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.ReportDesignDto;
import com.dotop.smartwater.project.module.core.water.dto.ReportRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.ReportDesignVo;
import com.dotop.smartwater.project.module.core.water.vo.ReportRelationVo;

/**
 * 
 * 报表展示设计Dao

 * @date 2019-07-22
 *
 */
public interface IReportDesignDao extends BaseDao<ReportDesignDto, ReportDesignVo> {
	
	@Override
    List<ReportDesignVo> list(ReportDesignDto reportDesignDto);
	
	List<ReportRelationVo> relationList(ReportRelationDto reportRelationDto);

    @Override
    void add(ReportDesignDto reportDesignDto);
    
    Integer addRelations(@Param("list") List<ReportRelationDto> list);

    @Override
    Integer del(ReportDesignDto reportDesignDto);
    
    Integer deleteRelation(ReportDesignDto reportDesignDto);

    @Override
    ReportDesignVo get(ReportDesignDto reportDesignDto);
    
    @Override
    Integer edit(ReportDesignDto reportDesignDto);

}
