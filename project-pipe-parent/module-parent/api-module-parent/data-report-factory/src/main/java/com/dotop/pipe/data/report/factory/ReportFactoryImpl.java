package com.dotop.pipe.data.report.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dotop.pipe.data.report.api.service.IReportService;
import com.dotop.pipe.data.report.core.form.AreaReportForm;
import com.dotop.pipe.data.report.core.form.ReportForm;
import com.dotop.pipe.data.report.core.form.VirtualForm;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.pipe.data.report.core.vo.ReportGroupVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dotop.pipe.api.service.area.IAreaService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.constants.DateTypeConstants;
import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.data.report.api.factory.IReportFactory;
import com.dotop.pipe.data.report.core.vo.RegionReportVo;
import com.dotop.pipe.data.report.core.vo.ReportAreaGroupVo;
import com.dotop.pipe.data.report.core.vo.ReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;

/**
 *
 * @date 2018/11/2.
 */
@Component
public class ReportFactoryImpl implements IReportFactory {

	private final static Logger logger = LogManager.getLogger(ReportFactoryImpl.class);

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private IReportService iReportService;
	@Autowired
	private IAreaService iAreaService;

	@Override
	public Map<String, List<ReportGroupVo>> getDeviceReport(ReportForm reportForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 所需参数
		// String[] deviceIds = reportForm.getDeviceIds();
		List<String> deviceIds = reportForm.getDeviceIds();
		DateTypeEnum dateTypeEnum = reportForm.getDateType();
		FieldTypeEnum[] fieldTypeEna = reportForm.getFieldType();
		Date startDate = reportForm.getStartDate();
		Date endDate = reportForm.getEndDate();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		// 年 2018-01-01 00:00:00
		// 月 2018-02-01 00:00:00
		// 日 2018-02-15 00:00:00
		// 时 2018-02-15 10:00:00
		// 实 2018-02-15 10:20:30
		startDate = DateUtils.date(dateTypeEnum.getCode(), startDate);
		endDate = DateUtils.date(dateTypeEnum.getCode(), endDate, 1);
		// switch (dateTypeEnum.getCode()) {
		// case DateTypeConstants.YEAR:
		// startDate = DateUtils.year(startDate);
		// endDate = DateUtils.year(endDate, 1);
		// break;
		// case DateTypeConstants.MONTH:
		// startDate = DateUtils.month(startDate);
		// endDate = DateUtils.month(endDate, 1);
		// break;
		// case DateTypeConstants.DAY:
		// startDate = DateUtils.day(startDate);
		// endDate = DateUtils.day(endDate, 1);
		// break;
		// case DateTypeConstants.HOUR:
		// startDate = DateUtils.hour(startDate);
		// endDate = DateUtils.hour(endDate, 1);
		// break;
		// default:
		// break;
		// }
		List<ReportVo> list = iReportService.getDeviceReport(operEid, deviceIds, dateTypeEnum, fieldTypeEna, startDate,
				endDate, ctimes);
		return listToMap(list);
	}

	@Override
	public Pagination<ReportVo> getDeviceReportPage(ReportForm reportForm, Integer page, Integer pageSize)
			throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// String[] deviceIds = reportForm.getDeviceIds();
		List<String> deviceIds = reportForm.getDeviceIds();
		DateTypeEnum dateTypeEnum = reportForm.getDateType();
		FieldTypeEnum[] fieldTypeEna = reportForm.getFieldType();
		Date startDate = reportForm.getStartDate();
		Date endDate = reportForm.getEndDate();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		startDate = DateUtils.date(dateTypeEnum.getCode(), startDate);
		endDate = DateUtils.date(dateTypeEnum.getCode(), endDate, 1);
		return iReportService.getDeviceReportPage(operEid, deviceIds, dateTypeEnum, fieldTypeEna, startDate, endDate,
				ctimes, page, pageSize);
	}

	@Override
	public List<ReportVo> getDeviceRealTime(ReportForm reportForm) throws FrameworkRuntimeException {
		// 所需参数
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// String[] deviceIds = reportForm.getDeviceIds();
		List<String> deviceIds = reportForm.getDeviceIds();
		FieldTypeEnum[] fieldTypeEna = reportForm.getFieldType();

		List<ReportVo> list = iReportService.getDeviceRealTime(operEid, deviceIds, fieldTypeEna);
		// return listToMap(list);
		return list;
	}

	private Map<String, List<ReportGroupVo>> listToMap(List<ReportVo> list) {
		// field deviceId reports
		Map<String, List<ReportGroupVo>> groupMap = new HashMap<>(16);
		if (CollectionUtils.isEmpty(list)) {
			return groupMap;
		}
		list.forEach(i -> {
			String field = i.getField();
			List<ReportGroupVo> rgs = groupMap.get(field);
			if (rgs == null) {
				rgs = new ArrayList<ReportGroupVo>();
				groupMap.put(field, rgs);
			}
			String deviceId = i.getDeviceId();
			ReportGroupVo rg = null;
			for (ReportGroupVo rgi : rgs) {
				if (deviceId.equals(rgi.getDeviceId())) {
					rg = rgi;
					break;
				}
			}
			if (rg == null) {
				rg = new ReportGroupVo();
				rg.setDeviceId(deviceId);
				rgs.add(rg);
			}
			List<ReportVo> reports = rg.getReports();
			if (reports == null) {
				reports = new ArrayList<>();
				rg.setReports(reports);
			}
			reports.add(i);
		});
		return groupMap;
	}

	@Override
	public List<ReportAreaGroupVo> getAreaReport(AreaReportForm areaReportForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		List<String> areaIds = areaReportForm.getAreaIds();
		Date startDate = areaReportForm.getStartDate();
		Date endDate = areaReportForm.getEndDate();
		DateTypeEnum dateTypeEnum = areaReportForm.getDateType();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		startDate = DateUtils.date(dateTypeEnum.getCode(), startDate);
		endDate = DateUtils.date(dateTypeEnum.getCode(), endDate, 1);
		return iReportService.getAreaReport(operEid, areaIds, dateTypeEnum, startDate, endDate, ctimes);
	}

	@Override
	public List<ReportVo> getVirtualFlow(VirtualForm virtualForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		List<String> positiveIds = virtualForm.getPositiveIds();
		List<String> reverseIds = virtualForm.getReverseIds();
		Date startDate = virtualForm.getStartDate();
		Date endDate = virtualForm.getEndDate();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		return iReportService.getVirtualFlow(operEid, positiveIds, reverseIds, startDate, endDate, ctimes);
	}

	@Override
	public Pagination<RegionReportVo> getRegionReportPage(ReportForm reportForm, Integer page, Integer pageSize) {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		List<String> deviceIds = reportForm.getDeviceIds();
		DateTypeEnum dateTypeEnum = reportForm.getDateType();
		Date startDate = reportForm.getStartDate();
		Date endDate = reportForm.getEndDate();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		startDate = DateUtils.date(DateTypeConstants.REAL_TIME, startDate);
		endDate = DateUtils.date(DateTypeConstants.REAL_TIME, endDate, 1);
		return iReportService.getRegionReportPage(operEid, deviceIds, dateTypeEnum, startDate, endDate, ctimes, page,
				pageSize);
	}

	@Override
	public List<AreaReportVo> getAreaReportByReading(Date startDate, Date endDate, List<String> areaIds) {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		startDate = DateUtils.date(DateTypeConstants.REAL_TIME, startDate);
		endDate = DateUtils.date(DateTypeConstants.REAL_TIME, endDate, 1);
		List<AreaModelVo> arealist = iAreaService.listAll(operEid);
		List<AreaReportVo> list = iReportService.getAreaReportByReading(startDate, endDate, ctimes, operEid, arealist);
		return list;
	}

	@Override
	public List<RegionReportVo> getRegionRealTimeReport(Date startDate, Date endDate, List<String> regionList) {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		startDate = DateUtils.date(DateTypeConstants.REAL_TIME, startDate);
		endDate = DateUtils.date(DateTypeConstants.REAL_TIME, endDate, 1);
		List<RegionReportVo> list = iReportService.getRegionRealTimeReport(startDate, endDate, ctimes, operEid);
		return list;
	}

	@Override
	public List<AreaReportVo> getAreaRealTimeReport(Date startDate, Date endDate, List<String> regionList) {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		startDate = DateUtils.date(DateTypeConstants.REAL_TIME, startDate);
		endDate = DateUtils.date(DateTypeConstants.REAL_TIME, endDate, 1);
		List<AreaReportVo> list = iReportService.getAreaRealTimeReport(startDate, endDate, ctimes, operEid);
		return list;
	}

}
