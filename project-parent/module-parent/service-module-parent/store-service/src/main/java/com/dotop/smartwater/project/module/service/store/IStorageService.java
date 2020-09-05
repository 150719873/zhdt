package com.dotop.smartwater.project.module.service.store;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.StorageBo;
import com.dotop.smartwater.project.module.core.water.vo.StorageVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;

/* 
 * 仓库管理-入库管理Service

 * @date 2018-11-27 下午 14:05
 *
 */

public interface IStorageService extends BaseService<StorageBo, StorageVo> {
	/**
	 * 获取入库信息并分页
	 * 
	 * @param storageBo
	 * @return @
	 */
	Pagination<StorageVo> getStorageByCon(StorageBo storageBo);

	/**
	 * 根据记录编号获取入库信息
	 * 
	 * @param storageBo
	 * @return @
	 */
	StorageVo getStorageByNo(StorageBo storageBo);

	/**
	 * 新增入库
	 * 
	 * @param storageBo
	 * @return @
	 */
	Integer addStorage(StorageBo storageBo);

	/**
	 * 编辑入库信息
	 * 
	 * @param storageBo
	 * @return @
	 */
	Integer editStorage(StorageBo storageBo);

	/**
	 * 删除入库记录
	 * 
	 * @param storageBo
	 * @return @
	 */
	Integer deleteStorage(StorageBo storageBo);

	/**
	 * 确认入库
	 * 
	 * @param storageBo
	 * @return @
	 */
	Integer confirmStorage(StorageBo storageBo);

	/**
	 * 获取已入库产品信息
	 * 
	 * @param storageBo
	 * @return @
	 */
	List<StoreProductVo> getProIn(StorageBo storageBo);

}
