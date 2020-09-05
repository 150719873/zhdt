package com.dotop.pipe.data.report.service;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.pipe.data.report.api.dao.IAreaReportDao;
import com.dotop.pipe.data.report.api.service.IAreaReportService;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

@Service
public class AreaReportServiceImpl implements IAreaReportService {

	private final static Logger logger = LogManager.getLogger(AreaReportServiceImpl.class);

	@Autowired
	private IAreaReportDao iAreaReportDao;

	@Override
	public Pagination<AreaReportVo> getTotalByRealTime(String sensorCode, Date startDate, Date endDate,
			String sensorType, String operEid, String userBy, Integer page, Integer pageSize, String areaCode)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;

			List<AreaReportVo> list = iAreaReportDao.listByRealTime(operEid, offset, limit, isDel, sensorCode,
					startDate, endDate, sensorType, areaCode);
			Integer count = iAreaReportDao.listCountByRealTime(operEid, isDel, sensorCode, startDate, endDate,
					sensorType, areaCode);
			Pagination<AreaReportVo> pagination = new Pagination<AreaReportVo>(pageSize, page);
			pagination.setData(list);
			pagination.setTotalPageSize(count);
			return pagination;
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

	@Override
	public List<AreaReportVo> getAreaAlarmReport(String operEid) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			List<AreaReportVo> list = iAreaReportDao.getAreaAlarmReport(operEid, isDel);
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
