package com.dotop.smartwater.project.module.service.store.impl;

import java.util.List;
import java.util.Map;

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
import com.dotop.smartwater.project.module.core.water.bo.InventoryBo;
import com.dotop.smartwater.project.module.core.water.dto.InventoryDto;
import com.dotop.smartwater.project.module.core.water.dto.StoreProductDto;
import com.dotop.smartwater.project.module.core.water.vo.InventoryVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.dao.store.IInventoryDao;
import com.dotop.smartwater.project.module.dao.store.IStoreProductDao;
import com.dotop.smartwater.project.module.service.store.IInventoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 
 * 仓库管理-库存盘点ServiceImpl
 * 

 * @date 2018-12-05 下午 19:27
 *
 */

@Service
public class InventoryServiceImpl implements IInventoryService {
	private static final Logger LOGGER = LogManager.getLogger(InventoryServiceImpl.class);

	@Autowired
	private IInventoryDao iInventoryDao;
	
	@Autowired
	private IStoreProductDao iStoreProductDao;

	@Override
	public Pagination<InventoryVo> getInventoryByCon(InventoryBo inventoryBo) {
		try {
			// 参数转换
			InventoryDto inventoryDto = new InventoryDto();
			BeanUtils.copyProperties(inventoryBo, inventoryDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(inventoryBo.getPage(), inventoryBo.getPageCount());
			List<InventoryVo> inList = iInventoryDao.getInventoryByCon(inventoryDto);
			Long total = pageHelper.getTotal();
			if (inList.isEmpty()) {
				return new Pagination<>(inventoryBo.getPage(), inventoryBo.getPageCount(), inList, total);
			}
			// 每个产品的出库数量
			Map<String, InventoryVo> outTotalMap = iInventoryDao.getOutCount(inventoryDto);
			if (outTotalMap.isEmpty()) {
				for (InventoryVo item : inList) {
					item.setOutTotal(0);
				}
				return new Pagination<>(inventoryBo.getPage(), inventoryBo.getPageCount(), inList, total);
			}

			for (InventoryVo item : inList) {
				if (outTotalMap.containsKey(item.getProduct().getProductNo())) {
					item.setOutTotal(outTotalMap.get(item.getProduct().getProductNo()).getOutTotal());
				} else {
					item.setOutTotal(0);
				}
			}
			// 拼接数据返回
			return new Pagination<>(inventoryBo.getPage(), inventoryBo.getPageCount(), inList, total);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
		
	}
	
	@Override
	public InventoryVo getInventory(InventoryBo inventoryBo) {
		try {
			// 参数转换
			InventoryDto inventoryDto = new InventoryDto();
			BeanUtils.copyProperties(inventoryBo, inventoryDto);
			// 拼接数据返回
			InventoryVo inventoryVo = iInventoryDao.getInventory(inventoryDto);
			StoreProductDto productDto = new StoreProductDto();
			productDto.setProductId(inventoryBo.getProductId());
			productDto.setProductNo(inventoryBo.getProductNo());
			StoreProductVo productVo = iStoreProductDao.getProductByNo(productDto);
			
			if(inventoryVo == null) {
				inventoryVo = new InventoryVo();
				inventoryVo.setInTotal(0);
				inventoryVo.setOutTotal(0);
				inventoryVo.setStockTotal(0);
			}
			inventoryVo.setProduct(productVo);
			
			return inventoryVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
