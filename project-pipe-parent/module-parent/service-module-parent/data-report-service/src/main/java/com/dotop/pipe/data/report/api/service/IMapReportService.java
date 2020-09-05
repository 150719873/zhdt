package com.dotop.pipe.data.report.api.service;

import java.util.List;

import com.dotop.pipe.data.report.core.vo.MapReportVo;
import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public abstract class IMapReportService implements BaseService<BaseBo, MapReportVo> {

	public abstract List<MapReportVo> list(String[] sensorIds, String sensorType, Integer limit)
			throws FrameworkRuntimeException;

}
