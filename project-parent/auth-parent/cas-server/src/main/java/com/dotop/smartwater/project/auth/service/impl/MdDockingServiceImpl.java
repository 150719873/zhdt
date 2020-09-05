package com.dotop.smartwater.project.auth.service.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.auth.dao.IMdDockingDao;
import com.dotop.smartwater.project.auth.service.IMdDockingService;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingBo;
import com.dotop.smartwater.project.module.core.auth.dto.MdDockingDto;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingVo;
import com.dotop.smartwater.project.module.core.auth.vo.md.ConfigListBaseVo;
import com.dotop.smartwater.project.module.core.auth.vo.md.ConfigListVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**

 */
@Component
public class MdDockingServiceImpl implements IMdDockingService {

    private static final Logger LOGGER = LogManager.getLogger(MdDockingServiceImpl.class);

    @Autowired
    private IMdDockingDao iMdDockingDao;

    @Override
    public Pagination<MdDockingVo> page(MdDockingBo bo) {
        try {
            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
            List<MdDockingVo> list = iMdDockingDao.list(BeanUtils.copy(bo, MdDockingDto.class));
            // 拼接数据返回
            return new Pagination<>(bo.getPage(), bo.getPageCount(), list,
                    pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public MdDockingVo get(MdDockingBo bo) {
        try {
            MdDockingVo vo = iMdDockingDao.get(BeanUtils.copy(bo, MdDockingDto.class));
            return vo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public MdDockingVo add(MdDockingBo bo) {
        try {
            iMdDockingDao.add(BeanUtils.copy(bo, MdDockingDto.class));
            return BeanUtils.copy(bo, MdDockingVo.class);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public MdDockingVo edit(MdDockingBo bo) {
        try {
            iMdDockingDao.edit(BeanUtils.copy(bo, MdDockingDto.class));
            return BeanUtils.copy(bo, MdDockingVo.class);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(MdDockingBo bo) {
        try {
            MdDockingDto dto = new MdDockingDto();
            dto.setId(bo.getId());
            dto.setIsDel(1);
            iMdDockingDao.edit(dto);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<MdDockingVo> list(MdDockingBo bo) {
        try {
            List<MdDockingVo> list = iMdDockingDao.list(BeanUtils.copy(bo, MdDockingDto.class));
            return list;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void delByMdeId(String mdeId) {
        try {
            iMdDockingDao.delByMdeId(mdeId);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void delByMdeIdAndFactory(String mdeId, String factory) {
        try {
            iMdDockingDao.delByMdeIdAndFactory(mdeId,factory);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void updateByMdeId(String mdeId, Boolean isDel) {
        try {
            iMdDockingDao.updateByMdeId(mdeId, !isDel);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void save(String mdeId, List<MdDockingVo> list) {
        try {
            iMdDockingDao.delByMdeId(mdeId);
            for (MdDockingVo vo : list) {
                iMdDockingDao.add(BeanUtils.copy(vo, MdDockingDto.class));
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ConfigListVo loadConfigList(String enterpriseid,String systemCode, String modeCode) {
        try {
            List<ConfigListBaseVo> systemList = iMdDockingDao.getSystemList(systemCode);
            List<ConfigListBaseVo> productList = iMdDockingDao.getProductList(enterpriseid);
            List<ConfigListBaseVo> modeList = iMdDockingDao.getDictionaryList(modeCode);
            ConfigListVo vo = new ConfigListVo();
            vo.setModeList(modeList);
            vo.setProductList(productList);
            vo.setSystemList(systemList);
            return vo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void delByFactoryAndType(MdDockingBo mdDockingBo) {
        try {
            iMdDockingDao.delByFactoryAndType(BeanUtils.copy(mdDockingBo, MdDockingDto.class));
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
