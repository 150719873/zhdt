package com.dotop.pipe.service.patrol;

import com.dotop.pipe.api.dao.patrol.IPatrolRoutePointDao;
import com.dotop.pipe.api.service.patrol.IPatrolRoutePointService;
import com.dotop.pipe.core.bo.patrol.PatrolRoutePointBo;
import com.dotop.pipe.core.dto.patrol.PatrolRoutePointDto;
import com.dotop.pipe.core.vo.patrol.PatrolRoutePointVo;
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
public class PatrolRoutePointServiceImpl implements IPatrolRoutePointService {

    private static final Logger logger = LogManager.getLogger(PatrolRoutePointServiceImpl.class);

    @Autowired
    private IPatrolRoutePointDao iPatrolRoutePointDao;


    @Override
    public PatrolRoutePointVo add(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRoutePointDto patrolRoutePointDto = BeanUtils.copy(patrolRoutePointBo, PatrolRoutePointDto.class);
            if (patrolRoutePointDto.getPatrolRoutePointId() == null) {
                patrolRoutePointDto.setPatrolRoutePointId(UuidUtils.getUuid());
                iPatrolRoutePointDao.add(patrolRoutePointDto);
            } else {
                iPatrolRoutePointDao.edit(patrolRoutePointDto);
            }
            PatrolRoutePointVo patrolRoutePointVo = BeanUtils.copy(patrolRoutePointDto, PatrolRoutePointVo.class);
            return patrolRoutePointVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRoutePointVo get(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRoutePointDto patrolRoutePointDto = BeanUtils.copy(patrolRoutePointBo, PatrolRoutePointDto.class);
            PatrolRoutePointVo patrolRoutePointVo = iPatrolRoutePointDao.get(patrolRoutePointDto);

            return patrolRoutePointVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolRoutePointVo> page(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRoutePointDto patrolRoutePointDto = BeanUtils.copy(patrolRoutePointBo, PatrolRoutePointDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(patrolRoutePointBo.getPage(), patrolRoutePointBo.getPageCount());
            List<PatrolRoutePointVo> list = iPatrolRoutePointDao.list(patrolRoutePointDto);
            // 拼接数据返回
            return new Pagination<>(patrolRoutePointBo.getPage(), patrolRoutePointBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolRoutePointVo> list(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRoutePointDto patrolRoutePointDto = BeanUtils.copy(patrolRoutePointBo, PatrolRoutePointDto.class);
            // 操作数据
            List<PatrolRoutePointVo> list = iPatrolRoutePointDao.list(patrolRoutePointDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRoutePointVo edit(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRoutePointDto patrolRoutePointDto = BeanUtils.copy(patrolRoutePointBo, PatrolRoutePointDto.class);
            iPatrolRoutePointDao.edit(patrolRoutePointDto);
            PatrolRoutePointVo patrolRoutePointVo = BeanUtils.copy(patrolRoutePointDto, PatrolRoutePointVo.class);
            return patrolRoutePointVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            PatrolRoutePointDto patrolRoutePointDto = BeanUtils.copy(patrolRoutePointBo, PatrolRoutePointDto.class);
            iPatrolRoutePointDao.del(patrolRoutePointDto);
            return null;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void batchAdd(List<PatrolRoutePointBo> pointList) {
        List<PatrolRoutePointDto> pointDtoList = BeanUtils.copy(pointList, PatrolRoutePointDto.class);
        iPatrolRoutePointDao.batchAdd(pointDtoList);
    }

    @Override
    public void batchUpdate(List<PatrolRoutePointBo> pointList) {
        List<PatrolRoutePointDto> pointDtoList = BeanUtils.copy(pointList, PatrolRoutePointDto.class);
        iPatrolRoutePointDao.batchUpdate(pointDtoList);
    }

    @Override
    public void batchDel(List<PatrolRoutePointBo> pointList) {
        List<PatrolRoutePointDto> pointDtoList = BeanUtils.copy(pointList, PatrolRoutePointDto.class);
        iPatrolRoutePointDao.batchDel(pointDtoList);
    }
}
