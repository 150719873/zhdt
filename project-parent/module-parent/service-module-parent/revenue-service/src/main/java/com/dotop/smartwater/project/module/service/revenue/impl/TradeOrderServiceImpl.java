package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.TradeOrderBo;
import com.dotop.smartwater.project.module.core.water.dto.TradeOrderDto;
import com.dotop.smartwater.project.module.core.water.vo.TradeOrderVo;
import com.dotop.smartwater.project.module.dao.revenue.ITradeOrderDao;
import com.dotop.smartwater.project.module.service.revenue.ITradeOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class TradeOrderServiceImpl implements ITradeOrderService {

	private static final Logger LOGGER = LogManager.getLogger(TradeOrderServiceImpl.class);

	@Autowired
	private ITradeOrderDao dao;

	@Override
	public Pagination<TradeOrderVo> page(TradeOrderBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			TradeOrderDto dto = new TradeOrderDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<TradeOrderVo> list = dao.getList(dto);
			Pagination<TradeOrderVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int save(TradeOrderBo bo) {
		try {
			TradeOrderDto dto = new TradeOrderDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());
			return dao.save(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public TradeOrderVo get(TradeOrderBo bo) {
		try {
			TradeOrderDto dto = new TradeOrderDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.get(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String del(TradeOrderBo bo) {
		try {
			TradeOrderDto dto = new TradeOrderDto();
			BeanUtils.copyProperties(bo, dto);
			dao.del(dto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int update(TradeOrderBo bo) {
		try {
			TradeOrderDto dto = new TradeOrderDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.update(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int updatePayStatus(TradeOrderBo bo) {
		try {
			TradeOrderDto dto = new TradeOrderDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.updatePayStatus(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
