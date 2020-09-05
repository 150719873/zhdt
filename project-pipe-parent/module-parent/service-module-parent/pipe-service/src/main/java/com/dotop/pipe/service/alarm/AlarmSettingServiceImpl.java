package com.dotop.pipe.service.alarm;

import com.dotop.pipe.api.client.core.WaterProductUtils;
import com.dotop.pipe.api.dao.alarm.IAlarmSettingDao;
import com.dotop.pipe.api.service.alarm.IAlarmSettingService;
import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.core.bo.alarm.AlarmSettingBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.dto.alarm.AlarmSettingDto;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AlarmSettingServiceImpl implements IAlarmSettingService {

    private static final Logger logger = LogManager.getLogger(AlarmSettingServiceImpl.class);

    @Autowired
    private IAlarmSettingDao iAlarmSettingDao;
    //	@Autowired
//	private IWaterProductClient iWaterProductClient;
    @Autowired
    private IProductService iProductService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AlarmSettingVo add(AlarmSettingBo alarmSettingBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmSettingDto alarmSettingDto = new AlarmSettingDto();
            BeanUtils.copyProperties(alarmSettingBo, alarmSettingDto);
            alarmSettingDto.setIsDel(isDel);
            // 查看是否存在 存在则先删除
            iAlarmSettingDao.del(alarmSettingDto);
            // 在插入
            iAlarmSettingDao.add(alarmSettingDto);

            return null;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<AlarmSettingVo> gets(AlarmSettingBo alarmSettingBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmSettingDto alarmSettingDto = new AlarmSettingDto();
            BeanUtils.copyProperties(alarmSettingBo, alarmSettingDto);
            alarmSettingDto.setIsDel(isDel);
            return iAlarmSettingDao.gets(alarmSettingDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<DeviceVo> page(DeviceBo deviceBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            deviceDto.setIsDel(isDel);
            // 请求产品
//            Map<String, ProductVo> productVoMap = iWaterProductClient.storeMap(deviceBo.getProductCategory());
            Map<String, ProductVo> productVoMap = iProductService.map(new ProductBo(deviceBo.getProductCategory(), null, null, deviceBo.getEnterpriseId()));
            deviceDto.setProductIds(new ArrayList<>(productVoMap.keySet()));
            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageSize());
            List<DeviceVo> list = iAlarmSettingDao.page(deviceDto);
            // 组装产品
            WaterProductUtils.build(productVoMap, list, deviceBo.getProductCategory());
            Pagination<DeviceVo> pagination = new Pagination<DeviceVo>(deviceBo.getPageSize(), deviceBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }


    @Override
    public Pagination<DeviceVo> areaAlarmSettingPage(DeviceBo deviceBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            deviceDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageSize());
            List<DeviceVo> list = iAlarmSettingDao.areaAlarmSettingPage(deviceDto);
            // 封装产品参数
            for (DeviceVo deviceVo : list) {
                ProductVo product = new ProductVo();
                DictionaryVo category = new DictionaryVo();
                category.setVal(deviceBo.getProductCategory());
                product.setCategory(category);
                product.setType(category);
                deviceVo.setProduct(product);
            }
            Pagination<DeviceVo> pagination = new Pagination<DeviceVo>(deviceBo.getPageSize(), deviceBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void importData(Map<String, AlarmSettingBo> propertiesMap, String userBy, Date curr, String enterpriseId) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            List<AlarmSettingBo> list = new ArrayList<>(propertiesMap.values());
            if (list.isEmpty()) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR);
            } else {
                iAlarmSettingDao.delList(list);
                // 在插入
                iAlarmSettingDao.addList(list, userBy, curr, enterpriseId, isDel);
            }
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public AlarmSettingVo get(AlarmSettingBo alarmSettingBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmSettingDto alarmSettingDto = new AlarmSettingDto();
            BeanUtils.copyProperties(alarmSettingBo, alarmSettingDto);
            alarmSettingDto.setIsDel(isDel);
            List<AlarmSettingVo> list = iAlarmSettingDao.gets(alarmSettingDto);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            } else {
                return null;
            }

        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }
}
