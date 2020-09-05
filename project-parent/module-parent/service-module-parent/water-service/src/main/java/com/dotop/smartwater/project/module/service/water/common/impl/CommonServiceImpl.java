package com.dotop.smartwater.project.module.service.water.common.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.dao.water.common.ICommonDao;
import com.dotop.smartwater.project.module.service.water.common.ICommonService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class CommonServiceImpl implements ICommonService {

	private static final Logger LOGGER = LogManager.getLogger(CommonServiceImpl.class);

	@Autowired
	private ICommonDao iCommonDao;

	@Override
	public List<Map<String, Object>> list(String sql, Map<String, String> sqlParams) {
		try {
			sqlParams.put("sqlInject", sql);
			return iCommonDao.select(sqlParams);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<Map<String, Object>> page(String sql, Map<String, String> sqlParams, Integer page,
			Integer pageCount) {
		try {
			sqlParams.put("sqlInject", sql);
			Page<Object> pageHelper = PageHelper.startPage(page, pageCount);
			List<Map<String, Object>> list = iCommonDao.select(sqlParams);
			return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
