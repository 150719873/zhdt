package com.dotop.pipe.data.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.pipe.data.report.api.dao.ISensorReportDao;
import com.dotop.pipe.data.report.api.service.ISensorReportService;
import com.dotop.pipe.data.report.core.vo.SensorReportVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

@Service
public class SensorReportServiceImpl implements ISensorReportService {

	private final static Logger logger = LogManager.getLogger(SensorReportServiceImpl.class);

	@Autowired
	private ISensorReportDao iSensorReportDao;

	@Override
	public Pagination<SensorReportVo> getTotalByHour(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;

			List<SensorReportVo> list = iSensorReportDao.listByHour(operEid, offset, limit, isDel, sensorCode,
					startDate, endDate, sensorType);
			Integer count = iSensorReportDao.listCountByHour(operEid, isDel, sensorCode, startDate, endDate,
					sensorType);

			Pagination<SensorReportVo> pagination = new Pagination<SensorReportVo>(pageSize, page);
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
	public Pagination<SensorReportVo> getTotalByYear(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;

			List<SensorReportVo> list = iSensorReportDao.listByYear(operEid, offset, limit, isDel, sensorCode,
					startDate, endDate, sensorType);
			Integer count = iSensorReportDao.listCountByYear(operEid, isDel, sensorCode, startDate, endDate,
					sensorType);

			Pagination<SensorReportVo> pagination = new Pagination<SensorReportVo>(pageSize, page);
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
	public Pagination<SensorReportVo> getTotalByMonth(String sensorCode, Date startDate, Date endDate,
			String sensorType, String operEid, String userBy, Integer page, Integer pageSize)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;

			List<SensorReportVo> list = iSensorReportDao.listByMonth(operEid, offset, limit, isDel, sensorCode,
					startDate, endDate, sensorType);
			Integer count = iSensorReportDao.listCountByMonth(operEid, isDel, sensorCode, startDate, endDate,
					sensorType);

			Pagination<SensorReportVo> pagination = new Pagination<SensorReportVo>(pageSize, page);
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
	public Pagination<SensorReportVo> getTotalByDay(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;

			List<SensorReportVo> list = iSensorReportDao.listByDay(operEid, offset, limit, isDel, sensorCode, startDate,
					endDate, sensorType);
			Integer count = iSensorReportDao.listCountByDay(operEid, isDel, sensorCode, startDate, endDate, sensorType);

			Pagination<SensorReportVo> pagination = new Pagination<SensorReportVo>(pageSize, page);
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
	public Pagination<SensorReportVo> getTotalByRealTime(String sensorCode, Date startDate, Date endDate,
			String sensorType, String operEid, String userBy, Integer page, Integer pageSize)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;

			List<SensorReportVo> list = iSensorReportDao.listByRealTime(operEid, offset, limit, isDel, sensorCode,
					startDate, endDate, sensorType);
			Integer count = iSensorReportDao.listCountByRealTime(operEid, isDel, sensorCode, startDate, endDate,
					sensorType);

			Pagination<SensorReportVo> pagination = new Pagination<SensorReportVo>(pageSize, page);
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
	public Pagination<SensorReportVo> getSensorWaterAmount(String sensorCode, Date startDate, Date endDate,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;

			List<SensorReportVo> list = iSensorReportDao.getSensorWaterAmount(operEid, offset, limit, isDel, sensorCode,
					startDate, endDate);
			Integer count = iSensorReportDao.getSensorWaterCount(operEid, isDel, sensorCode, startDate, endDate);

			Pagination<SensorReportVo> pagination = new Pagination<SensorReportVo>(pageSize, page);
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
	public Pagination<SensorReportVo> getVirtualFlowList(Date startDate, Date endDate, String operEid, Integer page,
			Integer pageSize, String positiveValue, String reverseValue) throws FrameworkRuntimeException {

		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;

			// 要查询两次 第一查询正向的值 第二次 查询反向的值
			SensorReportVo sensorReportVoPositive = iSensorReportDao.getVirtualFlowList(operEid, offset, limit, isDel,
					startDate, endDate, positiveValue);
			SensorReportVo sensorReportVoReverse = iSensorReportDao.getVirtualFlowList(operEid, offset, limit, isDel,
					startDate, endDate, reverseValue);
			Pagination<SensorReportVo> pagination = new Pagination<SensorReportVo>(pageSize, page);
			List<SensorReportVo> list = new ArrayList<SensorReportVo>();
			if (sensorReportVoPositive != null && sensorReportVoReverse != null) {
				// 返回的结果进行处理
				BigDecimal positiveVal = new BigDecimal(sensorReportVoPositive.getTotalVal());
				BigDecimal reverseVal = new BigDecimal(sensorReportVoReverse.getTotalVal());
				sensorReportVoReverse.setTotalVal(positiveVal.subtract(reverseVal).toString());
				list.add(sensorReportVoReverse);
			}
			pagination.setData(list);
			pagination.setTotalPageSize(1);
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
	public Pagination<SensorReportVo> getWaterQuality(String sensorType, String sensorCode, Date startDate,
			Date endDate, String operEid, String userBy, Integer page, Integer pageSize)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer offset = (page - 1) * pageSize;
			Integer limit = pageSize;
			List<SensorReportVo> list = iSensorReportDao.getWaterQuality(operEid, offset, limit, isDel, sensorCode,
					sensorType, startDate, endDate);
			Integer count = iSensorReportDao.getWaterQualityCount(operEid, isDel, sensorCode, sensorType, startDate,
					endDate);

			Pagination<SensorReportVo> pagination = new Pagination<SensorReportVo>(pageSize, page);
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

}
