package com.dotop.smartwater.project.module.dao.store;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.StorageDto;
import com.dotop.smartwater.project.module.core.water.vo.StorageVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;

import java.util.List;

public interface IStorageDao extends BaseDao<StorageDto, StorageVo> {

	List<StorageVo> getStorageByCon(StorageDto storageDto);

	StorageVo getStorageByNo(StorageDto storageDto);

	Integer addStorage(StorageDto storageDto);

	Integer editStorage(StorageDto storageDto);

	Integer deleteStorage(StorageDto storageDto);

	Integer confirmStorage(StorageDto storageDto);

	List<StoreProductVo> getProIn(StorageDto storageDto);

	List<StorageVo> getByProNoAndRepoNo(StorageDto storageDto);
}
