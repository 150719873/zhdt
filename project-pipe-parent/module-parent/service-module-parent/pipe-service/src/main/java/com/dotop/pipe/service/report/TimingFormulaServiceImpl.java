package com.dotop.pipe.service.report;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.report.ITimingFormulaDao;
import com.dotop.pipe.api.service.report.ITimingFormulaService;
import com.dotop.pipe.core.vo.report.TimingFormulaVo;
import com.dotop.pipe.data.report.core.bo.report.TimingFormulaBo;
import com.dotop.pipe.data.report.core.dto.report.TimingFormulaDto;
import com.dotop.pipe.service.device.DeviceServiceImpl;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;

@Service
public class TimingFormulaServiceImpl implements ITimingFormulaService {

	private final static Logger logger = LogManager.getLogger(DeviceServiceImpl.class);

	@Autowired
	private ITimingFormulaDao iTimingFormulaDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public TimingFormulaVo add(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			String tfId = UuidUtils.getUuid();
			// 参数转换
			TimingFormulaDto timingFormulaDto = new TimingFormulaDto();
			BeanUtils.copyProperties(timingFormulaBo, timingFormulaDto);
			timingFormulaDto.setTfId(tfId);
			timingFormulaDto.setIsDel(isDel);
			iTimingFormulaDao.add(timingFormulaDto);

			TimingFormulaVo timingFormula = new TimingFormulaVo();
			timingFormula.setTfId(tfId);
			return timingFormula;

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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public TimingFormulaVo edit(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			TimingFormulaDto timingFormulaDto = new TimingFormulaDto();
			BeanUtils.copyProperties(timingFormulaBo, timingFormulaDto);
			timingFormulaDto.setIsDel(isDel);
			iTimingFormulaDao.edit(timingFormulaDto);
			return null;
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			TimingFormulaDto timingFormulaDto = new TimingFormulaDto();
			timingFormulaDto.setIsDel(isDel);
			timingFormulaDto.setNewIsDel(newIsDel);
			timingFormulaDto.setEnterpriseId(timingFormulaBo.getEnterpriseId());
			timingFormulaDto.setTfId(timingFormulaBo.getTfId());
			timingFormulaDto.setTcId(timingFormulaBo.getTcId());
			timingFormulaDto.setCurr(timingFormulaBo.getCurr());
			timingFormulaDto.setUserBy(timingFormulaBo.getUserBy());
			iTimingFormulaDao.del(timingFormulaDto);
			return null;
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
	public boolean isExist(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			TimingFormulaDto timingFormulaDto = new TimingFormulaDto();
			timingFormulaDto.setDeviceId(timingFormulaBo.getDeviceId());
			timingFormulaDto.setEnterpriseId(timingFormulaBo.getEnterpriseId());
			timingFormulaDto.setIsDel(isDel);
			Boolean isExist = iTimingFormulaDao.isExist(timingFormulaDto);
			return isExist;
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
	public List<TimingFormulaVo> list(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			TimingFormulaDto timingFormulaDto = new TimingFormulaDto();
			timingFormulaDto.setTcId(timingFormulaBo.getTcId());
			timingFormulaDto.setEnterpriseId(timingFormulaBo.getEnterpriseId());
			timingFormulaDto.setIsDel(isDel);
			List<TimingFormulaVo> list = iTimingFormulaDao.list(timingFormulaDto);
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
