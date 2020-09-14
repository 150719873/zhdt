package com.dotop.smartwater.project.third.concentrator.service.impl;


import com.dotop.smartwater.project.third.concentrator.core.dto.DownLinkTaskLogDto;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.third.concentrator.core.bo.DownLinkTaskLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.DownLinkTaskLogVo;
import com.dotop.smartwater.project.third.concentrator.dao.IDownLinkTaskLogDao;
import com.dotop.smartwater.project.third.concentrator.service.IDownLinkTaskLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("IDownLinkTaskLogService")
public class DownLinkTaskLogServiceImpl extends AuthCasClient implements IDownLinkTaskLogService {

    private final static Logger LOGGER = LogManager.getLogger(DownLinkTaskLogServiceImpl.class);

    @Autowired
    private IDownLinkTaskLogDao iDownLinkTaskLogDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = FrameworkRuntimeException.class)
    public DownLinkTaskLogVo add(DownLinkTaskLogBo downLinkTaskLogBo) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkTaskLogBo", downLinkTaskLogBo));
        DownLinkTaskLogDto downLinkTaskLogDto = BeanUtils.copy(downLinkTaskLogBo, DownLinkTaskLogDto.class);
        iDownLinkTaskLogDao.add(downLinkTaskLogDto);
        DownLinkTaskLogVo downLinkTaskLogVo = BeanUtils.copy(downLinkTaskLogBo, DownLinkTaskLogVo.class);
        return downLinkTaskLogVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = FrameworkRuntimeException.class)
    public DownLinkTaskLogVo edit(DownLinkTaskLogBo downLinkTaskLogBo) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkTaskLogBo", downLinkTaskLogBo));
        DownLinkTaskLogDto downLinkTaskLogDto = BeanUtils.copy(downLinkTaskLogBo, DownLinkTaskLogDto.class);
        iDownLinkTaskLogDao.edit(downLinkTaskLogDto);
        DownLinkTaskLogVo downLinkTaskLogVo = BeanUtils.copy(downLinkTaskLogBo, DownLinkTaskLogVo.class);
        return downLinkTaskLogVo;
    }

    @Override
    public DownLinkTaskLogVo get(DownLinkTaskLogBo downLinkTaskLogBo) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 获取下发任务列表并分页
     * @param downLinkTaskLogBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<DownLinkTaskLogVo> page(DownLinkTaskLogBo downLinkTaskLogBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            DownLinkTaskLogDto downLinkTaskLogDto = BeanUtils.copy(downLinkTaskLogBo, DownLinkTaskLogDto.class);
            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(downLinkTaskLogBo.getPage(), downLinkTaskLogBo.getPageCount());
            List<DownLinkTaskLogVo> list = iDownLinkTaskLogDao.list(downLinkTaskLogDto);
            // 拼接数据返回
            return new Pagination<>(downLinkTaskLogBo.getPage(), downLinkTaskLogBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}

