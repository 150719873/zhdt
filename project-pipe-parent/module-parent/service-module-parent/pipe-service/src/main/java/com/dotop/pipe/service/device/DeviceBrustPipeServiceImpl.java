package com.dotop.pipe.service.device;

import com.dotop.pipe.api.dao.device.IDeviceBrustPipeDao;
import com.dotop.pipe.api.service.device.IDeviceBrustPipeService;
import com.dotop.pipe.core.bo.report.DeviceBrustPipeBo;
import com.dotop.pipe.core.dto.report.DeviceBrustPipeDto;
import com.dotop.pipe.core.vo.report.DeviceBrustPipeVo;
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

import java.util.List;

/**
 *
 */
@Service
public class DeviceBrustPipeServiceImpl implements IDeviceBrustPipeService {

    private final static Logger logger = LogManager.getLogger(DeviceBrustPipeServiceImpl.class);

    @Autowired
    private IDeviceBrustPipeDao iDeviceBrustPipeDao;

    @Override
    public Pagination<DeviceBrustPipeVo> pagePipe(DeviceBrustPipeBo deviceBrustPipeBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            DeviceBrustPipeDto deviceBrustPipeDto = BeanUtils.copyProperties(deviceBrustPipeBo, DeviceBrustPipeDto.class);
            deviceBrustPipeDto.setIsDel(RootModel.NOT_DEL);
            Page<Object> pageHelper = PageHelper.startPage(deviceBrustPipeBo.getPage(), deviceBrustPipeBo.getPageSize());
            List<DeviceBrustPipeVo> list = iDeviceBrustPipeDao.listPipe(deviceBrustPipeDto);
            // 拼接数据返回
            return new Pagination<>(deviceBrustPipeBo.getPage(), deviceBrustPipeBo.getPageSize(), list, pageHelper.getTotal());
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
    public Pagination<DeviceBrustPipeVo> pageArea(DeviceBrustPipeBo deviceBrustPipeBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            DeviceBrustPipeDto deviceBrustPipeDto = BeanUtils.copyProperties(deviceBrustPipeBo, DeviceBrustPipeDto.class);
            deviceBrustPipeDto.setIsDel(RootModel.NOT_DEL);
            Page<Object> pageHelper = PageHelper.startPage(deviceBrustPipeBo.getPage(), deviceBrustPipeBo.getPageSize());
            List<DeviceBrustPipeVo> list = iDeviceBrustPipeDao.listArea(deviceBrustPipeDto);
            // 拼接数据返回
            return new Pagination<>(deviceBrustPipeBo.getPage(), deviceBrustPipeBo.getPageSize(), list, pageHelper.getTotal());
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
