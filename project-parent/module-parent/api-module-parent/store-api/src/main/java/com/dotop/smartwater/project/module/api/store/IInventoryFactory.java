package com.dotop.smartwater.project.module.api.store;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.InventoryForm;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcellException;
import com.dotop.smartwater.project.module.core.water.vo.InventoryVo;

import java.io.IOException;

public interface IInventoryFactory extends BaseFactory<InventoryForm, InventoryVo> {
	/**
	 * 获取库存盘点并分页
	 * @param inventoryForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<InventoryVo> getInventoryByCon(InventoryForm inventoryForm);
	/**
	 * 获取库存数量
	 * @param inventoryForm
	 * @return
	 */
	InventoryVo getInventory(InventoryForm inventoryForm);
	/**
	 * 导出库存盘点信息
	 * @param tempFileName
	 * @param inventoryForm
	 * @throws ExcellException
	 * @throws IOException
	 */
	void exportInventory(String tempFileName, InventoryForm inventoryForm) throws ExcellException, IOException;
}
