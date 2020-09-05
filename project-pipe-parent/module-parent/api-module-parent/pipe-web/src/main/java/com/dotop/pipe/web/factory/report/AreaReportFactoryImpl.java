package com.dotop.pipe.web.factory.report;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.web.api.factory.report.IAreaReportFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.api.service.area.IAreaService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.data.report.api.service.IAreaReportService;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 
 */
@Component
public class AreaReportFactoryImpl implements IAreaReportFactory {

	private final static Logger logger = LogManager.getLogger(AreaReportFactoryImpl.class);

	@Autowired
	private IAreaReportService iAreaReportService;
	@Autowired
	private IAreaService iAreaService;
	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Override
	public Pagination<AreaReportVo> getTotalByArea(String sensorCode, Date startDate, Date endDate, String sensorType,
			Integer page, Integer pageSize, DateTypeEnum dateType, String areaId) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		String areaCode = "";
		if (!"".equals(areaId)) {
			// 查询该区域下的子区域
			AreaModelVo areaModelVo = iAreaService.getNodeDetails(operEid, areaId);
			areaCode = areaModelVo.getAreaCode();
			if (areaCode == null) {
				logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_CODE_EXIST, "areaCode", areaCode));
				throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_CODE_EXIST);
			}
		}
		Pagination<AreaReportVo> pagination = iAreaReportService.getTotalByRealTime(sensorCode, startDate, endDate,
				sensorType, operEid, userBy, page, pageSize, areaCode);
		return pagination;
	}

	@Override
	public List<AreaReportVo> getAreaAlarmReport() {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		List<AreaReportVo> list = iAreaReportService.getAreaAlarmReport(operEid);
		return list;
	}
}
