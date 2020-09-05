package com.dotop.pipe.service.patrol;

import com.dotop.pipe.api.dao.patrol.IPatrolRouteTemplateDao;
import com.dotop.pipe.api.service.patrol.IPatrolRouteTemplateService;
import com.dotop.pipe.core.bo.patrol.PatrolRouteTemplateBo;
import com.dotop.pipe.core.dto.patrol.PatrolRouteTemplateDto;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTemplateVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
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

import java.util.List;

/**
 *
 */
@Service
public class PatrolRouteTemplateServiceImpl implements IPatrolRouteTemplateService {

    private static final Logger logger = LogManager.getLogger(PatrolRouteTemplateServiceImpl.class);

    @Autowired
    private IPatrolRouteTemplateDao iPatrolRouteTemplateDao;


    @Override
    public PatrolRouteTemplateVo add(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTemplateDto patrolRouteTemplateDto = BeanUtils.copy(patrolRouteTemplateBo, PatrolRouteTemplateDto.class);
            if (patrolRouteTemplateDto.getPatrolRouteTemplateId() == null) {
                patrolRouteTemplateDto.setPatrolRouteTemplateId(UuidUtils.getUuid());
                iPatrolRouteTemplateDao.add(patrolRouteTemplateDto);
            } else {
                iPatrolRouteTemplateDao.edit(patrolRouteTemplateDto);
            }
            PatrolRouteTemplateVo patrolRouteTemplateVo = BeanUtils.copy(patrolRouteTemplateDto, PatrolRouteTemplateVo.class);
            return patrolRouteTemplateVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTemplateVo get(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTemplateDto patrolRouteTemplateDto = BeanUtils.copy(patrolRouteTemplateBo, PatrolRouteTemplateDto.class);
            PatrolRouteTemplateVo patrolRouteTemplateVo = iPatrolRouteTemplateDao.get(patrolRouteTemplateDto);

            return patrolRouteTemplateVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolRouteTemplateVo> page(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTemplateDto patrolRouteTemplateDto = BeanUtils.copy(patrolRouteTemplateBo, PatrolRouteTemplateDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(patrolRouteTemplateBo.getPage(), patrolRouteTemplateBo.getPageCount());
            List<PatrolRouteTemplateVo> list = iPatrolRouteTemplateDao.list(patrolRouteTemplateDto);
            // 拼接数据返回
            return new Pagination<>(patrolRouteTemplateBo.getPage(), patrolRouteTemplateBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolRouteTemplateVo> list(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTemplateDto patrolRouteTemplateDto = BeanUtils.copy(patrolRouteTemplateBo, PatrolRouteTemplateDto.class);
            // 操作数据
            List<PatrolRouteTemplateVo> list = iPatrolRouteTemplateDao.list(patrolRouteTemplateDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTemplateVo edit(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTemplateDto patrolRouteTemplateDto = BeanUtils.copy(patrolRouteTemplateBo, PatrolRouteTemplateDto.class);
            iPatrolRouteTemplateDao.edit(patrolRouteTemplateDto);
            PatrolRouteTemplateVo patrolRouteTemplateVo = BeanUtils.copy(patrolRouteTemplateDto, PatrolRouteTemplateVo.class);
            return patrolRouteTemplateVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTemplateDto patrolRouteTemplateDto = BeanUtils.copy(patrolRouteTemplateBo, PatrolRouteTemplateDto.class);
            iPatrolRouteTemplateDao.del(patrolRouteTemplateDto);
            return null;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
