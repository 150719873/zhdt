package com.dotop.pipe.web.factory.report;

import java.util.Date;

import com.dotop.pipe.web.api.factory.report.ISensorReportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.data.report.api.service.ISensorReportService;
import com.dotop.pipe.data.report.core.vo.SensorReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 
 */
@Component
public class SensorReportFactoryImpl implements ISensorReportFactory {

	@Autowired
	private ISensorReportService iSensorReportService;

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Override
	public Pagination<SensorReportVo> getTotalByHour(String sensorCode, Date startDate, Date endDate, String sensorType,
			Integer page, Integer pageSize) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();

		Pagination<SensorReportVo> pagination = iSensorReportService.getTotalByHour(sensorCode, startDate, endDate,
				sensorType, operEid, userBy, page, pageSize);

		return pagination;
	}

	@Override
	public Pagination<SensorReportVo> getTotalByRealTime(String sensorCode, Date startDate, Date endDate,
			String sensorType, Integer page, Integer pageSize) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();

		Pagination<SensorReportVo> pagination = iSensorReportService.getTotalByRealTime(sensorCode, startDate, endDate,
				sensorType, operEid, userBy, page, pageSize);

		return pagination;
	}

	@Override
	public Pagination<SensorReportVo> getTotalByDay(String sensorCode, Date startDate, Date endDate, String sensorType,
			Integer page, Integer pageSize) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();

		Pagination<SensorReportVo> pagination = iSensorReportService.getTotalByDay(sensorCode, startDate, endDate,
				sensorType, operEid, userBy, page, pageSize);

		return pagination;
	}

	@Override
	public Pagination<SensorReportVo> getTotalByMonth(String sensorCode, Date startDate, Date endDate,
			String sensorType, Integer page, Integer pageSize) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();

		Pagination<SensorReportVo> pagination = iSensorReportService.getTotalByMonth(sensorCode, startDate, endDate,
				sensorType, operEid, userBy, page, pageSize);

		return pagination;
	}

	@Override
	public Pagination<SensorReportVo> getTotalByYear(String sensorCode, Date startDate, Date endDate, String sensorType,
			Integer page, Integer pageSize) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();

		Pagination<SensorReportVo> pagination = iSensorReportService.getTotalByYear(sensorCode, startDate, endDate,
				sensorType, operEid, userBy, page, pageSize);

		return pagination;
	}

	@Override
	public Pagination<SensorReportVo> getSensorWaterAmount(String sensorCode, Date startDate, Date endDate,
			Integer page, Integer pageSize) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		Pagination<SensorReportVo> pagination = iSensorReportService.getSensorWaterAmount(sensorCode, startDate,
				endDate, operEid, userBy, page, pageSize);

		return pagination;
	}

	@Override
	public Pagination<SensorReportVo> getVirtualFlowList(Date startDate, Date endDate, Integer page, Integer pageSize,
			String[] positiveValues, String[] reverseValues) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		StringBuffer positiveValue = new StringBuffer();
		StringBuffer reverseValue = new StringBuffer();

		// 正向
		if (positiveValues != null && positiveValues.length > 0) {
			positiveValue.append("(");
			for (int i = 0; i < positiveValues.length; i++) {
				positiveValue.append("'").append(positiveValues[i]).append("',");
			}
			positiveValue.replace(positiveValue.length() - 1, positiveValue.length(), ")");
		}

		if (reverseValues != null && reverseValues.length > 0) {
			// 反向
			reverseValue.append("(");
			for (int j = 0; j < reverseValues.length; j++) {
				reverseValue.append("'").append(reverseValues[j]).append("',");
			}
			reverseValue.replace(reverseValue.length() - 1, reverseValue.length(), ")");
		}

		Pagination<SensorReportVo> pagination = iSensorReportService.getVirtualFlowList(startDate, endDate, operEid,
				page, pageSize, positiveValue.toString(), reverseValue.toString());
		return pagination;
	}

	@Override
	public Pagination<SensorReportVo> getWaterQuality(String sensorCode, Date startDate, Date endDate, Integer page,
			Integer pageSize) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		String sensorType = CommonConstants.DICTIONARY_SENSORTYPE_WM;
		Pagination<SensorReportVo> pagination = iSensorReportService.getWaterQuality(sensorType, sensorCode, startDate,
				endDate, operEid, userBy, page, pageSize);

		return pagination;
	}

}
