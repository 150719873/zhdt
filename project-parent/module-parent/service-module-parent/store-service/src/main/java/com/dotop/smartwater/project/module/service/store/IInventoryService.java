package com.dotop.smartwater.project.module.service.store;
/* 
 * 仓库管理-库存盘点Service

 * @date 2018-12-05 下午 19:24
 *
 */

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.InventoryBo;
import com.dotop.smartwater.project.module.core.water.vo.InventoryVo;

public interface IInventoryService extends BaseService<InventoryBo, InventoryVo> {
	/**
	 * 获取库存盘点信息并分页
	 * @param inventoryBo
	 * @return
	 * @
	 */
	Pagination<InventoryVo> getInventoryByCon(InventoryBo inventoryBo) ;
	
	/**
	 * 获取某产品库存余量
	 * @param inventoryBo
	 * @return
	 */
	InventoryVo getInventory(InventoryBo inventoryBo);
}
