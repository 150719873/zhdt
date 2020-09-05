package com.dotop.pipe.service.devicedata;

import com.dotop.pipe.api.client.core.WaterProductUtils;
import com.dotop.pipe.api.dao.devicedata.IMeterReadingDao;
import com.dotop.pipe.api.service.devicedata.IMeterReadingService;
import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.core.bo.area.AreaBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.dto.area.AreaDto;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MeterReadingServiceImpl implements IMeterReadingService {

    private final static Logger logger = LogManager.getLogger(MeterReadingServiceImpl.class);

    @Autowired
    private IMeterReadingDao iMeterReadingDao;
    //	@Autowired
//	private IWaterProductClient iWaterProductClient;
    @Autowired
    private IProductService iProductService;

    @Override
    public Pagination<DeviceVo> devicePage(DeviceBo deviceBo) {

        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
            deviceDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageSize());
            List<DeviceVo> list = iMeterReadingDao.deviceList(deviceDto);
            // 请求产品
//			Map<String, ProductVo> productVoMap = iWaterProductClient.storeMap(deviceBo.getProductCategory());
            Map<String, ProductVo> productVoMap = iProductService.map(new ProductBo(deviceBo.getProductCategory(), null, null, deviceBo.getEnterpriseId()));
            deviceDto.setProductIds(new ArrayList<>(productVoMap.keySet()));
            // 组装产品
            WaterProductUtils.build(productVoMap, list, deviceBo.getProductCategory());
            Pagination<DeviceVo> pagination = new Pagination<>(deviceBo.getPageSize(), deviceBo.getPage());
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
    public Pagination<DeviceVo> areaPage(AreaBo areaBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            AreaDto areaDto = BeanUtils.copyProperties(areaBo, AreaDto.class);
            areaDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(areaBo.getPage(), areaBo.getPageSize());
            List<DeviceVo> list = iMeterReadingDao.areaList(areaDto);
            // 组装产品
            Pagination<DeviceVo> pagination = new Pagination<>(areaBo.getPageSize(), areaBo.getPage());
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

}
