package com.dotop.smartwater.project.module.service.store;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.OutStorageBo;
import com.dotop.smartwater.project.module.core.water.bo.StorageBo;
import com.dotop.smartwater.project.module.core.water.vo.OutStorageVo;

/* 
 * 仓库管理-出库管理Service

 * @date 2018-11-30 下午 15:46
 *
 */

public interface IOutStorageService extends BaseService<OutStorageBo, OutStorageVo> {

	/**
	 * 获取出库信息并分页
	 * 
	 * @param outStor
	 * @return
	 */
	Pagination<OutStorageVo> getOutStorByCon(OutStorageBo outStorageBo);

	/**
	 * 根据记录编号获取单条入库信息
	 * 
	 * @param outStorageBo
	 * @return @
	 */
	OutStorageVo getOutStorByNo(OutStorageBo outStorageBo);

	/**
	 * 新增出库
	 * 
	 * @param outStorageBo
	 * @return @
	 */
	Integer addOutStor(OutStorageBo outStorageBo);

	/**
	 * 编辑出库信息
	 * 
	 * @param outStorageBo
	 * @return @
	 */
	Integer editOutStor(OutStorageBo outStorageBo);

	/**
	 * 删除出库信息
	 * 
	 * @param outStorageBo
	 * @return @
	 */
	Integer deleteOutStor(OutStorageBo outStorageBo);

	/**
	 * 确认出库
	 * 
	 * @param outStorageBo
	 * @return @
	 */
	Integer confirmOutStor(OutStorageBo outStorageBo);

	/**
	 * 获取库存数量
	 * 
	 * @param storageBo
	 * @return @
	 */
	Integer getProCount(StorageBo storageBo);
}
