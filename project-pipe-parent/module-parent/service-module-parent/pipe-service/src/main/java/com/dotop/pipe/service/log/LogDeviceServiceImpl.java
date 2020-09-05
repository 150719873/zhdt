package com.dotop.pipe.service.log;

import com.dotop.pipe.api.dao.log.ILogDeviceDao;
import com.dotop.pipe.api.service.log.ILogDeviceService;
import com.dotop.pipe.core.bo.log.LogDeviceBo;
import com.dotop.pipe.core.dto.log.LogDeviceDto;
import com.dotop.pipe.core.vo.log.LogDeviceVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogDeviceServiceImpl implements ILogDeviceService {

    private final static Logger logger = LogManager.getLogger(LogDeviceServiceImpl.class);

    @Autowired
    private ILogDeviceDao iLogDeviceDao;

    @Override
    public List<LogDeviceVo> list(LogDeviceBo logDeviceBo) throws FrameworkRuntimeException {
        try {
            LogDeviceDto logDeviceDto = BeanUtils.copy(logDeviceBo, LogDeviceDto.class);
            logDeviceDto.setIsDel(RootModel.NOT_DEL);
            return iLogDeviceDao.list(logDeviceDto);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<LogDeviceBo> logDeviceBos) throws FrameworkRuntimeException {
        try {
            List<LogDeviceDto> logDeviceDtos = BeanUtils.copy(logDeviceBos, LogDeviceDto.class);
//			iLogDeviceDao.adds(logDeviceDtos);
            int pageSize = 500;
            int total = logDeviceDtos.size();
            List<LogDeviceDto> subList;
            int cycleTotal = total % pageSize == 0 ? (total / pageSize) : (total / pageSize) + 1;
            for (int i = 0; i < cycleTotal; i++) {
                // 循环
                if ((i + 1) * pageSize > total) {
                    subList = logDeviceDtos.subList(i * pageSize, total);
                } else {
                    subList = logDeviceDtos.subList(i * pageSize, (i + 1) * pageSize);
                }
                iLogDeviceDao.adds(subList);
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void dels(LogDeviceBo logDeviceBo) throws FrameworkRuntimeException {
        try {
            LogDeviceDto logDeviceDto = BeanUtils.copy(logDeviceBo, LogDeviceDto.class);
            logDeviceDto.setIsDel(1);
            Integer count = iLogDeviceDao.del(logDeviceDto);
            if (count == 0) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "删除失败");
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
