package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.revenue.ISummaryAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SummaryVo;
import com.dotop.smartwater.project.module.dao.revenue.ISummaryAccountDao;

/**
 * 收银核算 -- 汇总核算
 * 

 * @date 2019年2月25日
 */
@Service
public class SummaryAccountServiceImpl implements ISummaryAccountService {

	private static final Logger LOGGER = LogManager.getLogger(AccountingServiceImpl.class);

	@Autowired
	private ISummaryAccountDao iSummaryAccountDao;

	@Override
	public SummaryVo summaryData(String year, String enterpriseid) {
		try {
			return iSummaryAccountDao.summaryData(year, enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<SummaryVo> summaryDetail(String date, String enterpriseid) {
		try {
			return iSummaryAccountDao.summaryDetail(date, enterpriseid);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<AccountingVo> summarySelfDetail(String date, String userid, String enterpriseid) {
		try {
			return iSummaryAccountDao.summarySelfDetail(date, enterpriseid, userid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public SummaryVo summarySelf(String date, String userid, String enterpriseid) {
		try {
			return iSummaryAccountDao.summarySelf(date, enterpriseid, userid);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
