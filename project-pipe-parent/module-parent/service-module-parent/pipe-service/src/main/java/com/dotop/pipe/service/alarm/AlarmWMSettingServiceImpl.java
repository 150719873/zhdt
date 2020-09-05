package com.dotop.pipe.service.alarm;

import com.dotop.pipe.api.client.core.WaterProductUtils;
import com.dotop.pipe.api.dao.alarm.IAlarmWMSettingDao;
import com.dotop.pipe.api.service.alarm.IAlarmWMSettingService;
import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.core.bo.alarm.AlarmWMSettingBo;
import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.dto.alarm.AlarmWMSettingDto;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
public class AlarmWMSettingServiceImpl implements IAlarmWMSettingService {

    private static final Logger logger = LogManager.getLogger(AlarmWMSettingServiceImpl.class);

    @Autowired
    private IAlarmWMSettingDao iAlarmWMSettingDao;
    //	@Autowired
//    private IWaterProductClient iWaterProductClient;
    @Autowired
    private IProductService iProductService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public BaseVo add(AlarmWMSettingBo alarmSettingBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmWMSettingDto alarmSettingDto = BeanUtils.copyProperties(alarmSettingBo, AlarmWMSettingDto.class);
            alarmSettingDto.setIsDel(isDel);
            alarmSettingDto.setId(UuidUtils.getUuid());
            // 在插入
            iAlarmWMSettingDao.add(alarmSettingDto);
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
    public Pagination<DeviceVo> getPage(AlarmWMSettingBo alarmWMSettingBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            AlarmWMSettingDto alarmWMSettingDto = BeanUtils.copyProperties(alarmWMSettingBo, AlarmWMSettingDto.class);
            alarmWMSettingDto.setIsDel(isDel);
            // 请求产品
//            Map<String, ProductVo> productVoMap = iWaterProductClient.storeMap(CommonConstants.PRODUCT_CATEGORY_SENSOR);
            Map<String, ProductVo> productVoMap = iProductService.map(new ProductBo(CommonConstants.PRODUCT_CATEGORY_SENSOR, null, null, alarmWMSettingBo.getEnterpriseId()));
            alarmWMSettingDto.setProductIds(new ArrayList<>(productVoMap.keySet()));

            Page<Object> pageHelper = PageHelper.startPage(alarmWMSettingBo.getPage(), alarmWMSettingBo.getPageSize());
            List<DeviceVo> list = iAlarmWMSettingDao.getPage(alarmWMSettingDto);
            // 组装产品
            WaterProductUtils.build(productVoMap, list, CommonConstants.PRODUCT_CATEGORY_SENSOR);
            Pagination<DeviceVo> pagination = new Pagination<DeviceVo>(alarmWMSettingBo.getPageSize(),
                    alarmWMSettingBo.getPage());
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
    public BaseVo edit(AlarmWMSettingBo alarmWMSettingBo) {
        try {
            Integer isDel = RootModel.DEL;
            AlarmWMSettingDto alarmSettingDto = BeanUtils.copyProperties(alarmWMSettingBo, AlarmWMSettingDto.class);
            alarmSettingDto.setIsDel(isDel);
            // 在插入
            iAlarmWMSettingDao.edit(alarmSettingDto);
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
    public List<DeviceVo> getWmAlarm(AlarmWMSettingBo alarmWMSettingBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmWMSettingDto alarmSettingDto = BeanUtils.copyProperties(alarmWMSettingBo, AlarmWMSettingDto.class);
            alarmSettingDto.setIsDel(isDel);
            // 在插入
            return iAlarmWMSettingDao.getWmAlarm(alarmSettingDto);
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
