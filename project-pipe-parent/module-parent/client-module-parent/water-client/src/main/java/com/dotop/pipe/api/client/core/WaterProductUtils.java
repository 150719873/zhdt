package com.dotop.pipe.api.client.core;

import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.factory.FactoryVo;
import com.dotop.pipe.core.vo.product.InventoryVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;

import java.util.List;
import java.util.Map;

/**
 *
 */
public final class WaterProductUtils {

    public static void build(Map<String, ProductVo> productMap, List<DeviceVo> devices, String productCategory) {
        // TODO !CommonConstants.PRODUCT_CATEGORY_REGION.equals(productCategory) &&
        if (!productMap.isEmpty()) {
            for (DeviceVo d : devices) {
                String productId = d.getProduct().getProductId();
                ProductVo p = productMap.get(productId);
                if (p != null) {
                    d.setProduct(p);
                }
            }
        }
    }

    public static void build(Map<String, ProductVo> productMap, DeviceVo device, String productCategory) {
        // TODO
//        if (!CommonConstants.PRODUCT_CATEGORY_REGION.equals(productCategory) && !productMap.isEmpty()) {
        if (!productMap.isEmpty()) {
            String productId = device.getProduct().getProductId();
            ProductVo p = productMap.get(productId);
            if (p != null) {
                device.setProduct(p);
            }
        }
    }

    public static void build(ProductVo product, DeviceVo device, String productCategory) {
        // TODO
//        if (!CommonConstants.PRODUCT_CATEGORY_REGION.equals(productCategory)) {
        if (product != null) {
            device.setProduct(product);
        }
//        }
    }

    public static ProductVo build(StoreProductVo sp) {
        ProductVo p = new ProductVo();
        p.setProductId(sp.getProductId());
        p.setCode(sp.getProductNo());
        p.setName(sp.getName());
        p.setDes(sp.getIntro());
        // 类别
        DictionaryVo category = new DictionaryVo();
        category.setId(sp.getCategory());
        category.setName(sp.getCategoryName());
        category.setVal(sp.getCategoryValue());
        p.setCategory(category);
        // 类型
        DictionaryVo type = new DictionaryVo();
        type.setId(sp.getType());
        type.setName(sp.getTypeName());
        type.setVal(sp.getTypeValue());
        p.setType(type);
        // 厂家
        FactoryVo factory = new FactoryVo();
        factory.setName(sp.getVender());
        p.setFactory(factory);
        // 口径
        DictionaryVo caliber = new DictionaryVo();
        caliber.setName(String.valueOf(sp.getCaliber()));
        caliber.setVal(String.valueOf(sp.getCaliber()));
        p.setCaliber(caliber);
        // 材质
        DictionaryVo material = new DictionaryVo();
        material.setId(sp.getMaterial());
        material.setName(sp.getMaterialName());
        material.setVal(sp.getMaterialValue());
        p.setMaterial(material);
        return p;
    }

    public static InventoryVo build(com.dotop.smartwater.project.module.core.water.vo.InventoryVo in) {
        InventoryVo inventory = new InventoryVo();
        inventory.setInTotal(in.getInTotal());
        inventory.setOutTotal(in.getOutTotal());
        inventory.setStockTotal(in.getStockTotal());
        return inventory;
    }

    public static void build(InventoryVo inventory, ProductVo product, String productCategory) {
        // TODO
        if (!CommonConstants.PRODUCT_CATEGORY_REGION.equals(productCategory)) {
            if (product != null && inventory != null) {
                product.setInventory(inventory);
            }
        }
    }
}
