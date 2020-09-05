package com.dotop.pipe.web.factory.product;

import com.dotop.pipe.web.api.factory.product.IProductFactory;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.auth.cas.web.IAuthCasClient;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.form.ProductForm;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @date 2018/10/25.
 */
@Component
public class ProductFactoryImpl implements IProductFactory, IAuthCasClient {

    private final static Logger logger = LogManager.getLogger(ProductFactoryImpl.class);

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ProductVo add(ProductForm productForm) throws FrameworkRuntimeException {
        ProductBo productBo = BeanUtils.copy(productForm, ProductBo.class);
        productBo.setCode(productForm.getCode());
        productBo.setEnterpriseId(getEnterpriseId());
        productBo.setUserBy(getUserBy());
        productBo.setCurr(getCurr());
        productBo.setCategory(null);
        // 检验code是否唯一
        ProductVo productVo = iProductService.getByCode(productBo);
        logger.debug(LogMsg.to("productVo", productVo));
        if (productVo != null) {
            logger.error(LogMsg.to("ex", BaseExceptionConstants.DUPLICATE_KEY_ERROR, "productBo", productBo));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "编号已存在");
        }
        productBo.setCategory(productForm.getCategory());
        return iProductService.add(productBo);
    }

    @Override
    public ProductVo get(ProductForm productForm) throws FrameworkRuntimeException {
        // 所需参数
        ProductBo productBo = new ProductBo();
        productBo.setProductId(productBo.getProductId());
        productBo.setCategory(productForm.getCategory());
        productBo.setEnterpriseId(getEnterpriseId());
        return iProductService.get(productBo);
    }

    @Override
    public Pagination<ProductVo> page(ProductForm productForm) throws FrameworkRuntimeException {
        ProductBo productBo = BeanUtils.copy(productForm, ProductBo.class);
        productBo.setEnterpriseId(getEnterpriseId());
        Pagination<ProductVo> pagination = iProductService.page(productBo);
        // 产品
        List<ProductVo> products = pagination.getData();
        if (!products.isEmpty()) {
            Map<String, ProductVo> productMap = products.stream().collect(Collectors.toMap(ProductVo::getProductId, a -> a, (k1, k2) -> k1));
            // 一对一设备
            DeviceBo deviceBo = new DeviceBo();
            deviceBo.setProductIds(new ArrayList<>(productMap.keySet()));
            List<DeviceVo> devices = iDeviceService.list(deviceBo);
            Map<String, DeviceVo> deviceMap = devices.stream().collect(Collectors.toMap(a -> a.getProduct().getProductId(), a -> a, (k1, k2) -> k2));
            // 组装
            for (String productId : productMap.keySet()) {
                ProductVo product = productMap.get(productId);
                DeviceVo device = deviceMap.get(productId);
                product.setDevice(device);
            }
        }
        return pagination;
    }

    @Override
    public List<ProductVo> list(ProductForm productForm) throws FrameworkRuntimeException {
        ProductBo productBo = BeanUtils.copy(productForm, ProductBo.class);
        productBo.setCategory(productForm.getCategory());
        productBo.setEnterpriseId(getEnterpriseId());
        List<ProductVo> list = iProductService.list(productBo);
        // TODO 常量
        if ("selected".equals(productForm.getSelectType())) {
            list = list.stream().filter(t -> t.getDevice() == null).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ProductVo edit(ProductForm productForm) throws FrameworkRuntimeException {
        ProductBo productBo = BeanUtils.copy(productForm, ProductBo.class);
        productBo.setEnterpriseId(getEnterpriseId());
        productBo.setUserBy(getUserBy());
        productBo.setCurr(getCurr());
        iProductService.edit(productBo);
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(ProductForm productForm) throws FrameworkRuntimeException {
        // 判断是否已经地图设备绑定设施产品
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setProductId(productForm.getProductId());
        deviceBo.setEnterpriseId(getEnterpriseId());
        Boolean otherFlag = iDeviceService.isExist(deviceBo);
        if (otherFlag) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_PRODUCT_EXIST, "msg",
                    PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_PRODUCT_EXIST)));
            throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_PRODUCT_EXIST,
                    PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_PRODUCT_EXIST));
        }
        // 删除设施产品
        ProductBo productBo = BeanUtils.copy(productForm, ProductBo.class);
        productBo.setEnterpriseId(getEnterpriseId());
        productBo.setUserBy(getUserBy());
        productBo.setCurr(getCurr());
        iProductService.del(productBo);
        return null;
    }
}
