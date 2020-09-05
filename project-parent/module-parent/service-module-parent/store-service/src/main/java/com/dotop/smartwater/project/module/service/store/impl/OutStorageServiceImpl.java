package com.dotop.smartwater.project.module.service.store.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.store.IOutStorageService;
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
import com.dotop.smartwater.project.module.core.water.bo.OutStorageBo;
import com.dotop.smartwater.project.module.core.water.bo.StorageBo;
import com.dotop.smartwater.project.module.core.water.dto.OutStorageDto;
import com.dotop.smartwater.project.module.core.water.dto.StorageDto;
import com.dotop.smartwater.project.module.core.water.vo.OutStorageVo;
import com.dotop.smartwater.project.module.core.water.vo.StorageVo;
import com.dotop.smartwater.project.module.dao.store.IOutStorageDao;
import com.dotop.smartwater.project.module.dao.store.IStorageDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 
 * 仓库管理-出库管理ServiceImpl
 * 

 * @date 2018-11-30 下午 15:49
 *
 */

@Service
public class OutStorageServiceImpl implements IOutStorageService {

	private static final Logger LOGGER = LogManager.getLogger(OutStorageServiceImpl.class);

	@Autowired
	private IOutStorageDao iOutStorageDao;

	@Autowired
	private IStorageDao iStorageDao;

	@Override
	public Pagination<OutStorageVo> getOutStorByCon(OutStorageBo outStorageBo) {
		try {
			// 参数转换
			OutStorageDto outStorageDto = new OutStorageDto();
			BeanUtils.copyProperties(outStorageBo, outStorageDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(outStorageBo.getPage(), outStorageBo.getPageCount());
			List<OutStorageVo> list = iOutStorageDao.getOutStorByCon(outStorageDto);
			// 拼接数据返回
			return new Pagination<>(outStorageBo.getPage(), outStorageBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OutStorageVo getOutStorByNo(OutStorageBo outStorageBo) {
		try {
			// 参数转换
			OutStorageDto outStorageDto = new OutStorageDto();
			BeanUtils.copyProperties(outStorageBo, outStorageDto);
			// 操作数据
			return iOutStorageDao.getOutStorByNo(outStorageDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer addOutStor(OutStorageBo outStorageBo) {
		try {
			// 参数转换
			OutStorageDto outStorageDto = new OutStorageDto();
			BeanUtils.copyProperties(outStorageBo, outStorageDto);

			// status? 0: 直接出库 1 保存出库信息不出库
			if (outStorageDto.getStatus() == null || outStorageDto.getStatus() != 0) {
				return iOutStorageDao.addOutStor(outStorageDto);
			}

			StorageDto storageDto = new StorageDto();
			storageDto.setEnterpriseid(outStorageDto.getEnterpriseid());
			storageDto.setRepoNo(outStorageDto.getRepoNo());
			storageDto.setProductNo(outStorageDto.getProductNo());
			List<StorageVo> listStore = iStorageDao.getByProNoAndRepoNo(storageDto);
			Integer count = outStorageDto.getQuantity();
			for (StorageVo stor : listStore) {
				if (stor.getQuantity() <= count) {
					StorageDto changeStor = new StorageDto();
					BeanUtils.copyProperties(stor, changeStor);
					count = count - stor.getQuantity();
					changeStor.setQuantity(0);
					iStorageDao.editStorage(changeStor);
				} else {
					StorageDto changeStor = new StorageDto();
					BeanUtils.copyProperties(stor, changeStor);
					changeStor.setQuantity(stor.getQuantity() - count);
					iStorageDao.editStorage(changeStor);
					break;
				}
			}

			// 操作数据
			return iOutStorageDao.addOutStor(outStorageDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer editOutStor(OutStorageBo outStorageBo) {
		try {
			// 参数转换
			OutStorageDto outStorageDto = new OutStorageDto();
			BeanUtils.copyProperties(outStorageBo, outStorageDto);

			if (outStorageDto.getStatus() == null || outStorageDto.getStatus() != 0) {
				// 修改保存出库的信息 没有出库
				return iOutStorageDao.editOutStor(outStorageDto);
			}

			StorageDto storageDto = new StorageDto();
			storageDto.setEnterpriseid(outStorageDto.getEnterpriseid());
			storageDto.setRepoNo(outStorageDto.getRepoNo());
			storageDto.setProductNo(outStorageDto.getProductNo());
			List<StorageVo> listStore = iStorageDao.getByProNoAndRepoNo(storageDto);
			Integer count = outStorageDto.getQuantity();
			for (StorageVo stor : listStore) {
				if (stor.getQuantity() <= count) {
					StorageDto changeStor = new StorageDto();
					BeanUtils.copyProperties(stor, changeStor);
					count = count - stor.getQuantity();
					changeStor.setQuantity(0);
					iStorageDao.editStorage(changeStor);
				} else {
					StorageDto changeStor = new StorageDto();
					BeanUtils.copyProperties(stor, changeStor);
					changeStor.setQuantity(stor.getQuantity() - count);
					iStorageDao.editStorage(changeStor);
					break;
				}
			}
			// 操作数据
			return iOutStorageDao.editOutStor(outStorageDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer deleteOutStor(OutStorageBo outStorageBo) {
		try {
			// 参数转换
			OutStorageDto outStorageDto = new OutStorageDto();
			BeanUtils.copyProperties(outStorageBo, outStorageDto);
			// 操作数据
			return iOutStorageDao.deleteOutStor(outStorageDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer confirmOutStor(OutStorageBo outStorageBo) {
		try {
			// 参数转换
			OutStorageDto outStorageDto = new OutStorageDto();
			BeanUtils.copyProperties(outStorageBo, outStorageDto);
			if (outStorageDto.getStatus() == null || outStorageDto.getStatus() != 0) {
				// 修改保存出库的信息 没有出库
				return iOutStorageDao.confirmOutStor(outStorageDto);
			}

			StorageDto storageDto = new StorageDto();
			storageDto.setEnterpriseid(outStorageDto.getEnterpriseid());
			storageDto.setRepoNo(outStorageDto.getRepoNo());
			storageDto.setProductNo(outStorageDto.getProductNo());
			List<StorageVo> listStore = iStorageDao.getByProNoAndRepoNo(storageDto);
			Integer count = outStorageDto.getQuantity();
			for (StorageVo stor : listStore) {
				if (stor.getQuantity() <= count) {
					StorageDto changeStor = new StorageDto();
					BeanUtils.copyProperties(stor, changeStor);
					count = count - stor.getQuantity();
					changeStor.setQuantity(0);
					iStorageDao.editStorage(changeStor);
				} else {
					StorageDto changeStor = new StorageDto();
					BeanUtils.copyProperties(stor, changeStor);
					changeStor.setQuantity(stor.getQuantity() - count);
					iStorageDao.editStorage(changeStor);
					break;
				}
			}
			// 操作数据
			return iOutStorageDao.confirmOutStor(outStorageDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer getProCount(StorageBo storageBo) {
		try {
			// 参数转换
			StorageDto storageDto = new StorageDto();
			BeanUtils.copyProperties(storageBo, storageDto);
			// 操作数据
			return iOutStorageDao.getProCount(storageDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
