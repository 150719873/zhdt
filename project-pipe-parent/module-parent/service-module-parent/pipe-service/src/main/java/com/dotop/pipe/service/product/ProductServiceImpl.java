package com.dotop.pipe.service.product;

import com.dotop.pipe.api.client.IWaterProductClient;
import com.dotop.pipe.api.dao.product.IProductDao;
import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.dto.product.ProductDto;
import com.dotop.pipe.core.vo.product.InventoryVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @date 2018/10/25.
 */
@Service
public class ProductServiceImpl implements IProductService {
    private final static Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    @Autowired
    private IWaterProductClient iWaterProductClient;

    @Autowired
    private IProductDao iProductDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ProductVo add(ProductBo productBo) throws FrameworkRuntimeException {
        try {
            String productId = UuidUtils.getUuid();
            ProductDto productDto = BeanUtils.copy(productBo, ProductDto.class);
            productDto.setProductId(productId);
            productDto.setIsDel(RootModel.NOT_DEL);
            iProductDao.add(productDto);
            // 返回
            ProductVo productVo = new ProductVo();
            productVo.setProductId(productId);
            return productVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ProductVo get(ProductBo productBo) throws FrameworkRuntimeException {
        try {
            String productId = productBo.getProductId();
            String category = productBo.getCategory();
            if (CommonConstants.isWaterProduct(category)) {
                return iWaterProductClient.store(productId, null, category);
            } else {
                // ProductVo result = null;
                ProductDto productDto = new ProductDto();
                productDto.setProductId(productId);
                productDto.setCategory(category);
                productDto.setEnterpriseId(productBo.getEnterpriseId());
                productDto.setIsDel(RootModel.NOT_DEL);
                ProductVo result = iProductDao.get(productDto);
                /*if (!CollectionUtils.isEmpty(list) && list.size() == 1) {
                    result = list.get(0);
                }*/
                return result;
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ProductVo getByCode(ProductBo productBo)
            throws FrameworkRuntimeException {
        try {
            String code = productBo.getCode();
            String category = productBo.getCategory();
            if (CommonConstants.isWaterProduct(category)) {
                // TODO
                return iWaterProductClient.store(null, code, null);
            } else {
                ProductVo result = null;
                ProductDto productDto = new ProductDto();
                productDto.setCategory(category);
                productDto.setCode(code);
                productDto.setEnterpriseId(productBo.getEnterpriseId());
                productDto.setIsDel(RootModel.NOT_DEL);
                List<ProductVo> list = iProductDao.list(productDto);
                if (!CollectionUtils.isEmpty(list)) {
                    if (list.size() == 1) {
                        result = list.get(0);
                    } else {
                        logger.error(LogMsg.to("ex", BaseExceptionConstants.DUPLICATE_KEY_ERROR, "code", productBo.getCode()));
                        throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR);
                    }
                }
                return result;
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<ProductVo> page(ProductBo productBo) throws FrameworkRuntimeException {
        try {
            ProductDto productDto = BeanUtils.copy(productBo, ProductDto.class);
            productDto.setCode_(productDto.getCode());
            productDto.setName_(productDto.getName());
            productDto.setCode(null);
            productDto.setName(null);
            productDto.setIsDel(RootModel.NOT_DEL);
            Page<ProductDto> pageHelper = PageHelper.startPage(productBo.getPage(), productBo.getPageSize());
            List<ProductVo> list = iProductDao.list(productDto);
            return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<ProductVo> list(ProductBo productBo) throws FrameworkRuntimeException {
        try {
            String category = productBo.getCategory();
            if (CommonConstants.isWaterProduct(category)) {
                return iWaterProductClient.storeList(category);
            } else {
                ProductDto productDto = new ProductDto();
                productDto.setCategory(category);
                productDto.setEnterpriseId(productBo.getEnterpriseId());
                productDto.setIsDel(RootModel.NOT_DEL);
                return iProductDao.list(productDto);
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ProductVo edit(ProductBo productBo) throws FrameworkRuntimeException {
        try {
            ProductDto productDto = BeanUtils.copy(productBo, ProductDto.class);
            productDto.setIsDel(RootModel.NOT_DEL);
            iProductDao.edit(productDto);
            return null;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(ProductBo productBo) throws FrameworkRuntimeException {
        try {
            ProductDto productDto = BeanUtils.copy(productBo, ProductDto.class);
            productDto.setIsDel(RootModel.NOT_DEL);
            productDto.setNewIsDel(RootModel.DEL);
            iProductDao.del(productDto);
            return null;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, ProductVo> mapAll(String enterpriseId) throws FrameworkRuntimeException {
        try {
            // 水务产品
            List<ProductVo> waterProducts = iWaterProductClient.storeList(null);
            Map<String, ProductVo> waterProductMap = waterProducts.stream().collect(Collectors.toMap(ProductVo::getCode, a -> a, (k1, k2) -> k1));
            // 管漏设施
            List<ProductVo> products = list(new ProductBo(null, null, null, enterpriseId));
            Map<String, ProductVo> productMap = products.stream().collect(Collectors.toMap(ProductVo::getCode, a -> a, (k1, k2) -> k1));
            waterProductMap.putAll(productMap);
            return waterProductMap;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, ProductVo> map(ProductBo productBo) throws FrameworkRuntimeException {
        try {
            String category = productBo.getCategory();
            if (StringUtils.isBlank(category)) {
                Map<String, ProductVo> map1 = iWaterProductClient.storeMap(category);
                List<ProductVo> products = list(productBo);
                Map<String, ProductVo> map2 = products.stream().collect(Collectors.toMap(ProductVo::getProductId, a -> a, (k1, k2) -> k1));
                map1.putAll(map2);
                return map1;
            } else {
                if (CommonConstants.isWaterProduct(category)) {
                    return iWaterProductClient.storeMap(category);
                } else {
                    List<ProductVo> products = list(productBo);
                    return products.stream().collect(Collectors.toMap(ProductVo::getProductId, a -> a, (k1, k2) -> k1));
                }
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public InventoryVo inventory(String productId, String productCode, String productCategory) throws FrameworkRuntimeException {
        return iWaterProductClient.inventory(productId, productCode, productCategory);
    }
}
