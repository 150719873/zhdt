package com.dotop.smartwater.project.third.concentrator.service.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.dto.CollectorDto;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.bo.CollectorBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.project.third.concentrator.dao.ICollectorDao;
import com.dotop.smartwater.project.third.concentrator.service.ICollectorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 采集器数据获取层接口
 *
 *
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class CollectorServiceImpl implements ICollectorService {

    @Autowired
    private ICollectorDao iCollectorDao;



    @Override
    public CollectorVo add(CollectorBo collectorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);

            CollectorVo tempCollectorVo = iCollectorDao.get(collectorDto);
            if (tempCollectorVo != null){
                throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "该集中器下已经存在该采集器编号");
            }
            if (collectorDto.getId() == null) {
                collectorDto.setId(UuidUtils.getUuid());
                iCollectorDao.add(collectorDto);
            } else {
                iCollectorDao.edit(collectorDto);
            }
            CollectorVo collectorVo = BeanUtils.copy(collectorDto, CollectorVo.class);
            return collectorVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public CollectorVo get(CollectorBo collectorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);
            CollectorVo collectorVo = iCollectorDao.get(collectorDto);
            return collectorVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public CollectorVo getByCode(CollectorBo collectorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);
            return iCollectorDao.getByCode(collectorDto);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<CollectorVo> page(CollectorBo collectorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(collectorBo.getPage(), collectorBo.getPageCount());
            List<CollectorVo> list = iCollectorDao.list(collectorDto);
            // 拼接数据返回
            return new Pagination<>(collectorBo.getPage(), collectorBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<CollectorVo> list(CollectorBo collectorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);
            // 操作数据
            List<CollectorVo> list = iCollectorDao.list(collectorDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public CollectorVo edit(CollectorBo collectorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);
            //编辑校验
            CollectorDto tempCollectorDto = new CollectorDto();
            tempCollectorDto.setId(collectorDto.getId());
            tempCollectorDto.setEnterpriseid(collectorDto.getEnterpriseid());
            CollectorVo tempCollectorVo = iCollectorDao.get(tempCollectorDto);
            if (tempCollectorVo.getCode() != null && !tempCollectorVo.getCode().equals(collectorDto.getCode())){
                CollectorDto flagCollectorDto = new CollectorDto();
                flagCollectorDto.setCode(collectorDto.getCode());
                flagCollectorDto.setEnterpriseid(collectorDto.getEnterpriseid());
                ConcentratorBo concentratorBo = new ConcentratorBo();
                if (collectorDto.getConcentrator() != null) {
                    concentratorBo.setId(collectorDto.getConcentrator().getId());
                }
                flagCollectorDto.setConcentrator(concentratorBo);
                CollectorVo byCode = iCollectorDao.get(flagCollectorDto);
                if (byCode != null){
                    throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "该集中器下已经存在该采集器编号");
                }
            }

            iCollectorDao.edit(collectorDto);
            CollectorVo collectorVo = BeanUtils.copy(collectorDto, CollectorVo.class);
            return collectorVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(CollectorBo collectorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);
            iCollectorDao.del(collectorDto);
            return ResultCode.SUCCESS;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExist(CollectorBo collectorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);
            Boolean exist = iCollectorDao.isExist(collectorDto);
            return exist;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public CollectorVo editStatus(CollectorBo collectorBo) {
        try {
            // 参数转换
            CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);
            iCollectorDao.editStatus(collectorDto);
            CollectorVo collectorVo =  BeanUtils.copy(collectorDto, CollectorVo.class);
            return collectorVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Integer countCollector(String id, String code) throws FrameworkRuntimeException {
        try {
            Integer count = iCollectorDao.countCollector(id, code);
            if (count == null) {
                return 0;
            } else {
                return count;
            }
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<CollectorVo> pageArchive(CollectorBo collectorBo) throws FrameworkRuntimeException {
        // 参数转换
        CollectorDto collectorDto = BeanUtils.copy(collectorBo, CollectorDto.class);
        // 操作数据
        Page<Object> pageHelper = PageHelper.startPage(collectorBo.getPage(), collectorBo.getPageCount());
        List<CollectorVo> list = iCollectorDao.listArchive(collectorDto);
        // 拼接数据返回
        return new Pagination<>(collectorBo.getPage(), collectorBo.getPageCount(), list, pageHelper.getTotal());
    }

    @Override
    public void adds(List<CollectorBo> collectorBos) throws FrameworkRuntimeException {
        try {
            // 参数转换
            List<CollectorDto> collectorDtos = BeanUtils.copy(collectorBos, CollectorDto.class);
            iCollectorDao.adds(collectorDtos);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
