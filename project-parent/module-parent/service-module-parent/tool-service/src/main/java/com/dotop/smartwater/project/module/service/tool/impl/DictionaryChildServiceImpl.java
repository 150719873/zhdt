package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.dto.DictionaryChildDto;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.dao.tool.IDictionaryChildDao;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 字典配置
 *

 * @date 2019年3月4日
 */
@Service
public class DictionaryChildServiceImpl implements IDictionaryChildService {

	private static final Logger LOGGER = LogManager.getLogger(DictionaryChildServiceImpl.class);

	@Autowired
	private IDictionaryChildDao iDictionaryChildDao;

	@Override
	public Pagination<DictionaryChildVo> page(DictionaryChildBo dictionaryChildBo) {
		try {
			Page<Object> pageHelper = PageHelper.startPage(dictionaryChildBo.getPage(),
					dictionaryChildBo.getPageCount());
			List<DictionaryChildVo> list = iDictionaryChildDao
					.list(BeanUtils.copy(dictionaryChildBo, DictionaryChildDto.class));
			Pagination<DictionaryChildVo> pagination = new Pagination<>(dictionaryChildBo.getPageCount(),
					dictionaryChildBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DictionaryChildVo get(DictionaryChildBo dictionaryChildBo) {
		try {
			return iDictionaryChildDao.get(BeanUtils.copy(dictionaryChildBo, DictionaryChildDto.class));
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DictionaryChildVo add(DictionaryChildBo dictionaryChildBo) {
		try {
			DictionaryChildDto dictionaryChildDto = BeanUtils.copy(dictionaryChildBo, DictionaryChildDto.class);
			dictionaryChildDto.setChildId(getDictionaryChildUUID(dictionaryChildBo));
			dictionaryChildDto.setIsDel(RootModel.NOT_DEL);
			iDictionaryChildDao.add(dictionaryChildDto);
			return BeanUtils.copy(dictionaryChildDto, DictionaryChildVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DictionaryChildVo edit(DictionaryChildBo dictionaryChildBo) {
		try {
			DictionaryChildDto dictionaryChildDto = BeanUtils.copy(dictionaryChildBo, DictionaryChildDto.class);
			iDictionaryChildDao.edit(dictionaryChildDto);
			return BeanUtils.copy(dictionaryChildDto, DictionaryChildVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(DictionaryChildBo dictionaryChildBo) {
		try {
			return String.valueOf(iDictionaryChildDao.del(BeanUtils.copy(dictionaryChildBo, DictionaryChildDto.class)));
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DictionaryChildVo> list(DictionaryChildBo dictionaryChildBo) {
		try {
			DictionaryChildDto dictionaryChildDto = BeanUtils.copy(dictionaryChildBo, DictionaryChildDto.class);
			return iDictionaryChildDao.list(dictionaryChildDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(DictionaryChildBo dictionaryChildBo) {
		try {
			DictionaryChildDto dictionaryChildDto = BeanUtils.copy(dictionaryChildBo, DictionaryChildDto.class);
			return iDictionaryChildDao.isExist(dictionaryChildDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	private static String getDictionaryChildUUID(DictionaryChildBo dictionaryChildBo) {
		if (StringUtils.isBlank(dictionaryChildBo.getDictionaryId())
				|| StringUtils.isBlank(dictionaryChildBo.getChildValue())) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "主键参数不能为空");
		}
		return dictionaryChildBo.getDictionaryId() + "," + dictionaryChildBo.getChildValue();
	}
}
