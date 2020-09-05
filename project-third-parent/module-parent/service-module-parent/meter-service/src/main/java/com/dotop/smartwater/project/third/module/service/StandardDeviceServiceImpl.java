package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IWaterDeviceDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IStandardDeviceService;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class StandardDeviceServiceImpl implements IStandardDeviceService {


    @Autowired
    private IWaterDeviceDao iWaterDeviceDao;






    @Override
    public Pagination<DeviceVo> pageDevice(DeviceBo deviceBo, Integer page, Integer pageCount) throws FrameworkRuntimeException {
        DeviceDto deviceDto = BeanUtils.copy(deviceBo, DeviceDto.class);
        deviceDto.setIsDel(RootModel.NOT_DEL);
        Page<Object> resultPage = PageHelper.startPage(page, pageCount);
        List<DeviceVo> deviceVos = iWaterDeviceDao.list(deviceDto);
        return  new Pagination<>(page, pageCount, deviceVos, resultPage.getTotal());
    }
}
