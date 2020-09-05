package com.dotop.pipe.web.factory.report;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dotop.pipe.web.api.factory.report.ITimingCalculationFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.service.report.ITimingCalculationService;
import com.dotop.pipe.api.service.report.ITimingFormulaService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.pipe.core.vo.report.TimingFormulaVo;
import com.dotop.pipe.data.report.core.bo.report.TimingCalculationBo;
import com.dotop.pipe.data.report.core.bo.report.TimingFormulaBo;
import com.dotop.pipe.data.report.core.form.TimingCalculationForm;
import com.dotop.pipe.data.report.core.form.TimingFormulaForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 
 */
@Component
public class TimingCalculationFactoryImpl implements ITimingCalculationFactory {

	private final static Logger logger = LogManager.getLogger(TimingCalculationFactoryImpl.class);

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private ITimingCalculationService iTimingCalculationService;

	@Autowired
	private ITimingFormulaService iTimingFormulaService;

	@Override
	public Pagination<TimingCalculationVo> page(TimingCalculationForm timingCalculationForm)
			throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 封装参数
		TimingCalculationBo timingCalculationBo = new TimingCalculationBo();
		timingCalculationBo.setPage(timingCalculationForm.getPage());
		timingCalculationBo.setPageSize(timingCalculationForm.getPageSize());
		timingCalculationBo.setEnterpriseId(operEid);
		Pagination<TimingCalculationVo> pagination = iTimingCalculationService.page(timingCalculationBo);
		return pagination;
	}

	@Override
	public TimingCalculationVo get(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 封装参数
		TimingCalculationBo timingCalculationBo = new TimingCalculationBo();
		timingCalculationBo.setEnterpriseId(operEid);
		timingCalculationBo.setTcId(timingCalculationForm.getTcId());
		TimingCalculationVo obj = iTimingCalculationService.get(timingCalculationBo);
		return obj;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public TimingCalculationVo add(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		// 封装参数
		TimingCalculationBo timingCalculationBo = new TimingCalculationBo();
		BeanUtils.copyProperties(timingCalculationForm, timingCalculationBo);
		timingCalculationBo.setEnterpriseId(operEid);
		timingCalculationBo.setUserBy(userBy);
		timingCalculationBo.setCurr(curr);
		boolean isExist = iTimingCalculationService.isExist(timingCalculationBo);
		if (isExist) {
			logger.error(LogMsg.to("ex", PipeExceptionConstants.TIMING_CALCULATION_CODE_EXIST, "msg",
					PipeExceptionConstants.getMessage(PipeExceptionConstants.TIMING_CALCULATION_CODE_EXIST)));
			throw new FrameworkRuntimeException(PipeExceptionConstants.TIMING_CALCULATION_CODE_EXIST,
					PipeExceptionConstants.getMessage(PipeExceptionConstants.TIMING_CALCULATION_CODE_EXIST));
		}
		// 创建公式
		TimingCalculationVo obj = iTimingCalculationService.add(timingCalculationBo);
		// 创建公式内容
		List<TimingFormulaForm> formulas = timingCalculationForm.getFormulas();
		formulas.forEach((item) -> {
			TimingFormulaBo timingFormulaBo = new TimingFormulaBo();
			timingFormulaBo.setDeviceId(item.getDeviceId());
			timingFormulaBo.setDirection(item.getDirection());
			timingFormulaBo.setMultiple(item.getMultiple());
			timingFormulaBo.setTcId(obj.getTcId());
			timingFormulaBo.setEnterpriseId(operEid);
			timingFormulaBo.setUserBy(userBy);
			timingFormulaBo.setCurr(curr);
			iTimingFormulaService.add(timingFormulaBo);
		});
		return obj;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public TimingCalculationVo edit(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		String tcId = timingCalculationForm.getTcId();
		List<TimingFormulaForm> formulas = timingCalculationForm.getFormulas();
		// 封装参数
		TimingCalculationBo timingCalculationBo = new TimingCalculationBo();
		BeanUtils.copyProperties(timingCalculationForm, timingCalculationBo);
		timingCalculationBo.setEnterpriseId(operEid);
		timingCalculationBo.setUserBy(userBy);
		timingCalculationBo.setCurr(curr);
		iTimingCalculationService.edit(timingCalculationBo);
		// 修改公式内容
		TimingFormulaBo timingFormulaBo = new TimingFormulaBo();
		timingFormulaBo.setEnterpriseId(operEid);
		timingFormulaBo.setTcId(tcId);

		List<TimingFormulaVo> tfs = iTimingFormulaService.list(timingFormulaBo);
		logger.info(LogMsg.to("tfs", tfs));

		Map<String, TimingFormulaForm> formulaMapNew = formulas.stream()
				.collect(Collectors.toMap(TimingFormulaForm::getDeviceId, a -> a, (k1, k2) -> k1));
		logger.info(LogMsg.to("formulaMapNew", formulaMapNew));

		Map<String, TimingFormulaVo> formulaMapDb = tfs.stream()
				.collect(Collectors.toMap(TimingFormulaVo::getDeviceId, a -> a, (k1, k2) -> k1));
		logger.info(LogMsg.to("formulaMapDb", formulaMapDb));

		// 存在拥有的id在上传的集合中，代表不需要创建
		Map<String, TimingFormulaVo> formulaMapExist = tfs.stream().filter(i -> {
			TimingFormulaForm tff = formulaMapNew.get(i.getDeviceId());
			if (tff == null) {
				// 如果不存在
				return false;
			}
			if (i.getDirection().equals(tff.getDirection()) && i.getMultiple().equals(tff.getMultiple())) {
				// 方向和倍数一样，理解存在
				return true;
			}
			return false;
		}).collect(Collectors.toMap(TimingFormulaVo::getDeviceId, a -> a, (k1, k2) -> k1));
		logger.info(LogMsg.to("formulaMapExist", formulaMapExist));

		// 存在拥有的id不在在上传的集合或方向倍数不相同中，代表需要删除
		Map<String, TimingFormulaVo> formulaMapDel = tfs.stream().filter(i -> {
			TimingFormulaForm tff = formulaMapNew.get(i.getDeviceId());
			if (tff == null) {
				// 如果不存在
				return true;
			}
			if (!i.getDirection().equals(tff.getDirection()) || !i.getMultiple().equals(tff.getMultiple())) {
				// 方向和倍数不一样，理解删除
				return true;
			}
			return false;
		}).collect(Collectors.toMap(TimingFormulaVo::getDeviceId, a -> a, (k1, k2) -> k1));
		logger.info(LogMsg.to("formulaMapDel", formulaMapDel));

		// 需要创建
		Map<String, TimingFormulaForm> formulaMapAdd = formulas.stream().filter(i -> {
			TimingFormulaVo tfv = formulaMapDb.get(i.getDeviceId());
			if (tfv == null) {
				// 如果不存在
				return true;
			}
			if (!i.getDirection().equals(tfv.getDirection()) || !i.getMultiple().equals(tfv.getMultiple())) {
				// 方向和倍数不一样，理解删除
				return true;
			}
			return false;
		}).collect(Collectors.toMap(TimingFormulaForm::getDeviceId, a -> a, (k1, k2) -> k1));
		logger.info(LogMsg.to("formulaMapAdd", formulaMapAdd));

		// 删除
		formulaMapDel.forEach((deviceId, tfv) -> {
			logger.debug(LogMsg.to("msg", "删除", "deviceId", deviceId));
			TimingFormulaBo tfb = new TimingFormulaBo();
			tfb.setEnterpriseId(operEid);
			tfb.setTfId(tfv.getTfId());
			tfb.setUserBy(userBy);
			tfb.setCurr(curr);
			iTimingFormulaService.del(tfb);
		});
		// 新增
		formulaMapAdd.forEach((deviceId, tff) -> {
			logger.debug(LogMsg.to("msg", "新增", "deviceId", deviceId));
			TimingFormulaBo tfb = new TimingFormulaBo();
			tfb.setDeviceId(tff.getDeviceId());
			tfb.setDirection(tff.getDirection());
			tfb.setMultiple(tff.getMultiple());
			tfb.setTcId(tcId);
			tfb.setEnterpriseId(operEid);
			tfb.setUserBy(userBy);
			tfb.setCurr(curr);
			iTimingFormulaService.add(tfb);
		});
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		String tcId = timingCalculationForm.getTcId();
		// 封装参数
		TimingCalculationBo timingCalculationBo = new TimingCalculationBo();
		timingCalculationBo.setEnterpriseId(operEid);
		timingCalculationBo.setTcId(tcId);
		timingCalculationBo.setUserBy(userBy);
		timingCalculationBo.setCurr(curr);
		iTimingCalculationService.del(timingCalculationBo);
		// 封装参数
		TimingFormulaBo timingFormulaBo = new TimingFormulaBo();
		timingFormulaBo.setEnterpriseId(operEid);
		timingFormulaBo.setTcId(tcId);
		timingFormulaBo.setUserBy(userBy);
		timingFormulaBo.setCurr(curr);
		iTimingFormulaService.del(timingFormulaBo);
		return null;
	}

	@Override
	public List<TimingCalculationVo> list(TimingCalculationForm timingCalculationForm)
			throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 封装参数
		TimingCalculationBo timingCalculationBo = new TimingCalculationBo();
		timingCalculationBo.setEnterpriseId(operEid);
		List<TimingCalculationVo> list = iTimingCalculationService.list(timingCalculationBo);
		return list;
	}

}
