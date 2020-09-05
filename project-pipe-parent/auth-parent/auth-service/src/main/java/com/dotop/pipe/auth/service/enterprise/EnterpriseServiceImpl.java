package com.dotop.pipe.auth.service.enterprise;

import com.dotop.pipe.auth.api.cache.enterprise.IEnterpriseCache;
import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import com.dotop.pipe.auth.api.dao.enterprise.IEnterpriseDao;
import com.dotop.pipe.auth.api.service.enterprise.IEnterpriseService;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Service
public class EnterpriseServiceImpl implements IEnterpriseService {

    private final static Logger logger = LogManager.getLogger(EnterpriseServiceImpl.class);

    @Autowired
    private IEnterpriseDao iEnterpriseDao;

    @Autowired
    private IEnterpriseCache iEnterpriseCache;

    @Override
    public EnterpriseVo get(String eid) throws FrameworkRuntimeException {
        EnterpriseVo enterprise = iEnterpriseCache.get(eid);
        if (enterprise == null) {
            Integer isDel = RootModel.NOT_DEL;
            enterprise = iEnterpriseDao.get(null, eid, isDel);
            if (enterprise != null) {
                String enterpriseId = enterprise.getEnterpriseId();
                iEnterpriseCache.set(enterpriseId, eid);
            }
        }
        return enterprise;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String add(String eid, String enterpriseName, Date curr, String userBy) throws FrameworkRuntimeException {
        try {
            // 简单设计 cas企业eid与管漏系统enterpriseId保持一样
            String enterpriseId = eid;
            // 不简单设计
            // String enterpriseId = UuidUtils.getUuid();
            Integer isDel = RootModel.NOT_DEL;
            iEnterpriseDao.add(enterpriseId, eid, enterpriseName, new ArrayList<>(), curr, userBy, isDel);
            iEnterpriseCache.set(enterpriseId, eid);
            return enterpriseId;
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
    public EnterpriseVo getDb(String eid) throws FrameworkRuntimeException {
        Integer isDel = RootModel.NOT_DEL;
        EnterpriseVo enterprise = iEnterpriseDao.get(null, eid, isDel);
        if (enterprise != null) {
            String enterpriseId = enterprise.getEnterpriseId();
            iEnterpriseCache.set(enterpriseId, eid);
        }
        return enterprise;
    }

}
