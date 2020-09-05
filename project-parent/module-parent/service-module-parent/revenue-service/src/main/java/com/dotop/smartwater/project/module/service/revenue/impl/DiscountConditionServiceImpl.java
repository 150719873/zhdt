package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

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
import com.dotop.smartwater.project.module.core.water.bo.ConditionBo;
import com.dotop.smartwater.project.module.core.water.dto.ConditionDto;
import com.dotop.smartwater.project.module.core.water.vo.ConditionVo;
import com.dotop.smartwater.project.module.dao.revenue.IDiscountConditionDao;
import com.dotop.smartwater.project.module.service.revenue.IDiscountConditionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class DiscountConditionServiceImpl implements IDiscountConditionService {

	private static final Logger LOGGER = LogManager.getLogger(DiscountConditionServiceImpl.class);

	@Autowired
	private IDiscountConditionDao iDiscountConditionDao;

	@Override
	public void savecondition(ConditionBo conditionBo) {
		try {
			// 参数转换
			ConditionDto conditionDto = new ConditionDto();
			BeanUtils.copyProperties(conditionBo, conditionDto);
			String id = UuidUtils.getUuid();
			conditionDto.setId(id);

			// 操作数据
			iDiscountConditionDao.savecondition(conditionDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void editcondition(ConditionBo conditionBo) {
		try {
			// 参数转换
			ConditionDto conditionDto = new ConditionDto();
			BeanUtils.copyProperties(conditionBo, conditionDto);

			// 操作数据
			iDiscountConditionDao.editcondition(conditionDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public ConditionVo getCondition(ConditionBo conditionBo) {
		try {
			// 参数转换
			ConditionDto conditionDto = new ConditionDto();
			BeanUtils.copyProperties(conditionBo, conditionDto);
			// 操作数据
			return iDiscountConditionDao.getCondition(conditionDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void deleteCond(ConditionBo conditionBo) {
		try {
			// 参数转换
			ConditionDto conditionDto = new ConditionDto();
			BeanUtils.copyProperties(conditionBo, conditionDto);

			// 操作数据
			iDiscountConditionDao.deleteCond(conditionDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<ConditionVo> findcondition(ConditionBo conditionBo) {
		try {
			// 参数转换
			ConditionDto conditionDto = new ConditionDto();
			BeanUtils.copyProperties(conditionBo, conditionDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(conditionBo.getPage(), conditionBo.getPageCount());
			List<ConditionVo> list = iDiscountConditionDao.findcondition(conditionBo);
			// 拼接数据返回
			return new Pagination<>(conditionBo.getPage(), conditionBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<ConditionVo> getcondtions(ConditionBo conditionBo) {
		try {
			// 参数转换
			ConditionDto conditionDto = new ConditionDto();
			BeanUtils.copyProperties(conditionBo, conditionDto);
			// 操作数据
			return iDiscountConditionDao.getcondtions(conditionBo);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
