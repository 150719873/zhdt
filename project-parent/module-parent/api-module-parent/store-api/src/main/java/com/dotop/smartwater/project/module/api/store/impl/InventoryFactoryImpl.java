package com.dotop.smartwater.project.module.api.store.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.store.IInventoryFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.InventoryBo;
import com.dotop.smartwater.project.module.core.water.form.InventoryForm;
import com.dotop.smartwater.project.module.core.water.utils.file.CellStyleFactory;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcelCreator;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcellException;
import com.dotop.smartwater.project.module.core.water.vo.InventoryVo;
import com.dotop.smartwater.project.module.service.store.IInventoryService;

import jxl.write.WritableCellFormat;

@Component
public class InventoryFactoryImpl implements IInventoryFactory {

	@Autowired
	private IInventoryService iInventoryService;

	@Override
	public Pagination<InventoryVo> getInventoryByCon(InventoryForm inventoryForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		InventoryBo inventoryBo = new InventoryBo();
		BeanUtils.copyProperties(inventoryForm, inventoryBo);
		inventoryBo.setEnterpriseid(user.getEnterpriseid());
		inventoryBo.setUserBy(userBy);
		inventoryBo.setCurr(curr);
		// 数据处理
		Pagination<InventoryVo> pagination = iInventoryService.getInventoryByCon(inventoryBo);
		return pagination;
	}
	
	@Override
	public InventoryVo getInventory(InventoryForm inventoryForm) {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		// 业务逻辑
		InventoryBo inventoryBo = new InventoryBo();
		BeanUtils.copyProperties(inventoryForm, inventoryBo);
		inventoryBo.setEnterpriseid(user.getEnterpriseid());
		
		return iInventoryService.getInventory(inventoryBo);
	}
	
	@Override
	public void exportInventory(String tempFileName, InventoryForm inventoryForm) throws ExcellException, IOException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		int line = 0;
		InventoryBo inventoryBo = new InventoryBo();
		BeanUtils.copyProperties(inventoryForm, inventoryBo);
		inventoryBo.setPage(1);
		inventoryBo.setPageCount(2000);
		inventoryBo.setEnterpriseid(user.getEnterpriseid());
		
		Pagination<InventoryVo> pagination = iInventoryService.getInventoryByCon(inventoryBo);
		List<InventoryVo> list = pagination.getData();
				
		ExcelCreator creator = new ExcelCreator(tempFileName);
		creator.CreateSheet("库存盘点数据查询");
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();
		
		creator.writeExcel("产品编号", 0, line,wcf);
		creator.writeExcel("产品名称", 1, line,wcf);
		creator.writeExcel("已入库总量", 2, line,wcf);
		creator.writeExcel("已出库总量", 3, line,wcf);
		creator.writeExcel("库存总量", 4, line,wcf);
		creator.writeExcel("单 位", 5, line,wcf);
		creator.writeExcel("型 号", 6, line,wcf);
		creator.writeExcel("规 格", 7, line,wcf);
		creator.writeExcel("材 质", 8, line,wcf);
		creator.writeExcel("厂 家", 9, line,wcf);
		creator.writeExcel("源产地", 10, line,wcf);
		creator.writeExcel("联系人", 11, line,wcf);
		creator.writeExcel("联系方式", 12, line,wcf);
				
		creator.getSheet().setColumnView(0, 20);
		creator.getSheet().setColumnView(1, 20);
		creator.getSheet().setColumnView(2, 15);
		creator.getSheet().setColumnView(3, 15);
		creator.getSheet().setColumnView(4, 10);
		creator.getSheet().setColumnView(5, 10);
		creator.getSheet().setColumnView(6, 20);
		creator.getSheet().setColumnView(7, 20);
		creator.getSheet().setColumnView(8, 20);
		creator.getSheet().setColumnView(9, 15);
		creator.getSheet().setColumnView(10, 15);
		creator.getSheet().setColumnView(11, 15);
		creator.getSheet().setColumnView(12, 20);
		
		if(list != null && list.size() > 0) {
			for(InventoryVo inv: list) {
				line++;
				creator.writeExcel(inv.getProductNo(), 0, line,wcfItem);
				creator.writeExcel(inv.getName(), 1, line,wcfItem);
				creator.writeExcel(inv.getInTotal(), 2, line,wcfItem);
				creator.writeExcel(inv.getOutTotal() , 3, line,wcfItem);				
				creator.writeExcel(inv.getStockTotal(), 4, line,wcfItem);				
				creator.writeExcel(inv.getProduct().getUnit(), 5, line,wcfItem);				
				creator.writeExcel(inv.getProduct().getModel(), 6, line,wcfItem);
				creator.writeExcel(inv.getProduct().getSpec(), 7, line,wcfItem);
				creator.writeExcel(inv.getProduct().getMaterial(), 8, line,wcfItem);
				creator.writeExcel(inv.getProduct().getVender(), 9, line,wcfItem);
				creator.writeExcel(inv.getProduct().getProduced(), 10, line,wcfItem);
				creator.writeExcel(inv.getProduct().getContacts(), 11, line,wcfItem);
				creator.writeExcel(inv.getProduct().getPhone(), 12, line,wcfItem);			
			}
		}
		creator.close();
	}

}
