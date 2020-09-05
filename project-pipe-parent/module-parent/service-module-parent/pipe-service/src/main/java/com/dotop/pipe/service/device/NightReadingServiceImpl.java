package com.dotop.pipe.service.device;

import com.dotop.pipe.api.dao.device.INightReadingDao;
import com.dotop.pipe.api.service.device.INightReadingService;
import com.dotop.pipe.core.bo.report.NightReadingBo;
import com.dotop.pipe.core.dto.report.NightReadingDto;
import com.dotop.pipe.core.vo.report.NightReadingVo;
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

import java.util.List;

/**
 *
 */
@Service
public class NightReadingServiceImpl implements INightReadingService {

    private final static Logger logger = LogManager.getLogger(NightReadingServiceImpl.class);

    @Autowired
    private INightReadingDao iNightReadingDao;

    @Override
    public List<NightReadingVo> listDevices(NightReadingBo nightReadingBo) throws FrameworkRuntimeException {
        try {
            NightReadingDto nightReadingDto = BeanUtils.copyProperties(nightReadingBo, NightReadingDto.class);
            nightReadingDto.setIsDel(RootModel.NOT_DEL);
            return iNightReadingDao.listDevices(nightReadingDto);
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
