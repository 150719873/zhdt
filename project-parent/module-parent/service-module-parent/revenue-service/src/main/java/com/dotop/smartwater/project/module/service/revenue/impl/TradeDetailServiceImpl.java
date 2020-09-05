package com.dotop.smartwater.project.module.service.revenue.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.TradeDetailBo;
import com.dotop.smartwater.project.module.core.water.dto.TradeDetailDto;
import com.dotop.smartwater.project.module.core.water.vo.TradeDetailVo;
import com.dotop.smartwater.project.module.dao.revenue.ITradeDetailDao;
import com.dotop.smartwater.project.module.service.revenue.ITradeDetailService;

@Service
public class TradeDetailServiceImpl implements ITradeDetailService {

	private static final Logger LOGGER = LogManager.getLogger(TradeDetailServiceImpl.class);

	@Autowired
	private ITradeDetailDao dao;

	@Override
	public TradeDetailVo add(TradeDetailBo bo) {
		try {
			TradeDetailDto dto = new TradeDetailDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());
			dao.add(dto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public TradeDetailVo edit(TradeDetailBo bo) {
		try {
			TradeDetailDto dto = new TradeDetailDto();
			BeanUtils.copyProperties(bo, dto);
			dao.edit(dto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public TradeDetailVo get(TradeDetailBo bo) {
		try {
			TradeDetailDto dto = new TradeDetailDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.get(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public TradeDetailVo getDetail(TradeDetailBo bo) {
		try {
			TradeDetailDto dto = new TradeDetailDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.getDetail(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
