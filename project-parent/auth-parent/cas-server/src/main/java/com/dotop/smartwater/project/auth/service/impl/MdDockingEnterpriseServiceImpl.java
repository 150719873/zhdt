package com.dotop.smartwater.project.auth.service.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.auth.dao.IMdDockingEnterpriseDao;
import com.dotop.smartwater.project.auth.service.IMdDockingEnterpriseService;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingEnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.dto.MdDockingEnterpriseDto;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingEnterpriseVo;
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
public class MdDockingEnterpriseServiceImpl implements IMdDockingEnterpriseService {

	private static final Logger LOGGER = LogManager.getLogger(MdDockingEnterpriseServiceImpl.class);

	@Autowired
	private IMdDockingEnterpriseDao iMdDockingEnterpriseDao;

	@Override
	public Pagination<MdDockingEnterpriseVo> page(MdDockingEnterpriseBo bo) {
		try {
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<MdDockingEnterpriseVo> list = iMdDockingEnterpriseDao.list(BeanUtils.copy(bo, MdDockingEnterpriseDto.class));
			// 拼接数据返回
			return new Pagination<>(bo.getPage(), bo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public MdDockingEnterpriseVo get(MdDockingEnterpriseBo bo) {
		try {
			MdDockingEnterpriseVo vo = iMdDockingEnterpriseDao.get(BeanUtils.copy(bo, MdDockingEnterpriseDto.class));
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MdDockingEnterpriseVo add(MdDockingEnterpriseBo bo) {
		try {
			iMdDockingEnterpriseDao.add(BeanUtils.copy(bo, MdDockingEnterpriseDto.class));
			return BeanUtils.copy(bo, MdDockingEnterpriseVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MdDockingEnterpriseVo edit(MdDockingEnterpriseBo bo) {
		try {
			iMdDockingEnterpriseDao.edit(BeanUtils.copy(bo, MdDockingEnterpriseDto.class));
			return BeanUtils.copy(bo, MdDockingEnterpriseVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(MdDockingEnterpriseBo bo) {
		try {
			MdDockingEnterpriseDto dto = new MdDockingEnterpriseDto();
			dto.setId(bo.getId());
			iMdDockingEnterpriseDao.del(dto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<MdDockingEnterpriseVo> list(MdDockingEnterpriseBo bo) {
		try {
			List<MdDockingEnterpriseVo> list = iMdDockingEnterpriseDao.list(BeanUtils.copy(bo, MdDockingEnterpriseDto.class));
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<EnterpriseVo> enterpriseList() {
		try {
			return iMdDockingEnterpriseDao.enterpriseList();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
