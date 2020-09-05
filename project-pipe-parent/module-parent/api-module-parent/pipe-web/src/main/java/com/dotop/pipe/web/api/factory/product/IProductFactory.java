package com.dotop.pipe.web.api.factory.product;

import java.util.List;

import com.dotop.pipe.core.form.ProductForm;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 * @date 2018/10/25.
 *       <p>
 *       产品
 */
public interface IProductFactory extends BaseFactory<ProductForm, ProductVo> {
	/**
	 * 新增产品
	 *
	 * @param productForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	ProductVo add(ProductForm productForm) throws FrameworkRuntimeException;

	/**
	 * 获取产品
	 *
	 * @param productForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	ProductVo get(ProductForm productForm) throws FrameworkRuntimeException;

	/**
	 * 产品分页
	 *
	 * @param productForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<ProductVo> page(ProductForm productForm) throws FrameworkRuntimeException;

	/**
	 * 列出产品
	 * 
	 * @param productForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<ProductVo> list(ProductForm productForm) throws FrameworkRuntimeException;

	/**
	 * 编辑产品
	 *
	 * @param productForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	ProductVo edit(ProductForm productForm) throws FrameworkRuntimeException;

	/**
	 * 删除产品
	 *
	 * @param productForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	String del(ProductForm productForm) throws FrameworkRuntimeException;

}
