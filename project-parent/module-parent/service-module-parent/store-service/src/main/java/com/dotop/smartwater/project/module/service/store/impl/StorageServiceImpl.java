package com.dotop.smartwater.project.module.service.store.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.StorageBo;
import com.dotop.smartwater.project.module.core.water.dto.StorageDto;
import com.dotop.smartwater.project.module.core.water.dto.StoreProductDto;
import com.dotop.smartwater.project.module.core.water.vo.StorageVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.dao.store.IStorageDao;
import com.dotop.smartwater.project.module.dao.store.IStoreProductDao;
import com.dotop.smartwater.project.module.service.store.IStorageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class StorageServiceImpl implements IStorageService {

	private static final Logger LOGGER = LogManager.getLogger(StorageServiceImpl.class);

	@Autowired
	private IStorageDao iStorageDao;

	@Autowired
	private IStoreProductDao iStoreProductDao;

	@Override
	public Pagination<StorageVo> getStorageByCon(StorageBo storageBo) {
		try {
			// 参数转换
			StorageDto storageDto = new StorageDto();
			BeanUtils.copyProperties(storageBo, storageDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(storageBo.getPage(), storageBo.getPageCount());
			List<StorageVo> list = iStorageDao.getStorageByCon(storageDto);
			// 拼接数据返回
			return new Pagination<>(storageBo.getPage(), storageBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public StorageVo getStorageByNo(StorageBo storageBo) {
		try {
			// 参数转换
			StorageDto storageDto = new StorageDto();
			BeanUtils.copyProperties(storageBo, storageDto);
			// 操作数据
			StorageVo storage = iStorageDao.getStorageByNo(storageDto);
			if (storage != null) {
				StoreProductDto storeProductDto = new StoreProductDto();
				storeProductDto.setEnterpriseid(storageDto.getEnterpriseid());
				storeProductDto.setProductNo(storage.getProductNo());
				storage.setProduct(iStoreProductDao.getProductByNo(storeProductDto));
			}
			return storage;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer editStorage(StorageBo storageBo) {
		try {
			// 参数转换
			StorageDto storageDto = new StorageDto();
			BeanUtils.copyProperties(storageBo, storageDto);
			// 操作数据
			return iStorageDao.editStorage(storageDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer addStorage(StorageBo storageBo) {
		try {
			// 参数转换
			StorageDto storageDto = new StorageDto();
			BeanUtils.copyProperties(storageBo, storageDto);

			return iStorageDao.addStorage(storageDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer deleteStorage(StorageBo storageBo) {
		try {
			// 参数转换
			StorageDto storageDto = new StorageDto();
			BeanUtils.copyProperties(storageBo, storageDto);
			// 操作数据
			return iStorageDao.deleteStorage(storageDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer confirmStorage(StorageBo storageBo) {
		try {
			// 参数转换
			StorageDto storageDto = new StorageDto();
			BeanUtils.copyProperties(storageBo, storageDto);
			// 操作数据
			return iStorageDao.confirmStorage(storageDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<StoreProductVo> getProIn(StorageBo storageBo) {
		try {
			// 参数转换
			StorageDto storageDto = new StorageDto();
			BeanUtils.copyProperties(storageBo, storageDto);
			// 操作数据
			return iStorageDao.getProIn(storageDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
