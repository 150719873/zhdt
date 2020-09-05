package com.dotop.smartwater.project.third.concentrator.service.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorDto;
import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorFileDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorFileVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.bo.AreaBo;
import com.dotop.smartwater.project.module.core.auth.dto.AreaDto;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorFileBo;
import com.dotop.smartwater.project.third.concentrator.dao.IConcentratorDao;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 集中器/中继器数据获取层接口
 *
 *
 */
@Component("IConcentratorService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class ConcentratorServiceImpl implements IConcentratorService {

    @Autowired
    private IConcentratorDao iConcentratorDao;

    @Override
    public ConcentratorVo add(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            ConcentratorVo tempConcentratorVo = iConcentratorDao.get(concentratorDto);
            if (tempConcentratorVo != null){
                throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "集中器编号已经存在");
            }

            if (concentratorDto.getId() == null) {
                concentratorDto.setId(UuidUtils.getUuid());
                iConcentratorDao.add(concentratorDto);
            } else {
                iConcentratorDao.edit(concentratorDto);
            }
            ConcentratorVo concentratorVo = BeanUtils.copy(concentratorDto, ConcentratorVo.class);
            return concentratorVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ConcentratorVo get(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            return iConcentratorDao.get(concentratorDto);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ConcentratorVo getByCode(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            return iConcentratorDao.getByCode(concentratorDto);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<ConcentratorVo> page(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(concentratorBo.getPage(), concentratorBo.getPageCount());
            List<ConcentratorVo> list = iConcentratorDao.list(concentratorDto);
            // 拼接数据返回
            return new Pagination<>(concentratorBo.getPage(), concentratorBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<ConcentratorVo> list(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            // 操作数据
            List<ConcentratorVo> list = iConcentratorDao.list(concentratorDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ConcentratorVo edit(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            ConcentratorDto tempConcentratorDto = new ConcentratorDto();
            tempConcentratorDto.setId(concentratorDto.getId());
            tempConcentratorDto.setEnterpriseid(concentratorDto.getEnterpriseid());
            ConcentratorVo tempConcentratorVo = iConcentratorDao.get(tempConcentratorDto);
            if (tempConcentratorVo.getCode() != null && !tempConcentratorVo.getCode().equals(concentratorDto.getCode())){
                ConcentratorDto flagConcentratorDto = new ConcentratorDto();
                flagConcentratorDto.setCode(concentratorDto.getCode());
                flagConcentratorDto.setEnterpriseid(concentratorDto.getEnterpriseid());
                ConcentratorVo byCode = iConcentratorDao.get(flagConcentratorDto);
                if (byCode != null){
                    throw  new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "该集中器编号已经存在");
                }

            }

            iConcentratorDao.edit(concentratorDto);
            ConcentratorVo concentratorVo = BeanUtils.copy(concentratorDto, ConcentratorVo.class);
            return concentratorVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            iConcentratorDao.del(concentratorDto);
            return ResultCode.SUCCESS;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExist(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            Boolean exist = iConcentratorDao.isExist(concentratorDto);
            return exist;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ConcentratorVo editStatus(ConcentratorBo concentratorBo) {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            iConcentratorDao.editStatus(concentratorDto);
            ConcentratorVo concentratorVo = BeanUtils.copy(concentratorDto, ConcentratorVo.class);
            return concentratorVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<ConcentratorVo> pageArchive(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(concentratorBo.getPage(), concentratorBo.getPageCount());
            List<ConcentratorVo> list = iConcentratorDao.listArchive(concentratorDto);
            // 拼接数据返回
            return new Pagination<>(concentratorBo.getPage(), concentratorBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public int editReordering(ConcentratorBo concentratorBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDto concentratorDto = BeanUtils.copy(concentratorBo, ConcentratorDto.class);
            return iConcentratorDao.edit(concentratorDto);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<ConcentratorFileVo> listFile(ConcentratorFileBo concentratorFileBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorFileDto concentratorFileDto = BeanUtils.copy(concentratorFileBo, ConcentratorFileDto.class);
            return iConcentratorDao.listFile(concentratorFileDto);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void adds(List<ConcentratorBo> concentratorBos) throws FrameworkRuntimeException {
        try {
            List<ConcentratorDto> concentratorDtos = BeanUtils.copy(concentratorBos, ConcentratorDto.class);
            iConcentratorDao.adds(concentratorDtos);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<AreaVo> getAreaList(AreaBo areaBo) throws FrameworkRuntimeException {
        try {
            AreaDto areaDto = BeanUtils.copy(areaBo, AreaDto.class);
            return iConcentratorDao.getAreaList(areaDto);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

}
