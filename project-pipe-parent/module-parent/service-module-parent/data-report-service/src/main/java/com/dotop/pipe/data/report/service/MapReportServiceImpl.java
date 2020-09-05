package com.dotop.pipe.data.report.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.pipe.data.report.api.dao.IMapReportDao;
import com.dotop.pipe.data.report.api.service.IMapReportService;
import com.dotop.pipe.data.report.core.dto.report.MapReportDto;
import com.dotop.pipe.data.report.core.vo.MapReportVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;

@Service
public class MapReportServiceImpl extends IMapReportService {

	private final static Logger logger = LogManager.getLogger(MapReportServiceImpl.class);

	@Autowired
	private IMapReportDao iMapReportDao;

	@Override
	public List<MapReportVo> list(String[] sensorIds, String sensorType, Integer limit)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			MapReportDto mapReportDto = new MapReportDto();
			mapReportDto.setSensorIds(sensorIds);
			mapReportDto.setSensorType(sensorType);
			mapReportDto.setOffset(0);
			mapReportDto.setLimit(limit);
			mapReportDto.setIsDel(isDel);
			List<MapReportVo> list = iMapReportDao.list(mapReportDto);
			return list;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

}
