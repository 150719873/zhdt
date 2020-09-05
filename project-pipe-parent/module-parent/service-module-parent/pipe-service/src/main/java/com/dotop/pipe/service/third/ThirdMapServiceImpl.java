package com.dotop.pipe.service.third;

import com.dotop.pipe.api.dao.third.IThirdMapDao;
import com.dotop.pipe.api.service.third.IThirdMapService;
import com.dotop.pipe.core.bo.third.ThirdMapBo;
import com.dotop.pipe.core.dto.third.ThirdMapDto;
import com.dotop.pipe.core.vo.third.ThirdMapVo;
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

import java.util.Date;
import java.util.List;

@Service
public class ThirdMapServiceImpl implements IThirdMapService {

    private final static Logger logger = LogManager.getLogger(ThirdMapServiceImpl.class);

    @Autowired
    private IThirdMapDao iThirdMapDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void add(String enterpriseId, String deviceId, String deviceCode, String thirdId, String thirdCode,
                    Date curr, String userBy, String deviceType, String protocol) throws FrameworkRuntimeException {
        try {
            String mapId = UuidUtils.getUuid();
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setMapId(mapId);
            thirdMapDto.setDeviceId(deviceId);
            thirdMapDto.setDeviceCode(deviceCode);
            thirdMapDto.setThirdId(thirdId);
            thirdMapDto.setThirdCode(thirdCode);
            thirdMapDto.setCurr(curr);
            thirdMapDto.setUserBy(userBy);
            thirdMapDto.setIsDel(isDel);
            thirdMapDto.setDeviceType(deviceType);
            thirdMapDto.setFactoryName("iot");
            thirdMapDto.setProtocol(protocol);
            iThirdMapDao.add(thirdMapDto);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void edit(String enterpriseId, String mapId, String deviceCode, String thirdCode, Date curr, String userBy, String deviceType, String protocol) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setMapId(mapId);
            thirdMapDto.setDeviceCode(deviceCode);
            thirdMapDto.setThirdCode(thirdCode);
            thirdMapDto.setCurr(curr);
            thirdMapDto.setUserBy(userBy);
            thirdMapDto.setIsDel(isDel);
            thirdMapDto.setDeviceType(deviceType);
            thirdMapDto.setFactoryName("iot");
            thirdMapDto.setProtocol(protocol);
            iThirdMapDao.edit(thirdMapDto);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void del(String enterpriseId, String deviceId, Date curr, String userBy) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            Integer newIsDel = RootModel.DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setDeviceId(deviceId);
            thirdMapDto.setNewIsDel(newIsDel);
            thirdMapDto.setCurr(curr);
            thirdMapDto.setUserBy(userBy);
            thirdMapDto.setIsDel(isDel);
            iThirdMapDao.del(thirdMapDto);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void delByCode(String enterpriseId, String sensorCode, Date curr, String userBy)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            Integer newIsDel = RootModel.DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setDeviceCode(sensorCode);
            thirdMapDto.setNewIsDel(newIsDel);
            thirdMapDto.setCurr(curr);
            thirdMapDto.setUserBy(userBy);
            thirdMapDto.setIsDel(isDel);
            iThirdMapDao.del(thirdMapDto);
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
    public Boolean isExist(String enterpriseId, String deviceId) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setDeviceId(deviceId);
            thirdMapDto.setIsDel(isDel);
            Boolean isExist = iThirdMapDao.isExist(thirdMapDto);
            return isExist;
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
    public ThirdMapVo get(String enterpriseId, String deviceId) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setDeviceId(deviceId);
            thirdMapDto.setIsDel(isDel);
            ThirdMapVo thirdMap = iThirdMapDao.get(thirdMapDto);
            return thirdMap;
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
    public ThirdMapVo get(String enterpriseId, String deviceId, String deviceCode) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setDeviceId(deviceId);
            thirdMapDto.setDeviceCode(deviceCode);
            thirdMapDto.setIsDel(isDel);
            ThirdMapVo thirdMap = iThirdMapDao.get(thirdMapDto);
            return thirdMap;
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
    public Boolean isExistByThirdId(String enterpriseId, String thirdId) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setThirdId(thirdId);
            thirdMapDto.setIsDel(isDel);
            Boolean isExist = iThirdMapDao.isExist(thirdMapDto);
            return isExist;
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
    public Boolean isExistByThirdCode(String enterpriseId, String thirdCode) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setThirdCode(thirdCode);
            thirdMapDto.setIsDel(isDel);
            Boolean isExist = iThirdMapDao.isExist(thirdMapDto);
            return isExist;
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
    public Pagination<ThirdMapVo> page(String enterpriseId, String factoryCode, Integer page, Integer pageSize)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ThirdMapDto thirdMapDto = new ThirdMapDto();
            thirdMapDto.setEnterpriseId(enterpriseId);
            thirdMapDto.setFactoryCode(factoryCode);
            thirdMapDto.setIsDel(isDel);

            Page<Object> pageHelper = PageHelper.startPage(page, pageSize);
            List<ThirdMapVo> list = iThirdMapDao.list(thirdMapDto);

            Pagination<ThirdMapVo> pagination = new Pagination<ThirdMapVo>(pageSize, page);
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
    public List<ThirdMapVo> list(ThirdMapBo thirdMapBo) {
        try {
            ThirdMapDto thirdMapDto = BeanUtils.copy(thirdMapBo, ThirdMapDto.class);
            thirdMapDto.setIsDel(RootModel.NOT_DEL);
            return iThirdMapDao.list(thirdMapDto);
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ThirdMapVo edit(ThirdMapBo thirdMapBo) throws FrameworkRuntimeException {
        try {
            ThirdMapDto thirdMapDto = BeanUtils.copy(thirdMapBo, ThirdMapDto.class);
            thirdMapDto.setIsDel(RootModel.NOT_DEL);
            thirdMapDto.setCurr(new Date());
            if (iThirdMapDao.edit(thirdMapDto) != 1) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "编辑错误");
            }
            return BeanUtils.copy(thirdMapBo, ThirdMapVo.class);
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
