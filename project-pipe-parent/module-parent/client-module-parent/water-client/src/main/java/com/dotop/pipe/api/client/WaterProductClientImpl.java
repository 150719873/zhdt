package com.dotop.pipe.api.client;

import com.alibaba.fastjson.TypeReference;
import com.dotop.pipe.api.client.core.PlsDictionaryCode;
import com.dotop.pipe.api.client.core.WaterProductUtils;
import com.dotop.pipe.api.client.fegin.water.IWaterFeginClient;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.vo.product.InventoryVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.InventoryForm;
import com.dotop.smartwater.project.module.core.water.form.StoreProductForm;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WaterProductClientImpl implements IWaterProductClient {

    private final static Logger logger = LogManager.getLogger(WaterProductClientImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IWaterFeginClient iWaterFeginClient;

    @Override
    public List<ProductVo> storeList(String userid, String ticket, String category, String type) throws FrameworkRuntimeException {
        List<ProductVo> products = new ArrayList<>();
        // 调用接口
        StoreProductForm storeProductForm = new StoreProductForm();
        storeProductForm.setCategory(PlsDictionaryCode.getDictionaryId(category));
        String result = iWaterFeginClient.storeList(storeProductForm, userid, ticket);
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
        if (!ResultCode.Success.equals(code)) {
            return products;
        }
        List<StoreProductVo> storeProducts = jsonObjects.getObject("data", new TypeReference<List<StoreProductVo>>() {
        });
        logger.info(LogMsg.to("storeProducts", storeProducts));
        // 水务管漏产品参数对接转换
        for (StoreProductVo sp : storeProducts) {
            if (sp != null) {
                // 组装数据
                ProductVo p = WaterProductUtils.build(sp);
                products.add(p);
            }
        }
        return products;
    }

    @Override
    public Map<String, ProductVo> storeMap(String userid, String ticket, String category, String type) throws FrameworkRuntimeException {
        List<ProductVo> products = storeList(userid, ticket, category, type);
        Map<String, ProductVo> productMap = products.stream().collect(Collectors.toMap(ProductVo::getProductId, a -> a, (k1, k2) -> k1));
        return productMap;
    }

    @Override
    public List<ProductVo> storeGetProductByNo() throws FrameworkRuntimeException {
        return null;
    }

    @Override
    public List<ProductVo> storeGetInventory() throws FrameworkRuntimeException {
        return null;
    }

    @Override
    public List<ProductVo> storeList(String productCategory) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get(false);
        if (loginCas != null) {
            return storeList(loginCas.getUserId(), loginCas.getTicket(), productCategory, null);
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, ProductVo> storeMap(String productCategory) throws FrameworkRuntimeException {
        Map<String, ProductVo> productVoMap = new HashMap<>(1);
        if (StringUtils.isNotBlank(productCategory) && !CommonConstants.PRODUCT_CATEGORY_REGION.equals(productCategory)) {
            LoginCas loginCas = iAuthCasApi.get(false);
            if (loginCas != null) {
                productVoMap = storeMap(loginCas.getUserId(), loginCas.getTicket(), productCategory, null);
            }
        } else if (StringUtils.isNotBlank(productCategory) && CommonConstants.PRODUCT_CATEGORY_REGION.equals(productCategory)) {
            productVoMap.put("region_product_id", null);
        } else if (StringUtils.isBlank(productCategory)) {
            LoginCas loginCas = iAuthCasApi.get(false);
            if (loginCas != null) {
                productVoMap = storeMap(loginCas.getUserId(), loginCas.getTicket(), productCategory, null);
                productVoMap.put("region_product_id", null);
            }
        }
        return productVoMap;
    }

    @Override
    public ProductVo store(String userid, String ticket, String productId, String productCode) throws FrameworkRuntimeException {
        if (StringUtils.isBlank(productId) && StringUtils.isBlank(productCode)) {
            return null;
        }
        // 调用接口
        StoreProductForm storeProductForm = new StoreProductForm();
        storeProductForm.setProductId(productId);
        storeProductForm.setProductNo(productCode);
        String result = iWaterFeginClient.store(storeProductForm, userid, ticket);
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
        if (ResultCode.Success.equals(code)) {
            StoreProductVo storeProduct = jsonObjects.getObject("data", StoreProductVo.class);
            if (storeProduct != null) {
                return WaterProductUtils.build(storeProduct);
            }
        }
        return null;
    }

    @Override
    public ProductVo store(String productId, String productCode, String productCategory) throws FrameworkRuntimeException {
        if (!CommonConstants.PRODUCT_CATEGORY_REGION.equals(productCategory)) {
            LoginCas loginCas = iAuthCasApi.get(false);
            if (loginCas != null) {
                return store(loginCas.getUserId(), loginCas.getTicket(), productId, productCode);
            }
        }
        return null;
    }

    @Override
    public InventoryVo inventory(String userid, String ticket, String productId, String productCode) throws FrameworkRuntimeException {
        if (StringUtils.isBlank(productId) && StringUtils.isBlank(productCode)) {
            return null;
        }
        // 调用接口
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setProductId(productId);
        inventoryForm.setProductNo(productCode);
        String result = iWaterFeginClient.inventory(inventoryForm, userid, ticket);
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
        if (ResultCode.Success.equals(code)) {
            com.dotop.smartwater.project.module.core.water.vo.InventoryVo inventory = jsonObjects.getObject("data", com.dotop.smartwater.project.module.core.water.vo.InventoryVo.class);
            if (inventory != null) {
                return WaterProductUtils.build(inventory);
            }
        }
        return null;
    }

    @Override
    public InventoryVo inventory(String productId, String productCode, String productCategory) throws FrameworkRuntimeException {
        if (!CommonConstants.PRODUCT_CATEGORY_REGION.equals(productCategory)) {
            LoginCas loginCas = iAuthCasApi.get(false);
            if (loginCas != null) {
                return inventory(loginCas.getUserId(), loginCas.getTicket(), productId, productCode);
            }
        }
        return null;
    }
}
