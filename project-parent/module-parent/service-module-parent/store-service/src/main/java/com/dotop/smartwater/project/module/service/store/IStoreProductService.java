package com.dotop.smartwater.project.module.service.store;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.StoreProductBo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;

/*
 * 仓库管理-产品管理

 * @date 2018-11-21 上午 11:00
 * 
 */

public interface IStoreProductService extends BaseService<StoreProductBo, StoreProductVo>  {
	/**
	 * 获取产品信息并分页
	 * @param storeProductBo
	 * @return
	 * @
	 */
	Pagination<StoreProductVo> getProByCon(StoreProductBo storeProductBo) ;
	/**
	 * 根据产品编号获取产品信息
	 * @param storeProductBo
	 * @return
	 * @
	 */
	StoreProductVo getProductByNo(StoreProductBo storeProductBo) ;
	/**
	 * 新增产品
	 * @param storeProductBo
	 * @return
	 * @
	 */
	Integer addProduct(StoreProductBo storeProductBo) ;
	/**
	 * 编辑产品
	 * @param storeProductBo
	 * @return
	 * @
	 */
	Integer editProduct(StoreProductBo storeProductBo) ;
	/**
	 * 修改产品状态
	 * @param storeProductBo
	 * @return
	 * @
	 */
	Integer changeStatus(StoreProductBo storeProductBo) ;
	/**
	 * 检验产品编号唯一性
	 * @param storeProductBo
	 * @return
	 * @
	 */
	Integer checkProductNo(StoreProductBo storeProductBo) ;
	/**
	 * 检验产品名称唯一性
	 * @param storeProductBo
	 * @return
	 */
	Integer checkProductName(StoreProductBo storeProductBo) ;
	/**
	 * 删除产品
	 * @param storeProductBo
	 * @return
	 * @
	 */
	Integer deleteProduct(StoreProductBo storeProductBo) ;
	/**
	 *获取已上线产品
	 * @param storeProductBo
	 * @return
	 * @
	 */
	List<StoreProductVo> getLinePro(StoreProductBo storeProductBo) ;
	/**
	 *获取产品列表
	 * @param storeProductBo
	 * @return
	 * @
	 */
	List<StoreProductVo> list(StoreProductBo storeProductBo) ;
}
