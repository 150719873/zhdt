package com.dotop.pipe.service.common;

import com.dotop.pipe.api.dao.common.ICommonUploadDao;
import com.dotop.pipe.api.service.common.ICommonUploadService;
import com.dotop.pipe.core.bo.common.CommonUploadBo;
import com.dotop.pipe.core.dto.common.CommonUploadDto;
import com.dotop.pipe.core.vo.common.CommonUploadVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonUploadServiceImpl implements ICommonUploadService {

    private final static Logger logger = LogManager.getLogger(CommonUploadServiceImpl.class);

    @Autowired
    private ICommonUploadDao iCommonUploadDao;

    @Override
    public CommonUploadVo add(CommonUploadBo commonUploadBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            commonUploadBo.setIsDel(isDel);
            CommonUploadDto commonUploadDto = BeanUtils.copyProperties(commonUploadBo, CommonUploadDto.class);
            iCommonUploadDao.add(commonUploadDto);
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
    public List<CommonUploadVo> get(String id, List<String> ids, String thirdId) throws FrameworkRuntimeException {
        try {
            if (ids == null) {
                ids = new ArrayList<>();
            }
            if (StringUtils.isNotEmpty(id)) {
                ids.add(id);
            }
            return iCommonUploadDao.get(ids, thirdId);
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
    public boolean del(String id, List<String> ids, String thirdId) throws FrameworkRuntimeException {
        try {
            if (ids == null) {
                ids = new ArrayList<>();
            }
            if (StringUtils.isNotEmpty(id)) {
                ids.add(id);
            }
            iCommonUploadDao.del(ids, thirdId);
            return true;
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
    public void updateThirdId(List<String> fileIdList, String thirdId) throws FrameworkRuntimeException {
        try {
            iCommonUploadDao.updateThirdId(fileIdList, thirdId);
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
