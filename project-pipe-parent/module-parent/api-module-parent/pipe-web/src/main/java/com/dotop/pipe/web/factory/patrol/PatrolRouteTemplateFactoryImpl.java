package com.dotop.pipe.web.factory.patrol;

import com.dotop.pipe.web.api.factory.patrol.IPatrolRoutePointFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteTemplateFactory;
import com.dotop.pipe.api.service.patrol.IPatrolRouteTemplateService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.patrol.PatrolRouteTemplateBo;
import com.dotop.pipe.core.form.PatrolRoutePointForm;
import com.dotop.pipe.core.form.PatrolRouteTemplateForm;
import com.dotop.pipe.core.vo.patrol.PatrolRoutePointVo;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTemplateVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class PatrolRouteTemplateFactoryImpl implements IPatrolRouteTemplateFactory {
    private final static Logger logger = LogManager.getLogger(PatrolRouteTemplateFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IPatrolRouteTemplateService iPatrolRouteTemplateService;

    @Autowired
    private IPatrolRoutePointFactory iPatrolRoutePointFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PatrolRouteTemplateVo add(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            String operEid = loginCas.getOperEid();
            String userId = loginCas.getUserId();
            String userBy = loginCas.getUserName();
            PatrolRouteTemplateBo patrolRouteTemplateBo = BeanUtils.copyProperties(patrolRouteTemplateForm,
                    PatrolRouteTemplateBo.class);
            Date curr = new Date();
            patrolRouteTemplateBo.setCurr(curr);
            patrolRouteTemplateBo.setUserBy(userBy);
            patrolRouteTemplateBo.setEnterpriseId(operEid);
            patrolRouteTemplateBo.setIsDel(RootModel.NOT_DEL);
            iPatrolRoutePointFactory.batchAdd(patrolRouteTemplateForm.getPatrolRoutePoints());
            return iPatrolRouteTemplateService.add(patrolRouteTemplateBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTemplateVo get(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTemplateBo patrolRouteTemplateBo = BeanUtils.copy(patrolRouteTemplateForm, PatrolRouteTemplateBo.class);
            patrolRouteTemplateBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            PatrolRouteTemplateVo patrolRouteTemplateVo = iPatrolRouteTemplateService.get(patrolRouteTemplateBo);
            List<String> pointIds = patrolRouteTemplateVo.getPatrolRoutePointIds();
            if (pointIds != null && !pointIds.isEmpty()) {
                PatrolRoutePointForm patrolRoutePointForm = new PatrolRoutePointForm();
                patrolRoutePointForm.setPatrolRoutePointIds(pointIds);
                List<PatrolRoutePointVo> patrolRoutePointList = iPatrolRoutePointFactory.list(patrolRoutePointForm);
                // 路线点排序
                List<PatrolRoutePointVo> sort = new ArrayList<>();
                if (patrolRoutePointList != null) {
                    pointIds.forEach((id) -> {
                        patrolRoutePointList.forEach((p) -> {
                            if (id.equals(p.getPatrolRoutePointId())) {
                                sort.add(p);
                            }
                        });
                    });
                }
                patrolRouteTemplateVo.setPatrolRoutePoints(sort);
            }
            return patrolRouteTemplateVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolRouteTemplateVo> page(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTemplateBo patrolRouteTemplateBo = BeanUtils.copy(patrolRouteTemplateForm, PatrolRouteTemplateBo.class);
            patrolRouteTemplateBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            Pagination<PatrolRouteTemplateVo> page = iPatrolRouteTemplateService.page(patrolRouteTemplateBo);
            List<PatrolRouteTemplateVo> data = page.getData();
            for (PatrolRouteTemplateVo patrolRouteTemplateVo : data) {
                List<String> pointIds = patrolRouteTemplateVo.getPatrolRoutePointIds();
                if (pointIds != null && !pointIds.isEmpty()) {
                    PatrolRoutePointForm patrolRoutePointForm = new PatrolRoutePointForm();
                    patrolRoutePointForm.setPatrolRoutePointIds(pointIds);
                    List<PatrolRoutePointVo> patrolRoutePointList = iPatrolRoutePointFactory.list(patrolRoutePointForm);
                    // 路线点排序
                    List<PatrolRoutePointVo> sort = new ArrayList<>();
                    if (patrolRoutePointList != null) {
                        pointIds.forEach((id) -> {
                            patrolRoutePointList.forEach((p) -> {
                                if (id.equals(p.getPatrolRoutePointId())) {
                                    sort.add(p);
                                }
                            });
                        });
                    }
                    patrolRouteTemplateVo.setPatrolRoutePoints(sort);
                }
            }
            return page;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolRouteTemplateVo> list(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTemplateBo patrolRouteTemplateBo = BeanUtils.copy(patrolRouteTemplateForm, PatrolRouteTemplateBo.class);
            patrolRouteTemplateBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iPatrolRouteTemplateService.list(patrolRouteTemplateBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PatrolRouteTemplateVo edit(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTemplateVo patrolRouteTemplateVo = get(patrolRouteTemplateForm);
            //新路线点与旧路线点求差集，得出新路线
            List<String> patrolRoutePointIds = patrolRouteTemplateVo.getPatrolRoutePointIds();
            List<PatrolRoutePointForm> newPoints = patrolRouteTemplateForm.getPatrolRoutePoints();
            List<PatrolRoutePointForm> patrolRoutePointForms = newPoints.stream().filter(s -> patrolRoutePointIds.indexOf(s.getPatrolRoutePointId()) == -1).collect(Collectors.toList());
            if (!patrolRoutePointForms.isEmpty()) {
                iPatrolRoutePointFactory.batchAdd(patrolRoutePointForms);
            }
            PatrolRouteTemplateBo patrolRouteTemplateBo = BeanUtils.copy(patrolRouteTemplateForm, PatrolRouteTemplateBo.class);
            patrolRouteTemplateBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolRouteTemplateBo.setUserBy(iAuthCasApi.get().getUserName());
            patrolRouteTemplateBo.setCurr(new Date());
            //与旧路线点求交集，且数据发生改变，则更新
            List<PatrolRoutePointForm> pointForms = patrolRoutePointIds.stream().filter(s -> newPoints.stream().noneMatch(t -> t.getPatrolRoutePointId().equals(s))).map(s -> {
                PatrolRoutePointForm p = new PatrolRoutePointForm();
                p.setPatrolRoutePointId(s);
                return p;
            }).collect(Collectors.toList());
            if (!pointForms.isEmpty()) {
                iPatrolRoutePointFactory.batchDel(pointForms);
            }
            return iPatrolRouteTemplateService.edit(patrolRouteTemplateBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTemplateBo patrolRouteTemplateBo = BeanUtils.copy(patrolRouteTemplateForm, PatrolRouteTemplateBo.class);
            patrolRouteTemplateBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolRouteTemplateBo.setIsDel(RootModel.DEL);
            return iPatrolRouteTemplateService.del(patrolRouteTemplateBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
