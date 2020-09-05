package com.dotop.pipe.service.log;

import com.dotop.pipe.api.dao.log.ILogPointDao;
import com.dotop.pipe.api.service.log.ILogPointService;
import com.dotop.pipe.core.bo.log.LogPointBo;
import com.dotop.pipe.core.dto.log.LogPointDto;
import com.dotop.pipe.core.vo.log.LogPointVo;
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
public class LogPointServiceImpl implements ILogPointService {

    private final static Logger logger = LogManager.getLogger(LogPointServiceImpl.class);

    @Autowired
    private ILogPointDao iLogPointDao;


    @Override
    public List<LogPointVo> list(LogPointBo logPointBo) throws FrameworkRuntimeException {
        try {
            LogPointDto logPointDto = BeanUtils.copy(logPointBo, LogPointDto.class);
            logPointDto.setIsDel(RootModel.NOT_DEL);
            return iLogPointDao.list(logPointDto);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<LogPointBo> logPointBos) throws FrameworkRuntimeException {
        try {
            List<LogPointDto> logPointDtos = BeanUtils.copy(logPointBos, LogPointDto.class);
//               iLogPointDao.adds(logPointDtos);
            int pageSize = 500;
            int total = logPointDtos.size();
            List<LogPointDto> subList;
            int cycleTotal = total % pageSize == 0 ? (total / pageSize) : (total / pageSize) + 1;
            for (int i = 0; i < cycleTotal; i++) {
                // 循环
                if ((i + 1) * pageSize > total) {
                    subList = logPointDtos.subList(i * pageSize, total);
                } else {
                    subList = logPointDtos.subList(i * pageSize, (i + 1) * pageSize);
                }
                iLogPointDao.adds(subList);
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void dels(LogPointBo logPointBo) throws FrameworkRuntimeException {
        try {
            LogPointDto logPointDto = BeanUtils.copy(logPointBo, LogPointDto.class);
            logPointDto.setIsDel(1);
            Integer count = iLogPointDao.del(logPointDto);
            if (count == 0) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "删除失败");
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
