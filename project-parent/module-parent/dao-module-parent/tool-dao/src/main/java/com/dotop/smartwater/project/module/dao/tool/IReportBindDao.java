package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.ReportBindDto;
import com.dotop.smartwater.project.module.core.water.vo.ReportBindVo;

import java.util.List;

public interface IReportBindDao extends BaseDao<ReportBindDto, ReportBindVo> {

    @Override
    List<ReportBindVo> list(ReportBindDto reportBindDto);

    @Override
    void add(ReportBindDto reportBindDto);

    @Override
    Integer del(ReportBindDto reportBindDto);

    @Override
    ReportBindVo get(ReportBindDto reportBindDto);

}
