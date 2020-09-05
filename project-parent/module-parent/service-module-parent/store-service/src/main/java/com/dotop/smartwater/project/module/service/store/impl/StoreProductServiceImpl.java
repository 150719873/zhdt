package com.dotop.smartwater.project.module.service.store.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.store.IStoreProductService;
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
import com.dotop.smartwater.project.module.core.water.bo.StoreProductBo;
import com.dotop.smartwater.project.module.core.water.dto.StoreProductDto;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.dao.store.IStoreProductDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/*
 * 仓库管理-产品管理

 * @date 2018-11-21 上午 11:00
 * 
 */
@Service
public class StoreProductServiceImpl implements IStoreProductService {

	private static final Logger LOGGER = LogManager.getLogger(StoreProductServiceImpl.class);

	@Autowired
	private IStoreProductDao iStoreProductDao;

	@Override
	public Pagination<StoreProductVo> getProByCon(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(storeProductBo.getPage(), storeProductBo.getPageCount());
			List<StoreProductVo> list = iStoreProductDao.getProByCon(storeProductDto);
			// 拼接数据返回
			return new Pagination<>(storeProductBo.getPage(), storeProductBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public StoreProductVo getProductByNo(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.getProductByNo(storeProductDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer addProduct(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.addProduct(storeProductDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer editProduct(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.editProduct(storeProductDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer changeStatus(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.changeStatus(storeProductDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer checkProductNo(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.checkProductNo(storeProductDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	

	@Override
	public Integer checkProductName(StoreProductBo storeProductBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.checkProductName(storeProductDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer deleteProduct(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.deleteProduct(storeProductDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<StoreProductVo> getLinePro(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.getProByCon(storeProductDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public List<StoreProductVo> list(StoreProductBo storeProductBo) {
		try {
			// 参数转换
			StoreProductDto storeProductDto = new StoreProductDto();
			BeanUtils.copyProperties(storeProductBo, storeProductDto);
			// 操作数据
			return iStoreProductDao.getProByCon(storeProductDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
