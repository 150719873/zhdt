package com.dotop.pipe.service.patrol;

import com.dotop.pipe.api.dao.patrol.IPatrolTaskDao;
import com.dotop.pipe.api.service.patrol.IPatrolTaskService;
import com.dotop.pipe.core.bo.patrol.PatrolTaskBo;
import com.dotop.pipe.core.dto.patrol.PatrolTaskDto;
import com.dotop.pipe.core.vo.patrol.PatrolTaskVo;
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
public class PatrolTaskServiceImpl implements IPatrolTaskService {

    private static final Logger logger = LogManager.getLogger(PatrolTaskServiceImpl.class);

    @Autowired
    private IPatrolTaskDao iPatrolTaskDao;


    @Override
    public PatrolTaskVo add(PatrolTaskBo patrolTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskDto patrolTaskDto = BeanUtils.copy(patrolTaskBo, PatrolTaskDto.class);
            if (patrolTaskDto.getPatrolTaskId() == null) {
                patrolTaskDto.setPatrolTaskId(UuidUtils.getUuid());
                iPatrolTaskDao.add(patrolTaskDto);
            } else {
                iPatrolTaskDao.edit(patrolTaskDto);
            }
            PatrolTaskVo patrolTaskVo = BeanUtils.copy(patrolTaskDto, PatrolTaskVo.class);
            return patrolTaskVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskVo get(PatrolTaskBo patrolTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskDto patrolTaskDto = BeanUtils.copy(patrolTaskBo, PatrolTaskDto.class);
            PatrolTaskVo patrolTaskVo = iPatrolTaskDao.get(patrolTaskDto);

            return patrolTaskVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolTaskVo> page(PatrolTaskBo patrolTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskDto patrolTaskDto = BeanUtils.copy(patrolTaskBo, PatrolTaskDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(patrolTaskBo.getPage(), patrolTaskBo.getPageCount());
            List<PatrolTaskVo> list = iPatrolTaskDao.list(patrolTaskDto);
            // 拼接数据返回
            return new Pagination<>(patrolTaskBo.getPage(), patrolTaskBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolTaskVo> list(PatrolTaskBo patrolTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskDto patrolTaskDto = BeanUtils.copy(patrolTaskBo, PatrolTaskDto.class);
            // 操作数据
            List<PatrolTaskVo> list = iPatrolTaskDao.list(patrolTaskDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskVo edit(PatrolTaskBo patrolTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskDto patrolTaskDto = BeanUtils.copy(patrolTaskBo, PatrolTaskDto.class);
            if (patrolTaskDto.getPatrolTaskId() != null) {
                iPatrolTaskDao.edit(patrolTaskDto);
            }
            PatrolTaskVo patrolTaskVo = BeanUtils.copy(patrolTaskDto, PatrolTaskVo.class);
            return patrolTaskVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolTaskBo patrolTaskBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolTaskDto patrolTaskDto = BeanUtils.copy(patrolTaskBo, PatrolTaskDto.class);
            iPatrolTaskDao.del(patrolTaskDto);
            return null;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskVo editStatus(PatrolTaskBo patrolTaskBo) {
        try {
            // 参数转换
            PatrolTaskDto patrolTaskDto = BeanUtils.copy(patrolTaskBo, PatrolTaskDto.class);
            iPatrolTaskDao.editStatus(patrolTaskDto);
            PatrolTaskVo patrolTaskVo =  BeanUtils.copy(patrolTaskDto, PatrolTaskVo.class);
            return patrolTaskVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }


    @Override
    public void batchUpdateStatus(List<PatrolTaskBo> batchList, String enterpriseId, String status) {
        List<PatrolTaskDto> batList = BeanUtils.copy(batchList, PatrolTaskDto.class);
        iPatrolTaskDao.batchUpdateStatus(batList, enterpriseId, status);
    }
}
