package com.dotop.pipe.web.factory.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dotop.pipe.web.api.factory.report.ILeakageAnalysisFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.api.service.report.ITimingCalculationService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.form.LeakageAnalysisForm;
import com.dotop.pipe.core.form.LeakageAnalysisItemForm;
import com.dotop.pipe.core.vo.report.LeakageAnalysisVo;
import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.pipe.core.vo.report.TimingFormulaVo;
import com.dotop.pipe.data.report.api.service.IReportService;
import com.dotop.pipe.data.report.core.bo.report.TimingCalculationBo;
import com.dotop.pipe.data.report.core.vo.ReportAreaGroupVo;
import com.dotop.pipe.data.report.core.vo.ReportAreaVo;
import com.dotop.pipe.data.report.core.vo.ReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.DateUtils;

/**
 * 
 */
@Component
public class LeakageAnalysisFactoryImpl implements ILeakageAnalysisFactory {

	private final static Logger logger = LogManager.getLogger(LeakageAnalysisFactoryImpl.class);

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private ITimingCalculationService iTimingCalculationService;
	@Autowired
	private IReportService iReportService;

	@Override
	public LeakageAnalysisVo calculation(LeakageAnalysisForm leakageAnalysisForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 传输后台计算方式是选择或区域
		String type = leakageAnalysisForm.getType();
		Map<String, LeakageAnalysisItemForm> itemMap = null;
		Map<String, ReportVo> deviceReportMap = null;
		Map<String, ReportAreaVo> areaReportMap = null;
		List<LeakageAnalysisItemForm> items = leakageAnalysisForm.getItems();
		BigDecimal result = new BigDecimal(0);
		Date startDate = leakageAnalysisForm.getStartDate();
		Date endDate = leakageAnalysisForm.getEndDate();
		Set<String> ctimes = DateUtils.listDates(startDate, endDate);
		if (PipeConstants.ANALYSIS_TYPE_SELECT.equals(type)) {
			// 选择 观察点或者起始点集合
			itemMap = items.stream()
					.collect(Collectors.toMap(LeakageAnalysisItemForm::getDeviceId, a -> a, (k1, k2) -> k1));
			// 封装查询参数
			DateTypeEnum dateTypeEnum = DateTypeEnum.OTHER;

			FieldTypeEnum[] fieldTypeEna = new FieldTypeEnum[] { FieldTypeEnum.flwMeasure };
			List<String> deviceIds = new ArrayList<String>(itemMap.keySet());

			// 通过deviceIds计算获取最新field数据
			List<ReportVo> deviceReports = iReportService.getDeviceReport(operEid, deviceIds, dateTypeEnum,
					fieldTypeEna, startDate, endDate, ctimes);

			// 观察点的数据集合
			deviceReportMap = deviceReports.stream()
					.collect(Collectors.toMap(ReportVo::getDeviceId, a -> a, (k1, k2) -> k1));
			// 公式计算
			result = calculationSelect(itemMap, deviceReportMap);
		} else if (PipeConstants.ANALYSIS_TYPE_AREA.equals(type)) {
			// 区域集合
			itemMap = items.stream()
					.collect(Collectors.toMap(LeakageAnalysisItemForm::getAreaId, a -> a, (k1, k2) -> k1));

			// 所需参数
			List<String> areaIds = new ArrayList<>(itemMap.keySet());

			List<ReportAreaGroupVo> list = iReportService.getAreaReport(operEid, areaIds, DateTypeEnum.YEAR, startDate,
					endDate, ctimes);
			// 通过areaIds计算获取最新field数据
			List<ReportAreaVo> areaReports = new ArrayList<>();
			// 处理数据
			for (ReportAreaGroupVo reportAreaGroupVo : list) {
				for (ReportAreaVo reportAreaVo : reportAreaGroupVo.getReportAreas()) {
					areaReports.add(reportAreaVo);
				}
			}
			areaReportMap = areaReports.stream()
					.collect(Collectors.toMap(ReportAreaVo::getAreaId, a -> a, (k1, k2) -> k1));
			// 公式计算
			result = calculationArea(itemMap, areaReportMap);
		}

		// 组装
		LeakageAnalysisVo la = new LeakageAnalysisVo();
		la.setResult(result);

		return la;
	}

	private BigDecimal calculationSelect(Map<String, LeakageAnalysisItemForm> itemMap,
			Map<String, ReportVo> deviceReportMap) throws FrameworkRuntimeException {
		BigDecimal result = new BigDecimal(0);
		LeakageAnalysisItemForm lai;
		ReportVo report;
		String direction, multiple;
		BigDecimal val;
		for (String deviceId : itemMap.keySet()) {
			lai = itemMap.get(deviceId);
			report = deviceReportMap.get(deviceId);
			if (report != null) {
				direction = lai.getDirection();
				multiple = lai.getMultiple();
				// 行度总和
				val = new BigDecimal(report.getSumVal());
				System.out.println(report.getSumVal());
				if (PipeConstants.DIRECTION_PLUS.equals(direction)) {
					result = result.add(val.multiply(new BigDecimal(multiple)));
				} else if (PipeConstants.DIRECTION_SUB.equals(direction)) {
					result = result.subtract(val.multiply(new BigDecimal(multiple)));
				}
			}
		}
		return result;
	}

	private BigDecimal calculationArea(Map<String, LeakageAnalysisItemForm> itemMap,
			Map<String, ReportAreaVo> areaReportMap) throws FrameworkRuntimeException {
		BigDecimal result = new BigDecimal(0);
		LeakageAnalysisItemForm lai;
		ReportAreaVo reportArea;
		String direction, multiple;
		BigDecimal val;
		for (String deviceId : itemMap.keySet()) {
			lai = itemMap.get(deviceId);
			reportArea = areaReportMap.get(deviceId);
			if (reportArea != null) {
				direction = lai.getDirection();
				multiple = lai.getMultiple();
				val = new BigDecimal(reportArea.getTotalVal());
				if (PipeConstants.DIRECTION_PLUS.equals(direction)) {
					result = result.add(val.multiply(new BigDecimal(multiple)));
				} else if (PipeConstants.DIRECTION_SUB.equals(direction)) {
					result = result.subtract(val.multiply(new BigDecimal(multiple)));
				}
			}
		}
		return result;
	}

	// @Override
	// public List<LeakageAnalysisVo> timing1(LeakageAnalysisForm
	// leakageAnalysisForm) throws FrameworkRuntimeException {
	// LoginCas loginCas = iAuthCasApi.get();
	// String operEid = loginCas.getOperEid();
	// List<LeakageAnalysisVo> las = new ArrayList<LeakageAnalysisVo>();
	// TimingCalculationBo timingCalculationBo = new TimingCalculationBo();
	// timingCalculationBo.setEnterpriseId(operEid);
	// List<TimingCalculationVo> tcs =
	// iTimingCalculationService.list(timingCalculationBo); // 查询数据
	// // 前端开启管漏监控，定时请求后台获取漏损分析数据
	// // 查看数据库中有哪些公式是开启状态(以后可以计算定时获取所需数据，直接返回问题坐标)
	// // 公式获取公式内容，进行类似选择计算，获取有问题的坐标
	// // 因为没有选择流向，目前只显示流过该坐标点的管道出现异常，以后可以限定范围，例如，所属区域、流经过管道等
	// Map<String, TimingFormulaVo> itemMap = null;
	// Map<String, ReportVo> deviceReportMap = null;
	// for (TimingCalculationVo tc : tcs) {
	// if (tc.getStatus().equals("NOUSE")) {
	// continue;
	// }
	// BigDecimal result = new BigDecimal(0);
	// BigDecimal upperLimit = new BigDecimal(tc.getUpperLimit());
	// BigDecimal lowerLimit = new BigDecimal(tc.getLowerLimit());
	// List<TimingFormulaVo> formulas = tc.getFormulas();
	// itemMap =
	// formulas.stream().collect(Collectors.toMap(TimingFormulaVo::getDeviceId, a ->
	// a, (k1, k2) -> k1));
	// // 通过deviceIds计算获取最新field数据
	// // 获取设备实施数据
	// // String[] deviceIds = itemMap.keySet().toArray(new
	// // String[itemMap.keySet().size()]);
	// List<String> deviceIds = new ArrayList<String>(itemMap.keySet());
	// FieldTypeEnum[] fieldTypeEna = new FieldTypeEnum[] { FieldTypeEnum.flwMeasure
	// };
	// List<ReportVo> deviceReports = this.iReportService.getDeviceRealTime(operEid,
	// deviceIds, fieldTypeEna);// 流量计数据
	// deviceReportMap = deviceReports.stream()
	// .collect(Collectors.toMap(ReportVo::getDeviceId, a -> a, (k1, k2) -> k1));
	// // 公式计算
	// result = calculationTiming(itemMap, deviceReportMap);
	// // 有问题
	// // 比较 大于最大值 小于最小值 报异常 upper_limit上线值 lower_limit 下限值
	// if (result.compareTo(upperLimit) == 1 || result.compareTo(lowerLimit) == -1)
	// {
	// LeakageAnalysisVo la = new LeakageAnalysisVo();
	// List<DeviceVo> devices = new ArrayList<DeviceVo>();
	// la.setDevices(devices);
	// la.setResult(result);
	// la.setTimingCalculationVo(tc);
	// deviceReportMap.forEach((k, v) -> {
	// DeviceVo d = new DeviceVo();
	// d.setDeviceId(k);
	// devices.add(d);
	// });
	// las.add(la);
	// }
	// }
	// // 返回有问题的设备，并高亮显示异常设备(传感器)
	// return las;
	// }

	@Override
	public List<TimingCalculationVo> timing(LeakageAnalysisForm leakageAnalysisForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		List<TimingCalculationVo> las = new ArrayList<TimingCalculationVo>();
		TimingCalculationBo timingCalculationBo = new TimingCalculationBo();
		timingCalculationBo.setEnterpriseId(operEid);
		// 查询数据
		List<TimingCalculationVo> tcs = iTimingCalculationService.list(timingCalculationBo);
		// 前端开启管漏监控，定时请求后台获取漏损分析数据
		// 查看数据库中有哪些公式是开启状态(以后可以计算定时获取所需数据，直接返回问题坐标)
		// 公式获取公式内容，进行类似选择计算，获取有问题的坐标
		// 因为没有选择流向，目前只显示流过该坐标点的管道出现异常，以后可以限定范围，例如，所属区域、流经过管道等
		Map<String, TimingFormulaVo> itemMap = null;
		Map<String, ReportVo> deviceReportMap = null;
		for (TimingCalculationVo tc : tcs) {
			if ("NOUSE".equals(tc.getStatus())) {
				// 没有使用的 不用计算结果 直接返回状态正常 数据0
				tc.setResult(new BigDecimal(0));
				// 正常
				tc.setIsNormal(true);
				las.add(tc);
				continue;
			}
			BigDecimal result = new BigDecimal(0);
			BigDecimal upperLimit = new BigDecimal(tc.getUpperLimit());
			BigDecimal lowerLimit = new BigDecimal(tc.getLowerLimit());
			List<TimingFormulaVo> formulas = tc.getFormulas();
			itemMap = formulas.stream().collect(Collectors.toMap(TimingFormulaVo::getDeviceId, a -> a, (k1, k2) -> k1));
			// 通过deviceIds计算获取最新field数据
			// 获取设备实施数据
			// String[] deviceIds = itemMap.keySet().toArray(new
			// String[itemMap.keySet().size()]);
			List<String> deviceIds = new ArrayList<String>(itemMap.keySet());
			FieldTypeEnum[] fieldTypeEna = new FieldTypeEnum[] { FieldTypeEnum.flwMeasure };
			// 流量计数据
			List<ReportVo> deviceReports = this.iReportService.getDeviceRealTime(operEid, deviceIds, fieldTypeEna);
			deviceReportMap = deviceReports.stream()
					.collect(Collectors.toMap(ReportVo::getDeviceId, a -> a, (k1, k2) -> k1));
			// 公式计算
			result = calculationTiming(itemMap, deviceReportMap);
			// 如果 下线值 小于上线值 说明 在这个范围内报警 1000 < x < 2000 则
			if (lowerLimit.compareTo(upperLimit) == -1) {
				// 有问题
				// 比较 大于最大值 小于最小值 报异常 upper_limit上线值 lower_limit 下限值
				if (result.compareTo(lowerLimit) == 1 && result.compareTo(upperLimit) == -1) {
					tc.setResult(result);
					// 异常
					tc.setIsNormal(false);
				} else {
					tc.setResult(result);
					// 正常
					tc.setIsNormal(true);
				}

			} else { // 下线值 > 上线值 说明 X>2000 && X<1000
				// 1000< X < 2000 是正常范围
				if (result.compareTo(upperLimit) == 1 && result.compareTo(lowerLimit) == -1) {
					tc.setResult(result);
					// 正常
					tc.setIsNormal(true);
				} else {
					tc.setResult(result);
					// 异常
					tc.setIsNormal(false);
				}

			}

			las.add(tc);
		}
		// 返回有问题的设备，并高亮显示异常设备(传感器)
		return las;
	}

	private BigDecimal calculationTiming(Map<String, TimingFormulaVo> itemMap, Map<String, ReportVo> deviceReportMap)
			throws FrameworkRuntimeException {
		BigDecimal result = new BigDecimal(0);
		TimingFormulaVo tf;
		ReportVo report;
		String direction, multiple;
		BigDecimal val;
		for (String deviceId : itemMap.keySet()) {
			tf = itemMap.get(deviceId);
			report = deviceReportMap.get(deviceId);
			if (report != null) {
				direction = tf.getDirection();
				multiple = tf.getMultiple();
				if (report.getFlwMeasure() != null) {
					val = new BigDecimal(report.getFlwMeasure());
				} else {
					val = new BigDecimal(0);
				}
				if (PipeConstants.DIRECTION_PLUS.equals(direction)) {
					result = result.add(val.multiply(new BigDecimal(multiple)));
				} else if (PipeConstants.DIRECTION_SUB.equals(direction)) {
					result = result.subtract(val.multiply(new BigDecimal(multiple)));
				}
			}
		}
		return result;
	}

}
