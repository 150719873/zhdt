package com.dotop.pipe.service.factory;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.factory.IFactoryDao;
import com.dotop.pipe.api.service.factory.IFactoryService;
import com.dotop.pipe.core.dto.factory.FactoryDto;
import com.dotop.pipe.core.vo.factory.FactoryVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * @date 2018/10/25.
 */
@Service
public class FactoryServiceImpl implements IFactoryService {
	private final static Logger logger = LogManager.getLogger(FactoryServiceImpl.class);

	@Autowired
	private IFactoryDao iFactoryDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public FactoryVo add(String code, String name, String des, Date curr, String userBy)
			throws FrameworkRuntimeException {
		try {
			String factoryId = UuidUtils.getUuid();
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			FactoryDto factoryDto = new FactoryDto();
			factoryDto.setFactoryId(factoryId);
			factoryDto.setCode(code);
			factoryDto.setName(name);
			factoryDto.setDes(des);
			factoryDto.setCurr(curr);
			factoryDto.setUserBy(userBy);
			factoryDto.setIsDel(isDel);
			iFactoryDao.add(factoryDto);

			FactoryVo factoryVo = new FactoryVo();
			factoryVo.setFactoryId(factoryId);
			return factoryVo;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public FactoryVo get(String factoryId) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			FactoryDto factoryDto = new FactoryDto();
			factoryDto.setFactoryId(factoryId);
			factoryDto.setIsDel(isDel);
			return iFactoryDao.get(factoryDto);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public FactoryVo getByCode(String code) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			FactoryDto factoryDto = new FactoryDto();
			factoryDto.setCode(code);
			factoryDto.setIsDel(isDel);
			return iFactoryDao.get(factoryDto);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<FactoryVo> page(Integer page, Integer pageSize) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			FactoryDto factoryDto = new FactoryDto();
			factoryDto.setIsDel(isDel);
			Page<Object> pageHelper = PageHelper.startPage(page, pageSize);
			List<FactoryVo> list = iFactoryDao.list(factoryDto);
			Pagination<FactoryVo> pagination = new Pagination<>(pageSize, page);
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public List<FactoryVo> list() throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			FactoryDto factoryDto = new FactoryDto();
			factoryDto.setIsDel(isDel);
			return iFactoryDao.list(factoryDto);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public FactoryVo edit(String factoryId, String code, String name, String des, Date curr, String userBy)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			FactoryDto factoryDto = new FactoryDto();
			factoryDto.setFactoryId(factoryId);
			factoryDto.setCode(code);
			factoryDto.setName(name);
			factoryDto.setDes(des);
			factoryDto.setCurr(curr);
			factoryDto.setUserBy(userBy);
			factoryDto.setIsDel(isDel);
			iFactoryDao.edit(factoryDto);
			return null;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(String factoryId, Date curr, String userBy) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			FactoryDto factoryDto = new FactoryDto();
			factoryDto.setFactoryId(factoryId);
			factoryDto.setNewIsDel(newIsDel);
			factoryDto.setCurr(curr);
			factoryDto.setUserBy(userBy);
			factoryDto.setIsDel(isDel);
			iFactoryDao.del(factoryDto);
			return null;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}
}
