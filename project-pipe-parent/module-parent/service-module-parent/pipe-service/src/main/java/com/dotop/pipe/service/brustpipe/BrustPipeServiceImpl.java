package com.dotop.pipe.service.brustpipe;

import com.dotop.pipe.api.dao.brustpipe.IBrustPipeDao;
import com.dotop.pipe.api.service.brustpipe.IBrustPipeService;
import com.dotop.pipe.core.bo.brustpipe.BrustPipeBo;
import com.dotop.pipe.core.dto.brustpipe.BrustPipeDto;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
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
public class BrustPipeServiceImpl implements IBrustPipeService {

    private static final Logger logger = LogManager.getLogger(BrustPipeServiceImpl.class);

    @Autowired
    private IBrustPipeDao iBrustPipeDao;


    @Override
    public BrustPipeVo add(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            BrustPipeDto brustPipeDto = BeanUtils.copy(brustPipeBo, BrustPipeDto.class);

            if (brustPipeDto.getBrustPipeId() == null) {
                brustPipeDto.setBrustPipeId(UuidUtils.getUuid());
                iBrustPipeDao.add(brustPipeDto);
            } else {
                iBrustPipeDao.edit(brustPipeDto);
            }
            BrustPipeVo brustPipeVo = BeanUtils.copy(brustPipeDto, BrustPipeVo.class);
            return brustPipeVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public BrustPipeVo get(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            BrustPipeDto brustPipeDto = BeanUtils.copy(brustPipeBo, BrustPipeDto.class);
            BrustPipeVo brustPipeVo = iBrustPipeDao.get(brustPipeDto);

            return brustPipeVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<BrustPipeVo> page(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            BrustPipeDto brustPipeDto = BeanUtils.copy(brustPipeBo, BrustPipeDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(brustPipeBo.getPage(), brustPipeBo.getPageCount());
            List<BrustPipeVo> list = iBrustPipeDao.list(brustPipeDto);
            // 拼接数据返回
            return new Pagination<>(brustPipeBo.getPage(), brustPipeBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<BrustPipeVo> list(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            BrustPipeDto brustPipeDto = BeanUtils.copy(brustPipeBo, BrustPipeDto.class);
            // 操作数据
            List<BrustPipeVo> list = iBrustPipeDao.list(brustPipeDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public BrustPipeVo edit(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            BrustPipeDto brustPipeDto = BeanUtils.copy(brustPipeBo, BrustPipeDto.class);
            iBrustPipeDao.edit(brustPipeDto);
            BrustPipeVo brustPipeVo = BeanUtils.copy(brustPipeDto, BrustPipeVo.class);
            return brustPipeVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            BrustPipeDto brustPipeDto = BeanUtils.copy(brustPipeBo, BrustPipeDto.class);
            iBrustPipeDao.del(brustPipeDto);
            return null;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public BrustPipeVo editStatus(BrustPipeBo brustPipeBo) {
        try {
            // 参数转换
            BrustPipeDto brustPipeDto = BeanUtils.copy(brustPipeBo, BrustPipeDto.class);
            iBrustPipeDao.editStatus(brustPipeDto);
            BrustPipeVo brustPipeVo =  BeanUtils.copy(brustPipeDto, BrustPipeVo.class);
            return brustPipeVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
