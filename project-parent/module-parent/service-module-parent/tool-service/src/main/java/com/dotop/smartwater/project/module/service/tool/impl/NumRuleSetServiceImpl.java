package com.dotop.smartwater.project.module.service.tool.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.NumRuleSetBo;
import com.dotop.smartwater.project.module.core.water.dto.NumRuleSetDto;
import com.dotop.smartwater.project.module.core.water.vo.NumRuleSetVo;
import com.dotop.smartwater.project.module.dao.tool.INumRuleSetDao;
import com.dotop.smartwater.project.module.service.tool.INumRuleSetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典配置
 *

 * @date 2019年3月4日
 */
@Service
public class NumRuleSetServiceImpl implements INumRuleSetService {

	private static final Logger LOGGER = LogManager.getLogger(NumRuleSetServiceImpl.class);

	@Resource
	private INumRuleSetDao iNumRuleSetDao;

	@Override
	public List<NumRuleSetVo> findByEnterpriseId(NumRuleSetBo numRuleSetBo) {
		try {
			// 参数转换
			NumRuleSetDto numRuleSetDto = BeanUtils.copy(numRuleSetBo, NumRuleSetDto.class);
			return iNumRuleSetDao.findByEnterpriseId(numRuleSetDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<NumRuleSetVo> getBaseRuleList() {
		try {
			return iNumRuleSetDao.getBaseRuleList();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public NumRuleSetVo add(NumRuleSetBo numRuleSetBo) {
		try {
			NumRuleSetDto numRuleSetDto = BeanUtils.copy(numRuleSetBo, NumRuleSetDto.class);
			numRuleSetDto.setId(UuidUtils.getUuid());
			iNumRuleSetDao.add(numRuleSetDto);
			return BeanUtils.copy(numRuleSetDto, NumRuleSetVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public NumRuleSetVo edit(NumRuleSetBo numRuleSetBo) {
		try {
			NumRuleSetDto numRuleSetDto = BeanUtils.copy(numRuleSetBo, NumRuleSetDto.class);
			iNumRuleSetDao.edit(numRuleSetDto);
			return BeanUtils.copy(numRuleSetDto, NumRuleSetVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<NumRuleSetVo> page(NumRuleSetBo numRuleSetBo) {
		try {
			NumRuleSetDto numRuleSetDto = BeanUtils.copy(numRuleSetBo, NumRuleSetDto.class);
			Page<Object> pageHelper = PageHelper.startPage(numRuleSetBo.getPage(), numRuleSetBo.getPageCount());
			List<NumRuleSetVo> list = iNumRuleSetDao.findByEnterpriseId(numRuleSetDto);
			Pagination<NumRuleSetVo> pagination = new Pagination<>(numRuleSetBo.getPageCount(), numRuleSetBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public NumRuleSetVo findByEnterpriseIdAndRuleId(NumRuleSetBo numRuleSetBo) {
		try {
			return iNumRuleSetDao.findByEnterpriseIdAndRuleId(BeanUtils.copy(numRuleSetBo, NumRuleSetDto.class));
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int deleteByEnterpriseIdAndRuleId(NumRuleSetBo numRuleSetBo) {
		try {
			iNumRuleSetDao.deleteByEnterpriseIdAndRuleId(BeanUtils.copy(numRuleSetBo, NumRuleSetDto.class));
			return 1;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
