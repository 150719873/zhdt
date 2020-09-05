package com.dotop.smartwater.project.module.dao.store;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InventoryDto;
import com.dotop.smartwater.project.module.core.water.vo.InventoryVo;

/**
 * 仓库管理-库存盘点Mapper
 *

 * @date 2018-12-05 下午 19:38
 */
public interface IInventoryDao extends BaseDao<InventoryDto, InventoryVo> {

	List<InventoryVo> getInventoryByCon(InventoryDto inventoryDto);
	
	InventoryVo getInventory(InventoryDto inventoryDto);

	@MapKey("productNo")
	Map<String,InventoryVo> getOutCount(InventoryDto inventoryDto);
}
