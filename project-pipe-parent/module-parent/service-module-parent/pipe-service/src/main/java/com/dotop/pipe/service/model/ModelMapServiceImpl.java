package com.dotop.pipe.service.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.pipe.api.dao.model.IModelMapDao;
import com.dotop.pipe.api.service.model.IModelMapService;
import com.dotop.pipe.core.dto.model.ModelMapDto;
import com.dotop.pipe.core.vo.model.ModelMapVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;

@Service
public class ModelMapServiceImpl implements IModelMapService {

	private final static Logger logger = LogManager.getLogger(ModelMapServiceImpl.class);

	@Autowired
	private IModelMapDao iModelMapDao;

	@Override
	public ModelMapVo getByDeviceId(String enterpriseId, String deviceId) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			ModelMapDto modelMapDto = new ModelMapDto();
			modelMapDto.setEnterpriseId(enterpriseId);
			modelMapDto.setDeviceId(deviceId);
			modelMapDto.setIsDel(isDel);
			ModelMapVo modelMap = iModelMapDao.get(modelMapDto);
			return modelMap;
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
