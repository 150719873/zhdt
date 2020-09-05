package com.dotop.smartwater.project.auth.service.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.auth.dao.IMdDockingTempDao;
import com.dotop.smartwater.project.auth.service.IMdDockingTempService;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingTempBo;
import com.dotop.smartwater.project.module.core.auth.dto.MdDockingTempDto;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**

 */
@Component
public class MdDockingTempServiceImpl implements IMdDockingTempService {

	private static final Logger LOGGER = LogManager.getLogger(MdDockingTempServiceImpl.class);

	@Autowired
	private IMdDockingTempDao iMdDockingTempDao;

	@Override
	public Pagination<MdDockingTempVo> page(MdDockingTempBo bo) {
		try {
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<MdDockingTempVo> list = iMdDockingTempDao.list(BeanUtils.copy(bo, MdDockingTempDto.class));
			// 拼接数据返回
			return new Pagination<>(bo.getPage(), bo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public MdDockingTempVo get(MdDockingTempBo bo) {
		try {
			MdDockingTempVo vo = iMdDockingTempDao.get(BeanUtils.copy(bo, MdDockingTempDto.class));
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MdDockingTempVo add(MdDockingTempBo bo) {
		try {
			iMdDockingTempDao.add(BeanUtils.copy(bo, MdDockingTempDto.class));
			return BeanUtils.copy(bo, MdDockingTempVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MdDockingTempVo edit(MdDockingTempBo bo) {
		try {
			iMdDockingTempDao.edit(BeanUtils.copy(bo, MdDockingTempDto.class));
			return BeanUtils.copy(bo, MdDockingTempVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(MdDockingTempBo bo) {
		try {
			MdDockingTempDto dto = new MdDockingTempDto();
			dto.setId(bo.getId());
			iMdDockingTempDao.del(dto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<MdDockingTempVo> list(MdDockingTempBo bo) {
		try {
			List<MdDockingTempVo> list = iMdDockingTempDao.list(BeanUtils.copy(bo, MdDockingTempDto.class));
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
