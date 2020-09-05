package com.dotop.pipe.api.client;

import com.dotop.pipe.core.vo.product.InventoryVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;
import java.util.Map;

/**
 * 对接水务平台产品
 *
 *
 */
public interface IWaterProductClient {

    /**
     * 获取产品列表
     */
    List<ProductVo> storeList(String userid, String ticket, String category, String type) throws FrameworkRuntimeException;

    /**
     * 获取产品列表
     */
    Map<String, ProductVo> storeMap(String userid, String ticket, String category, String type) throws FrameworkRuntimeException;

    /**
     * 获取产品详情
     */
    List<ProductVo> storeGetProductByNo() throws FrameworkRuntimeException;

    /**
     * 获取产品库存余量
     */
    List<ProductVo> storeGetInventory() throws FrameworkRuntimeException;

    /**
     * 获取产品列表
     */
    List<ProductVo> storeList(String productCategory) throws FrameworkRuntimeException;

    /**
     * 获取产品列表
     */
    Map<String, ProductVo> storeMap(String productCategory) throws FrameworkRuntimeException;


    /**
     * 获取产品
     */
    ProductVo store(String userid, String ticket, String productId, String productCode) throws FrameworkRuntimeException;

    /**
     * 获取产品
     */
    ProductVo store(String productId, String productCode, String productCategory) throws FrameworkRuntimeException;

    /**
     * 库存盘点
     */
    InventoryVo inventory(String userid, String ticket, String productId, String productCode) throws FrameworkRuntimeException;

    /**
     * 库存盘点
     */
    InventoryVo inventory(String productId, String productCode, String productCategory) throws FrameworkRuntimeException;
}
