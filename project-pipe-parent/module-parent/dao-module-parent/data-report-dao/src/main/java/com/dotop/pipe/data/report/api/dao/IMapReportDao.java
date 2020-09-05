package com.dotop.pipe.data.report.api.dao;

import java.util.List;

import com.dotop.pipe.data.report.core.dto.report.MapReportDto;
import com.dotop.pipe.data.report.core.vo.MapReportVo;
import org.springframework.dao.DataAccessException;

import com.dotop.smartwater.dependence.core.common.BaseDao;

public interface IMapReportDao extends BaseDao<MapReportDto, MapReportVo> {

	public List<MapReportVo> list(MapReportDto mapReportDto) throws DataAccessException;
}
