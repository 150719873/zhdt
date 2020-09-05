package com.dotop.pipe.service.patrol;

import com.dotop.pipe.api.dao.patrol.IPatrolRouteDao;
import com.dotop.pipe.api.service.patrol.IPatrolRouteService;
import com.dotop.pipe.core.bo.patrol.PatrolRouteBo;
import com.dotop.pipe.core.dto.patrol.PatrolRouteDto;
import com.dotop.pipe.core.vo.patrol.PatrolRouteVo;
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

@Service
public class PatrolRouteServiceImpl implements IPatrolRouteService {

    private static final Logger logger = LogManager.getLogger(PatrolRouteServiceImpl.class);

    @Autowired
    private IPatrolRouteDao iPatrolRouteDao;


    @Override
    public PatrolRouteVo add(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteDto patrolRouteDto = BeanUtils.copy(patrolRouteBo, PatrolRouteDto.class);
            if (patrolRouteDto.getPatrolRouteId() == null) {
                patrolRouteDto.setPatrolRouteId(UuidUtils.getUuid());
                iPatrolRouteDao.add(patrolRouteDto);
            } else {
                iPatrolRouteDao.edit(patrolRouteDto);
            }
            PatrolRouteVo patrolRouteVo = BeanUtils.copy(patrolRouteDto, PatrolRouteVo.class);
            return patrolRouteVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteVo get(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteDto patrolRouteDto = BeanUtils.copy(patrolRouteBo, PatrolRouteDto.class);
            PatrolRouteVo patrolRouteVo = iPatrolRouteDao.get(patrolRouteDto);

            return patrolRouteVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolRouteVo> page(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteDto patrolRouteDto = BeanUtils.copy(patrolRouteBo, PatrolRouteDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(patrolRouteBo.getPage(), patrolRouteBo.getPageCount());
            List<PatrolRouteVo> list = iPatrolRouteDao.list(patrolRouteDto);
            // 拼接数据返回
            return new Pagination<>(patrolRouteBo.getPage(), patrolRouteBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolRouteVo> list(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteDto patrolRouteDto = BeanUtils.copy(patrolRouteBo, PatrolRouteDto.class);
            // 操作数据
            List<PatrolRouteVo> list = iPatrolRouteDao.list(patrolRouteDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteVo edit(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteDto patrolRouteDto = BeanUtils.copy(patrolRouteBo, PatrolRouteDto.class);
            iPatrolRouteDao.edit(patrolRouteDto);
            PatrolRouteVo patrolRouteVo = BeanUtils.copy(patrolRouteDto, PatrolRouteVo.class);
            return patrolRouteVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteDto patrolRouteDto = BeanUtils.copy(patrolRouteBo, PatrolRouteDto.class);
            iPatrolRouteDao.del(patrolRouteDto);
            return null;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
