package com.dotop.pipe.service.log;

import com.dotop.pipe.api.dao.log.ILogPointMapDao;
import com.dotop.pipe.api.service.log.ILogPointMapService;
import com.dotop.pipe.core.bo.log.LogPointMapBo;
import com.dotop.pipe.core.dto.log.LogPointMapDto;
import com.dotop.pipe.core.vo.log.LogPointMapVo;
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
public class LogPointMapServiceImpl implements ILogPointMapService {

	private final static Logger logger = LogManager.getLogger(LogPointMapServiceImpl.class);

	@Autowired
	private ILogPointMapDao iLogPointMapDao;


	@Override
	public List<LogPointMapVo> list(LogPointMapBo logPointMapBo) throws FrameworkRuntimeException {
		try {
			LogPointMapDto logPointMapDto = BeanUtils.copy(logPointMapBo, LogPointMapDto.class);
			logPointMapDto.setIsDel(RootModel.NOT_DEL);
			return iLogPointMapDao.list(logPointMapDto);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void adds(List<LogPointMapBo> logPointMapBos) throws FrameworkRuntimeException {
		try {
			List<LogPointMapDto> logPointMapDtos = BeanUtils.copy(logPointMapBos, LogPointMapDto.class);
//			iLogPointMapDao.adds(logPointMapDtos);
			int pageSize = 500;
			int total = logPointMapDtos.size();
			List<LogPointMapDto> subList;
			int cycleTotal = total % pageSize == 0 ? (total / pageSize) : (total / pageSize) + 1;
			for (int i = 0; i < cycleTotal; i++) {
				// 循环
				if ((i + 1) * pageSize > total) {
					subList = logPointMapDtos.subList(i * pageSize, total);
				} else {
					subList = logPointMapDtos.subList(i * pageSize, (i + 1) * pageSize);
				}
				iLogPointMapDao.adds(subList);
			}
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void dels(LogPointMapBo logPointMapBo) throws FrameworkRuntimeException {
		try {
			LogPointMapDto logPointMapDto = BeanUtils.copy(logPointMapBo, LogPointMapDto.class);
			logPointMapDto.setIsDel(1);
			Integer count = iLogPointMapDao.del(logPointMapDto);
			if(count == 0) {
				throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "删除失败");
			}
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
