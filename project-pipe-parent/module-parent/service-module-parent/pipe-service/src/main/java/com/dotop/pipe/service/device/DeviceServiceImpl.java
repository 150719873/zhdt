package com.dotop.pipe.service.device;

import com.dotop.pipe.api.client.IWaterDictClient;
import com.dotop.pipe.api.client.core.WaterDictUtils;
import com.dotop.pipe.api.client.core.WaterProductUtils;
import com.dotop.pipe.api.dao.device.IDeviceDao;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.dto.decive.DeviceDataDto;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.utils.ColumnToListUtils;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceFieldVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.InventoryVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.core.vo.report.DeviceCurrVo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements IDeviceService {

    private final static Logger logger = LogManager.getLogger(DeviceServiceImpl.class);

    @Autowired
    private IDeviceDao iDeviceDao;

    @Autowired
    private IProductService iProductService;

    //    @Autowired
//    private IWaterProductClient iWaterProductClient;

    @Autowired
    private IWaterDictClient iWaterDictClient;

    @Override
    public Pagination<DeviceVo> page(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            // 请求产品
//            Map<String, ProductVo> productVoMap = iWaterProductClient.storeMap(deviceBo.getProductCategory());
            Map<String, ProductVo> productVoMap = iProductService.map(new ProductBo(deviceBo.getProductCategory(), null, null, deviceBo.getEnterpriseId()));
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
            deviceDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageSize());
            deviceDto.setProductIds(new ArrayList<>(productVoMap.keySet()));
            List<DeviceVo> list = iDeviceDao.list(deviceDto);
            // 组装产品
            WaterProductUtils.build(productVoMap, list, deviceBo.getProductCategory());
            Pagination<DeviceVo> pagination = new Pagination<>(deviceBo.getPageSize(), deviceBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> list(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
            deviceDto.setIsDel(isDel);
            if (deviceBo.getLimit() != null) {
                PageHelper.startPage(0, deviceBo.getLimit());
            }
            List<DeviceVo> list;
            if (deviceBo.getProductIds() == null || deviceBo.getProductIds().isEmpty()) {
                // 请求产品
                // Map<String, ProductVo> productVoMap = iWaterProductClient.storeMap(deviceBo.getProductCategory());
                Map<String, ProductVo> productVoMap = iProductService.map(new ProductBo(deviceBo.getProductCategory(), null, null, deviceBo.getEnterpriseId()));
                deviceDto.setProductIds(new ArrayList<>(productVoMap.keySet()));
                list = iDeviceDao.list(deviceDto);
                // 组装产品
                WaterProductUtils.build(productVoMap, list, deviceBo.getProductCategory());
            } else {
                list = iDeviceDao.list(deviceDto);
            }
            return list;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceVo get(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            deviceDto.setEnterpriseId(deviceBo.getEnterpriseId());
            deviceDto.setDeviceId(deviceBo.getDeviceId());
            deviceDto.setProductId(deviceDto.getProductId());
            deviceDto.setCode(deviceBo.getCode());
            deviceDto.setIsDel(isDel);
            DeviceVo deviceVo = iDeviceDao.get(deviceDto);
            if (deviceVo == null) {
                logger.error("设备不存在");
                throw new FrameworkRuntimeException("设备不存在");
            }
            // 请求产品
            if (deviceVo.getProduct() != null && StringUtils.isNotBlank(deviceVo.getProduct().getProductId())) {
//                ProductVo store = iWaterProductClient.store(deviceVo.getProduct().getProductId(), null, deviceBo.getProductCategory());
                ProductVo store = iProductService.get(new ProductBo(deviceVo.getProductCategory(), deviceVo.getProduct().getProductId(), null, deviceBo.getEnterpriseId()));
                WaterProductUtils.build(store, deviceVo, deviceBo.getProductCategory());
                // 库存数量
//                InventoryVo inventory = iWaterProductClient.inventory(deviceVo.getProduct().getProductId(), null, deviceBo.getProductCategory());
                InventoryVo inventory = iProductService.inventory(deviceVo.getProduct().getProductId(), null, deviceVo.getProductCategory());
                WaterProductUtils.build(inventory, store, deviceVo.getProductCategory());
            }
            // 组装字典
            DictionaryVo laying = iWaterDictClient.get(CommonConstants.DICTIONARY_TYPE_LAYING);
            WaterDictUtils.laying(laying, deviceVo);
            return deviceVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public DeviceVo add(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            String deviceId = UuidUtils.getUuid();
            // 参数转换
            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
            deviceDto.setDeviceId(deviceId);
            deviceDto.setIsDel(isDel);
            iDeviceDao.add(deviceDto);

            DeviceVo deviceVo = new DeviceVo();
            deviceVo.setDeviceId(deviceId);
            return deviceVo;

        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public DeviceVo edit(DeviceBo deviceBo) throws FrameworkRuntimeException {

        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
            deviceDto.setIsDel(isDel);
            Integer updCount = iDeviceDao.edit(deviceDto);
            if (updCount != 1) {
                logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_UPDATE_ERROR, "msg",
                        PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_UPDATE_ERROR), "deviceBo",
                        deviceBo));
                throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_UPDATE_ERROR,
                        PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_UPDATE_ERROR));
            }
            return new DeviceVo();
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            Integer newIsDel = RootModel.DEL;
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            deviceDto.setIsDel(isDel);
            deviceDto.setNewIsDel(newIsDel);
            deviceDto.setEnterpriseId(deviceBo.getEnterpriseId());
            deviceDto.setDeviceId(deviceBo.getDeviceId());
            deviceDto.setDeviceIds(deviceBo.getDeviceIds());
            deviceDto.setCurr(deviceBo.getCurr());
            deviceDto.setUserBy(deviceBo.getUserBy());

            // 校验 deviceId 或者deviceIds 不能同时等于空
            if (StringUtils.isBlank(deviceBo.getDeviceId()) && deviceBo.getDeviceIds() == null) {
                logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_DEL_ERROR, "msg",
                        PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_DEL_ERROR), "deviceBo",
                        deviceBo));
                throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_DEL_ERROR,
                        PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_DEL_ERROR));
            }
            Integer updCount = iDeviceDao.del(deviceDto);
            // if (updCount != 1) {
            // logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_DEL_ERROR, "msg",
            // PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_DEL_ERROR),
            // "deviceBo",
            // deviceBo));
            // throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_DEL_ERROR,
            // PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_DEL_ERROR));
            // }
            return String.valueOf(updCount);

        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExist(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
//            deviceDto.setCode(deviceBo.getCode()); // 编码
//            deviceDto.setEnterpriseId(deviceBo.getEnterpriseId());
            deviceDto.setIsDel(isDel);
            Boolean isExist = iDeviceDao.isExist(deviceDto);
            return isExist;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExistByProductId(String productId) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            DeviceDto deviceDto = new DeviceDto();
            deviceDto.setProductId(productId);
            deviceDto.setIsDel(isDel);
            Boolean isExist = iDeviceDao.isExist(deviceDto);
            return isExist;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceFieldVo> getDeviceField(String enterpriseId, String deviceCode) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iDeviceDao.getDeviceField(enterpriseId, deviceCode, isDel);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DevicePropertyVo> getDeviceRealTime(List<String> deviceIds, FieldTypeEnum[] fieldType, String operEid)
            throws FrameworkRuntimeException {
        try {
            DeviceDataDto deviceDataDto = new DeviceDataDto();
            if (deviceIds != null && deviceIds.size() > 0) {
                deviceDataDto.setDeviceIds(deviceIds);
                deviceDataDto.setFieldType(fieldType);
                deviceDataDto.setEnterpriseId(operEid);
                List<DevicePropertyVo> devicePropertyVoList = iDeviceDao.getDeviceRealTime(deviceDataDto);
                if (devicePropertyVoList != null) {
                    return ColumnToListUtils.toList(devicePropertyVoList);
                }
            }
            return new ArrayList<>();
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> getFmDevice(String enterpriseId) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iDeviceDao.getFmDevice(enterpriseId, isDel);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<ProductVo> getDeviceCount(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            deviceDto.setEnterpriseId(deviceBo.getEnterpriseId());
            deviceDto.setProductCategory(deviceBo.getProductCategory());
            deviceDto.setIsDel(isDel);

            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageSize());
            List<ProductVo> list = iDeviceDao.getDeviceCount(deviceDto);

            Pagination<ProductVo> pagination = new Pagination<>(deviceBo.getPageSize(), deviceBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());

            return pagination;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DevicePropertyVo getDeviceProperty(String deviceId, String code, String field, String operEid)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iDeviceDao.getDeviceProperty(deviceId, code, field, operEid, isDel);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DevicePropertyVo> getDevicePropertys(String deviceId, String code, List<String> fields, String operEid)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            DevicePropertyVo devicePropertyVo = iDeviceDao.getDevicePropertys(deviceId, code, fields, operEid, isDel);
            if (devicePropertyVo != null) {
                return ColumnToListUtils.toList(devicePropertyVo);
            } else {
                return new ArrayList<>();
            }

        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, DeviceVo> mapAll(String operEid) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iDeviceDao.mapAll(operEid, isDel);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DevicePropertyVo> getDevicePropertyAll(String operEid, String category, List<String> fields)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iDeviceDao.getDevicePropertyAll(operEid, isDel, category, fields);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<DeviceVo> pageBind(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            deviceDto.setName(deviceBo.getName());
            deviceDto.setCode(deviceBo.getCode());
            deviceDto.setEnterpriseId(deviceBo.getEnterpriseId());
            deviceDto.setProductCategory(deviceBo.getProductCategory());
            deviceDto.setAreaId(deviceBo.getAreaId());
            deviceDto.setCode(deviceBo.getCode());
            deviceDto.setName(deviceBo.getName());
            deviceDto.setDeviceId(deviceBo.getDeviceId());
            deviceDto.setIsDel(isDel);
            deviceDto.setBindStatus(deviceBo.getBindStatus());
            // 请求产品
//            Map<String, ProductVo> productVoMap = iWaterProductClient.storeMap(deviceBo.getProductCategory());
            Map<String, ProductVo> productVoMap = iProductService.map(new ProductBo(deviceBo.getProductCategory(), null, null, deviceBo.getEnterpriseId()));
            deviceDto.setProductIds(new ArrayList<>(productVoMap.keySet()));
            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageSize());
            List<DeviceVo> list = iDeviceDao.listBind(deviceDto);
            // 组装产品
            WaterProductUtils.build(productVoMap, list, deviceBo.getProductCategory());
            Pagination<DeviceVo> pagination = new Pagination<DeviceVo>(deviceBo.getPageSize(), deviceBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, DeviceVo> getDeviceIdByCode(List<String> keylist) {
        try {
            return iDeviceDao.getDeviceIdByCode(keylist);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void editScales(List<DeviceBo> deviceBos) {
        try {
            List<DeviceDto> deviceDtos = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(deviceBos, DeviceDto.class);
            Integer count = iDeviceDao.editScales(deviceDtos);
            if (count == 0) {
                logger.error("编辑失败");
                throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "编辑失败");
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> listForApp(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
            deviceDto.setIsDel(isDel);
            if (deviceBo.getLimit() != null) {
                PageHelper.startPage(0, deviceBo.getLimit());
            }
            // 请求产品
//            Map<String, ProductVo> productVoMap = iWaterProductClient.storeMap(deviceBo.getProductCategory());
            Map<String, ProductVo> productVoMap = iProductService.map(new ProductBo(deviceBo.getProductCategory(), null, null, deviceBo.getEnterpriseId()));
            deviceDto.setProductIds(new ArrayList<>(productVoMap.keySet()));
            List<DeviceVo> list = iDeviceDao.listForApp(deviceDto);
            // 组装产品
            WaterProductUtils.build(productVoMap, list, deviceBo.getProductCategory());
            return list;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<DeviceCurrVo> getDeviceCurrPropertys(DeviceBo deviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
            deviceDto.setIsDel(RootModel.NOT_DEL);
            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageSize());
            List<DeviceCurrVo> list = iDeviceDao.listForCurr(deviceDto);
            // 拼接数据返回
            return new Pagination<>(deviceBo.getPage(), deviceBo.getPageSize(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
