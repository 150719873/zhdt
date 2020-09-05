package com.dotop.pipe.web.api.factory.third;

import com.dotop.pipe.core.form.ProductForm;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IThirdProductFactory extends BaseFactory<ProductForm, ProductVo> {

	Pagination<ProductVo> page(ProductForm productForm) throws FrameworkRuntimeException;

}
