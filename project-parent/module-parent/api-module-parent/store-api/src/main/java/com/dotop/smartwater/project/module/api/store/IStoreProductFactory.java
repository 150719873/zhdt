package com.dotop.smartwater.project.module.api.store;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeProductVo;
import com.dotop.smartwater.project.module.core.water.form.StoreProductForm;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;

public interface IStoreProductFactory extends BaseFactory<StoreProductForm, StoreProductVo>  {
	/**
	 * 获取产品信息并分页
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<StoreProductVo> getProByCon(StoreProductForm storeProductForm);
	/**
	 * 根据产品编号获取产品信息
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	StoreProductVo getProductByNo(StoreProductForm storeProductForm);
	/**
	 * 新增产品
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer addProduct(StoreProductForm storeProductForm);
	/**
	 * 验证产品编号唯一性
	 * @param productNo
	 * @param enterpriseid
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer checkProductNo(StoreProductForm storeProductForm);
	/**
	 * 验证产品名称唯一性
	 * @param storeProductForm
	 * @return
	 */
	Integer checkProductName(StoreProductForm storeProductForm);
	/**
	 * 编辑产品信息
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer editProduct(StoreProductForm storeProductForm);
	/**
	 * 修改产品状态
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer changeStatus(StoreProductForm storeProductForm);
	/**
	 * 删除产品
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer deleteProduct(StoreProductForm storeProductForm);
	/**
	 * 获取已上线产品
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<StoreProductVo> getLinePro(StoreProductForm storeProductForm);
	/**
	 * 获取产品列表
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<StoreProductVo> list(StoreProductForm storeProductForm);
	/**
	 * 查询管漏产品信息
	 * @param storeProductForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<PipeProductVo> getPipeProduct(StoreProductForm storeProductForm);
}
