package com.dotop.pipe.web.factory.historylog;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.web.api.factory.historylog.IHistoryLogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.service.historylog.IHistoryLogService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.historylog.HistoryLogBo;
import com.dotop.pipe.core.dto.historylog.ChangeDto;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.HistoryLogForm;
import com.dotop.pipe.core.utils.HistoryLogUtils;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.historylog.HistoryLogVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 
 *
 * @date 2019年2月15日
 */
@Component
public class HistoryLogFactoryImpl implements IHistoryLogFactory {

	private final static Logger logger = LogManager.getLogger(HistoryLogFactoryImpl.class);

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private IHistoryLogService iHistoryLogService;

	@Override
	public Pagination<HistoryLogVo> page(HistoryLogForm historyLogForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 封装参数
		HistoryLogBo historyLogBo = new HistoryLogBo();
		historyLogBo.setPage(historyLogForm.getPage());
		historyLogBo.setPageSize(historyLogForm.getPageSize());
		historyLogBo.setEnterpriseId(operEid);
		Pagination<HistoryLogVo> pagination = iHistoryLogService.page(historyLogBo);
		return pagination;
	}

	@Override
	public List<HistoryLogVo> list(HistoryLogForm historyLogForm) throws FrameworkRuntimeException {
		/*
		 * LoginCas loginCas = iAuthCasApi.get(); String operEid =
		 * loginCas.getOperEid(); Integer limit = historyLogForm.getLimit(); String type
		 * = historyLogForm.getType();
		 * 
		 * HistoryLogBo historyLogBo = new HistoryLogBo(); historyLogBo.setLimit(limit);
		 * historyLogBo.setType(type); historyLogBo.setEnterpriseId(operEid);
		 * List<HistoryLogVo> list = iHistoryLogService.list(historyLogBo); return list;
		 */
		return null;
	}

	@Override
	public HistoryLogVo get(HistoryLogForm historyLogForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();

		// 封装参数
		HistoryLogBo historyLogBo = new HistoryLogBo();
		historyLogBo.setEnterpriseId(operEid);
		historyLogBo.setId(historyLogForm.getId());

		HistoryLogVo historyLogVo = iHistoryLogService.get(historyLogBo);
		return historyLogVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public HistoryLogVo add(HistoryLogForm historyLogForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();

		DeviceVo deviceVo = new DeviceVo();

		deviceVo.setDeviceId("1234567");
		deviceVo.setCode("1235");
		deviceVo.setName("nameddd");
		deviceVo.setDes("maiou");
		deviceVo.setAddress("1125");
		deviceVo.setLength("1252");
		deviceVo.setDepth("152525");
		deviceVo.setPipeElevation("dddd");
		deviceVo.setGroundElevation("122");
		deviceVo.setInstallDate(new Date());

		DeviceForm deviceForm = new DeviceForm();
		deviceForm.setDeviceId("1234567");
		deviceForm.setCode("1235");
		deviceForm.setName("nameddd");
		deviceForm.setDes("maioshu");
		deviceForm.setAddress("12225");
		deviceForm.setLength("1252");
		deviceForm.setDepth("12");
		deviceForm.setPipeElevation("dddd");
		deviceForm.setGroundElevation("122");
		deviceForm.setInstallDate(new Date());
		deviceForm.setProductId("123522");
		deviceForm.setAreaId("ddd");

		List<ChangeDto> list = HistoryLogUtils.compareObj(deviceVo, deviceForm);
		iHistoryLogService.add(list, operEid, userBy, "1234567");
		return null;
	}

}
