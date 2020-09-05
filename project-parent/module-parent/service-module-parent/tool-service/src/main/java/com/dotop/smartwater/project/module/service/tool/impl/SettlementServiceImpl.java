package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;
import com.dotop.smartwater.project.module.core.auth.dto.SettlementDto;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.dao.tool.ISettlementDao;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;

@Service
public class SettlementServiceImpl implements ISettlementService {

	private static final Logger LOGGER = LogManager.getLogger(SettlementServiceImpl.class);

	@Autowired
	private ISettlementDao iSettlementDao;

	@Override
	public SettlementVo getSettlement(String enterpriseid) {
		try {
			return iSettlementDao.getSettlement(enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addSettlement(SettlementBo settlementBo) {
		try {
			SettlementDto settlementDto = BeanUtils.copy(settlementBo, SettlementDto.class);
			iSettlementDao.addSettlement(settlementDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void editSettlement(SettlementBo settlementBo) {
		try {
			SettlementDto settlementDto = BeanUtils.copy(settlementBo, SettlementDto.class);
			if(iSettlementDao.getSettlement(settlementDto.getEnterpriseid()) == null) {
				iSettlementDao.addSettlement(settlementDto);
			}else {
				iSettlementDao.editSettlement(settlementDto);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void saveTestIot(SettlementBo settlementBo) {
		try {
			SettlementDto settlementDto = BeanUtils.copy(settlementBo, SettlementDto.class);
			iSettlementDao.saveTestIot(settlementDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<SettlementVo> getSettlements() {
		try {
			return iSettlementDao.getSettlements();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
