package com.dotop.pipe.service.patrol;

import com.dotop.pipe.api.dao.patrol.IPatrolTaskTimerDao;
import com.dotop.pipe.api.service.patrol.IPatrolTaskTimerService;
import com.dotop.pipe.core.bo.patrol.PatrolTaskTimerBo;
import com.dotop.pipe.core.dto.patrol.PatrolTaskTimerDto;
import com.dotop.pipe.core.vo.patrol.PatrolTaskTimerVo;
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
public class PatrolTaskTimerServiceImpl implements IPatrolTaskTimerService {

    private static final Logger logger = LogManager.getLogger(PatrolTaskTimerServiceImpl.class);

    @Autowired
    private IPatrolTaskTimerDao iPatrolTaskTimerDao;


    @Override
    public PatrolTaskTimerVo add(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskTimerDto patrolTaskTimerDto = BeanUtils.copy(patrolTaskTimerBo, PatrolTaskTimerDto.class);
            if (patrolTaskTimerDto.getPatrolTaskTimerId() == null) {
                patrolTaskTimerDto.setPatrolTaskTimerId(UuidUtils.getUuid());
                iPatrolTaskTimerDao.add(patrolTaskTimerDto);
            } else {
                iPatrolTaskTimerDao.edit(patrolTaskTimerDto);
            }
            PatrolTaskTimerVo patrolTaskTimerVo = BeanUtils.copy(patrolTaskTimerDto, PatrolTaskTimerVo.class);
            return patrolTaskTimerVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskTimerVo get(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskTimerDto patrolTaskTimerDto = BeanUtils.copy(patrolTaskTimerBo, PatrolTaskTimerDto.class);
            PatrolTaskTimerVo patrolTaskTimerVo = iPatrolTaskTimerDao.get(patrolTaskTimerDto);

            return patrolTaskTimerVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolTaskTimerVo> page(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskTimerDto patrolTaskTimerDto = BeanUtils.copy(patrolTaskTimerBo, PatrolTaskTimerDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(patrolTaskTimerBo.getPage(), patrolTaskTimerBo.getPageCount());
            List<PatrolTaskTimerVo> list = iPatrolTaskTimerDao.list(patrolTaskTimerDto);
            // 拼接数据返回
            return new Pagination<>(patrolTaskTimerBo.getPage(), patrolTaskTimerBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolTaskTimerVo> list(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskTimerDto patrolTaskTimerDto = BeanUtils.copy(patrolTaskTimerBo, PatrolTaskTimerDto.class);
            // 操作数据
            List<PatrolTaskTimerVo> list = iPatrolTaskTimerDao.list(patrolTaskTimerDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskTimerVo edit(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskTimerDto patrolTaskTimerDto = BeanUtils.copy(patrolTaskTimerBo, PatrolTaskTimerDto.class);
            if (patrolTaskTimerDto.getPatrolTaskTimerId() != null) {
                iPatrolTaskTimerDao.edit(patrolTaskTimerDto);
            }
            PatrolTaskTimerVo patrolTaskTimerVo = BeanUtils.copy(patrolTaskTimerDto, PatrolTaskTimerVo.class);
            return patrolTaskTimerVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskTimerDto patrolTaskTimerDto = BeanUtils.copy(patrolTaskTimerBo, PatrolTaskTimerDto.class);
            iPatrolTaskTimerDao.del(patrolTaskTimerDto);
            return null;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskTimerVo editStatus(PatrolTaskTimerBo patrolTaskTimerBo) {
        try {
            // 参数转换
            PatrolTaskTimerDto patrolTaskTimerDto = BeanUtils.copy(patrolTaskTimerBo, PatrolTaskTimerDto.class);
            iPatrolTaskTimerDao.editStatus(patrolTaskTimerDto);
            PatrolTaskTimerVo patrolTaskTimerVo = BeanUtils.copy(patrolTaskTimerDto, PatrolTaskTimerVo.class);
            return patrolTaskTimerVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
