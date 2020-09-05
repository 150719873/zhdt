package com.dotop.pipe.service.product;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.product.IEnterpriseProductMapDao;
import com.dotop.pipe.api.service.product.IEnterpriseProductMapService;
import com.dotop.pipe.core.dto.product.EnterpriseProductMapDto;
import com.dotop.pipe.core.vo.product.EnterpriseProductMapVo;
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
 * @date 2018/10/30.
 */
@Service
public class EnterpriseProductMapServiceImpl implements IEnterpriseProductMapService {

	private final static Logger logger = LogManager.getLogger(EnterpriseProductMapServiceImpl.class);

	@Autowired
	private IEnterpriseProductMapDao iEnterpriseProductMapDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public EnterpriseProductMapVo add(String enterpriseId, String productId, Date curr, String userBy)
			throws FrameworkRuntimeException {
		try {
			String mapId = UuidUtils.getUuid();
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			EnterpriseProductMapDto enterpriseProductMapDto = new EnterpriseProductMapDto();
			enterpriseProductMapDto.setMapId(mapId);
			enterpriseProductMapDto.setEnterpriseId(enterpriseId);
			enterpriseProductMapDto.setProductId(productId);
			enterpriseProductMapDto.setCurr(curr);
			enterpriseProductMapDto.setUserBy(userBy);
			enterpriseProductMapDto.setIsDel(isDel);
			iEnterpriseProductMapDao.add(enterpriseProductMapDto);

			EnterpriseProductMapVo enterpriseProductMapVo = new EnterpriseProductMapVo();
			enterpriseProductMapVo.setMapId(mapId);
			return enterpriseProductMapVo;
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
	public EnterpriseProductMapVo get(String enterpriseId, String productId) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			EnterpriseProductMapDto enterpriseProductMapDto = new EnterpriseProductMapDto();
			enterpriseProductMapDto.setEnterpriseId(enterpriseId);
			enterpriseProductMapDto.setProductId(productId);
			enterpriseProductMapDto.setIsDel(isDel);
			return iEnterpriseProductMapDao.get(enterpriseProductMapDto);
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
	public Pagination<EnterpriseProductMapVo> page(Integer page, Integer pageSize, String enterpriseId)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			EnterpriseProductMapDto enterpriseProductMapDto = new EnterpriseProductMapDto();
			enterpriseProductMapDto.setEnterpriseId(enterpriseId);
			enterpriseProductMapDto.setIsDel(isDel);
			Page<Object> pageHelper = PageHelper.startPage(page, pageSize);
			List<EnterpriseProductMapVo> list = iEnterpriseProductMapDao.list(enterpriseProductMapDto);
			Pagination<EnterpriseProductMapVo> pagination = new Pagination<>(pageSize, page);
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(String mapId, String enterpriseId, String productId, Date curr, String userBy)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			EnterpriseProductMapDto enterpriseProductMapDto = new EnterpriseProductMapDto();
			enterpriseProductMapDto.setMapId(mapId);
			enterpriseProductMapDto.setEnterpriseId(enterpriseId);
			enterpriseProductMapDto.setProductId(productId);
			enterpriseProductMapDto.setNewIsDel(newIsDel);
			enterpriseProductMapDto.setCurr(curr);
			enterpriseProductMapDto.setUserBy(userBy);
			enterpriseProductMapDto.setIsDel(isDel);
			iEnterpriseProductMapDao.del(enterpriseProductMapDto);
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
	public List<EnterpriseProductMapVo> list(String enterpriseId) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			EnterpriseProductMapDto enterpriseProductMapDto = new EnterpriseProductMapDto();
			enterpriseProductMapDto.setEnterpriseId(enterpriseId);
			enterpriseProductMapDto.setIsDel(isDel);
			List<EnterpriseProductMapVo> list = iEnterpriseProductMapDao.list(enterpriseProductMapDto);
			return list;
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
