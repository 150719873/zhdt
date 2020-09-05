package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.revenue.IPerformanceWeightService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.PerforWeightBo;
import com.dotop.smartwater.project.module.core.water.dto.PerforWeightDto;
import com.dotop.smartwater.project.module.core.water.vo.PerforWeightVo;
import com.dotop.smartwater.project.module.dao.revenue.IPerformanceWeightDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class PerformanceWeightServiceImpl implements IPerformanceWeightService {

	@Autowired
	private IPerformanceWeightDao iPerformanceWeightDao;

	private static final Logger LOGGER = LogManager.getLogger(PerformanceWeightServiceImpl.class);

	@Override
	public Pagination<PerforWeightVo> page(PerforWeightBo perforWeightBo) {
		try {
			// 参数转换
			PerforWeightDto perforWeightDto = new PerforWeightDto();
			BeanUtils.copyProperties(perforWeightBo, perforWeightDto);
			Page<Object> pageHelper = PageHelper.startPage(perforWeightBo.getPage(), perforWeightBo.getPageCount());
			List<PerforWeightVo> list = iPerformanceWeightDao.getList(perforWeightDto);
			Pagination<PerforWeightVo> pagination = new Pagination<>(perforWeightBo.getPageCount(),
					perforWeightBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		}catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int save(PerforWeightBo perforWeightBo) {
		try {
			// 参数转换
			PerforWeightDto perforWeightDto = new PerforWeightDto();
			BeanUtils.copyProperties(perforWeightBo, perforWeightDto);
			perforWeightDto.setId(UuidUtils.getUuid());
			return iPerformanceWeightDao.addWeight(perforWeightDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int update(PerforWeightBo perforWeightBo) {
		try {
			// 参数转换
			PerforWeightDto perforWeightDto = new PerforWeightDto();
			BeanUtils.copyProperties(perforWeightBo, perforWeightDto);
			return iPerformanceWeightDao.updateWeight(perforWeightDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int delete(PerforWeightBo perforWeightBo) {
		try {
			// 参数转换
			PerforWeightDto perforWeightDto = new PerforWeightDto();
			BeanUtils.copyProperties(perforWeightBo, perforWeightDto);
			return iPerformanceWeightDao.delete(perforWeightDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public PerforWeightVo getWeight(PerforWeightBo perforWeightBo) {
		try {
			// 参数转换
			PerforWeightDto perforWeightDto = new PerforWeightDto();
			BeanUtils.copyProperties(perforWeightBo, perforWeightDto);
			return iPerformanceWeightDao.getWeight(perforWeightDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
