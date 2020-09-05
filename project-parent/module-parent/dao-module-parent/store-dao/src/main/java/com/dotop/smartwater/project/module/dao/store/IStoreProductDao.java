package com.dotop.smartwater.project.module.dao.store;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.StoreProductDto;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
/*
 * 仓库管理-产品管理Mapper

 * @date 2018-11-21 上午 11:10
 * 
 */

public interface IStoreProductDao extends BaseDao<StoreProductDto, StoreProductVo> {

	List<StoreProductVo> getProByCon(StoreProductDto storeProductDto);

	StoreProductVo getProductByNo(StoreProductDto storeProductDto);

	Integer addProduct(StoreProductDto storeProductDto);

	Integer editProduct(StoreProductDto storeProductDto);

	Integer changeStatus(StoreProductDto storeProductDto);

	Integer checkProductNo(StoreProductDto storeProductDto);
	
	Integer checkProductName(StoreProductDto storeProductDto);

	Integer deleteProduct(StoreProductDto storeProductDto);
	
	Integer batchAddProduct(@Param("list") List<StoreProductVo> list);
}
