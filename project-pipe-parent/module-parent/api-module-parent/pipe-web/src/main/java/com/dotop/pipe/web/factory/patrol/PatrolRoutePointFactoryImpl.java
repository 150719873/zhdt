package com.dotop.pipe.web.factory.patrol;

import com.dotop.pipe.web.api.factory.patrol.IPatrolRoutePointFactory;
import com.dotop.pipe.api.service.patrol.IPatrolRoutePointService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.patrol.PatrolRoutePointBo;
import com.dotop.pipe.core.form.PatrolRoutePointForm;
import com.dotop.pipe.core.vo.patrol.PatrolRoutePointVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
public class PatrolRoutePointFactoryImpl implements IPatrolRoutePointFactory {
    private final static Logger logger = LogManager.getLogger(PatrolRoutePointFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IPatrolRoutePointService iPatrolRoutePointService;

    @Override
    public PatrolRoutePointVo add(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            String operEid = loginCas.getOperEid();
            String userBy = loginCas.getUserName();
            PatrolRoutePointBo patrolRoutePointBo = BeanUtils.copyProperties(patrolRoutePointForm,
                    PatrolRoutePointBo.class);
            Date curr = new Date();
            patrolRoutePointBo.setCurr(curr);
            patrolRoutePointBo.setUserBy(userBy);
            patrolRoutePointBo.setEnterpriseId(operEid);
            patrolRoutePointBo.setIsDel(RootModel.NOT_DEL);
            return iPatrolRoutePointService.add(patrolRoutePointBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRoutePointVo get(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException {
        try {
            PatrolRoutePointBo patrolRoutePointBo = BeanUtils.copy(patrolRoutePointForm, PatrolRoutePointBo.class);
            patrolRoutePointBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iPatrolRoutePointService.get(patrolRoutePointBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolRoutePointVo> page(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException {
        try {
            PatrolRoutePointBo patrolRoutePointBo = BeanUtils.copy(patrolRoutePointForm, PatrolRoutePointBo.class);
            patrolRoutePointBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            Pagination<PatrolRoutePointVo> page = iPatrolRoutePointService.page(patrolRoutePointBo);
            return page;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolRoutePointVo> list(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException {
        try {
            PatrolRoutePointBo patrolRoutePointBo = BeanUtils.copy(patrolRoutePointForm, PatrolRoutePointBo.class);
            if (StringUtils.isBlank(patrolRoutePointForm.getEnterpriseId())) {
                patrolRoutePointBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            }
            return iPatrolRoutePointService.list(patrolRoutePointBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRoutePointVo edit(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException {
        try {
            Date curr = new Date();
            PatrolRoutePointBo patrolRoutePointBo = BeanUtils.copy(patrolRoutePointForm, PatrolRoutePointBo.class);
            patrolRoutePointBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolRoutePointBo.setUserBy(iAuthCasApi.get().getUserName());
            patrolRoutePointBo.setCurr(curr);
            return iPatrolRoutePointService.edit(patrolRoutePointBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException {
        try {
            PatrolRoutePointBo patrolRoutePointBo = BeanUtils.copy(patrolRoutePointForm, PatrolRoutePointBo.class);
            patrolRoutePointBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolRoutePointBo.setIsDel(RootModel.DEL);
            return iPatrolRoutePointService.del(patrolRoutePointBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void batchAdd(List<PatrolRoutePointForm> points) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userId = loginCas.getUserId();
        String userBy = loginCas.getUserName();
        List<PatrolRoutePointBo> pointList = BeanUtils.copy(points, PatrolRoutePointBo.class);
        Date curr = new Date();
        for (PatrolRoutePointBo patrolRoutePointBo : pointList) {
            patrolRoutePointBo.setCurr(curr);
            patrolRoutePointBo.setUserBy(userBy);
            patrolRoutePointBo.setEnterpriseId(operEid);
            patrolRoutePointBo.setIsDel(RootModel.NOT_DEL);
        }
        iPatrolRoutePointService.batchAdd(pointList);
    }

    @Override
    public void batchAddWithOutAuth(List<PatrolRoutePointForm> points) {
        String userBy = "system";
        List<PatrolRoutePointBo> pointList = BeanUtils.copy(points, PatrolRoutePointBo.class);
        Date curr = new Date();
        for (PatrolRoutePointBo patrolRoutePointBo : pointList) {
            patrolRoutePointBo.setCurr(curr);
            patrolRoutePointBo.setUserBy(userBy);
            patrolRoutePointBo.setIsDel(RootModel.NOT_DEL);
        }
        iPatrolRoutePointService.batchAdd(pointList);
    }

    @Override
    public void batUpdate(List<PatrolRoutePointForm> points) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        List<PatrolRoutePointBo> pointList = BeanUtils.copy(points, PatrolRoutePointBo.class);
        Date curr = new Date();
        for (PatrolRoutePointBo patrolRoutePointBo : pointList) {
            patrolRoutePointBo.setCurr(curr);
            patrolRoutePointBo.setUserBy(userBy);
            patrolRoutePointBo.setEnterpriseId(operEid);
        }
        iPatrolRoutePointService.batchUpdate(pointList);
    }

    @Override
    public void batchDel(List<PatrolRoutePointForm> points) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        List<PatrolRoutePointBo> pointList = BeanUtils.copy(points, PatrolRoutePointBo.class);
        Date curr = new Date();
        for (PatrolRoutePointBo patrolRoutePointBo : pointList) {
            patrolRoutePointBo.setCurr(curr);
            patrolRoutePointBo.setUserBy(userBy);
            patrolRoutePointBo.setEnterpriseId(operEid);
            patrolRoutePointBo.setIsDel(RootModel.DEL);
        }
        iPatrolRoutePointService.batchDel(pointList);
    }

    @Override
    public void batchDelWithOutAuth(List<PatrolRoutePointForm> points) {
        String userBy = "system";
        List<PatrolRoutePointBo> pointList = BeanUtils.copy(points, PatrolRoutePointBo.class);
        Date curr = new Date();
        for (PatrolRoutePointBo patrolRoutePointBo : pointList) {
            patrolRoutePointBo.setCurr(curr);
            patrolRoutePointBo.setUserBy(userBy);
            patrolRoutePointBo.setIsDel(RootModel.DEL);
        }
        iPatrolRoutePointService.batchDel(pointList);
    }
}
