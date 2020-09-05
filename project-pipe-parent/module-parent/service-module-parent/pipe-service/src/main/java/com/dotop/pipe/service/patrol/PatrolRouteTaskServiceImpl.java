package com.dotop.pipe.service.patrol;

import com.dotop.pipe.api.dao.patrol.IPatrolRouteTaskDao;
import com.dotop.pipe.api.service.patrol.IPatrolRouteTaskService;
import com.dotop.pipe.core.bo.patrol.PatrolRouteTaskBo;
import com.dotop.pipe.core.dto.patrol.PatrolRouteTaskDto;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTaskVo;
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
public class PatrolRouteTaskServiceImpl implements IPatrolRouteTaskService {

    private static final Logger logger = LogManager.getLogger(PatrolRouteTaskServiceImpl.class);

    @Autowired
    private IPatrolRouteTaskDao iPatrolRouteTaskDao;


    @Override
    public PatrolRouteTaskVo add(PatrolRouteTaskBo patrolRouteTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTaskDto patrolRouteTaskDto = BeanUtils.copy(patrolRouteTaskBo, PatrolRouteTaskDto.class);
            if (patrolRouteTaskDto.getPatrolRouteTaskId() == null) {
                patrolRouteTaskDto.setPatrolRouteTaskId(UuidUtils.getUuid());
                iPatrolRouteTaskDao.add(patrolRouteTaskDto);
            } else {
                iPatrolRouteTaskDao.edit(patrolRouteTaskDto);
            }
            PatrolRouteTaskVo patrolRouteTaskVo = BeanUtils.copy(patrolRouteTaskDto, PatrolRouteTaskVo.class);
            return patrolRouteTaskVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTaskVo get(PatrolRouteTaskBo patrolRouteTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTaskDto patrolRouteTaskDto = BeanUtils.copy(patrolRouteTaskBo, PatrolRouteTaskDto.class);
            PatrolRouteTaskVo patrolRouteTaskVo = iPatrolRouteTaskDao.get(patrolRouteTaskDto);

            return patrolRouteTaskVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolRouteTaskVo> page(PatrolRouteTaskBo patrolRouteTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTaskDto patrolRouteTaskDto = BeanUtils.copy(patrolRouteTaskBo, PatrolRouteTaskDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(patrolRouteTaskBo.getPage(), patrolRouteTaskBo.getPageCount());
            List<PatrolRouteTaskVo> list = iPatrolRouteTaskDao.list(patrolRouteTaskDto);
            // 拼接数据返回
            return new Pagination<>(patrolRouteTaskBo.getPage(), patrolRouteTaskBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolRouteTaskVo> list(PatrolRouteTaskBo patrolRouteTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTaskDto patrolRouteTaskDto = BeanUtils.copy(patrolRouteTaskBo, PatrolRouteTaskDto.class);
            // 操作数据
            List<PatrolRouteTaskVo> list = iPatrolRouteTaskDao.list(patrolRouteTaskDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTaskVo edit(PatrolRouteTaskBo patrolRouteTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTaskDto patrolRouteTaskDto = BeanUtils.copy(patrolRouteTaskBo, PatrolRouteTaskDto.class);
            iPatrolRouteTaskDao.edit(patrolRouteTaskDto);
            PatrolRouteTaskVo patrolRouteTaskVo = BeanUtils.copy(patrolRouteTaskDto, PatrolRouteTaskVo.class);
            return patrolRouteTaskVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolRouteTaskBo patrolRouteTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRouteTaskDto patrolRouteTaskDto = BeanUtils.copy(patrolRouteTaskBo, PatrolRouteTaskDto.class);
            iPatrolRouteTaskDao.del(patrolRouteTaskDto);
            return null;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTaskVo editStatus(PatrolRouteTaskBo patrolRouteTaskBo) {
        try {
            // 参数转换
            PatrolRouteTaskDto patrolRouteTaskDto = BeanUtils.copy(patrolRouteTaskBo, PatrolRouteTaskDto.class);
            iPatrolRouteTaskDao.editStatus(patrolRouteTaskDto);
            PatrolRouteTaskVo patrolRouteTaskVo =  BeanUtils.copy(patrolRouteTaskDto, PatrolRouteTaskVo.class);
            return patrolRouteTaskVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void batchAdd(List<PatrolRouteTaskBo> taskList) {
        List<PatrolRouteTaskDto> taskDtoList = BeanUtils.copy(taskList, PatrolRouteTaskDto.class);
        iPatrolRouteTaskDao.batchAdd(taskDtoList);
    }

    @Override
    public void batchUpdate(List<PatrolRouteTaskBo> taskList) {
        List<PatrolRouteTaskDto> taskDtoList = BeanUtils.copy(taskList, PatrolRouteTaskDto.class);
        iPatrolRouteTaskDao.batchUpdate(taskDtoList);
    }
}
