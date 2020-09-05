package com.dotop.pipe.api.service.product;

import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.vo.product.InventoryVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface IProductService extends BaseService<ProductBo, ProductVo> {

    /**
     * 添加产品
     */
    @Override
    ProductVo add(ProductBo productBo) throws FrameworkRuntimeException;

    /**
     * 获取产品
     */
    @Override
    ProductVo get(ProductBo productBo) throws FrameworkRuntimeException;

    /**
     * 获取唯一产品
     */
    @Override
    ProductVo getByCode(ProductBo productBo) throws FrameworkRuntimeException;

    /**
     * 产品分页
     */
    @Override
    Pagination<ProductVo> page(ProductBo productBo) throws FrameworkRuntimeException;

    /**
     * 列出产品
     */
    @Override
    List<ProductVo> list(ProductBo productBo) throws FrameworkRuntimeException;

    /**
     * 编辑产品
     */
    @Override
    ProductVo edit(ProductBo productBo) throws FrameworkRuntimeException;

    /**
     * 删除产品
     */
    @Override
    String del(ProductBo productBo) throws FrameworkRuntimeException;

    /**
     * 查询所有产品 code val
     */
    Map<String, ProductVo> mapAll(String enterpriseId) throws FrameworkRuntimeException;


    /**
     * 查询产品 productId val
     */
    Map<String, ProductVo> map(ProductBo productBo) throws FrameworkRuntimeException;

    /**
     * 库存盘点
     */
    InventoryVo inventory(String productId, String productCode, String productCategory) throws FrameworkRuntimeException;

}
