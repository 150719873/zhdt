package com.dotop.smartwater.project.third.concentrator.service.impl;

import com.dotop.smartwater.project.third.concentrator.core.dto.UpLinkLogDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.UpLinkLogVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.third.concentrator.core.bo.UpLinkLogBo;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.dao.IUpLinkLogDao;
import com.dotop.smartwater.project.third.concentrator.service.IUpLinkLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 集中器设备数据获取层接口
 *
 *
 */
@Component("IUpLinkLogService")
public class UplinkLogServiceImpl extends AuthCasClient implements IUpLinkLogService {

    @Autowired
    IUpLinkLogDao iUpLinkLogDao;

    @Override
    public UpLinkLogVo add(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException {
        return null;
    }

    @Override
    public Pagination<UpLinkLogVo> page(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            UpLinkLogDto upLinkLogDto = BeanUtils.copy(upLinkLogBo, UpLinkLogDto.class);
            upLinkLogDto.setPreMonth(DateUtils.format(DateUtils.month(new Date(), -1), DateUtils.YYYYMM));
            upLinkLogDto.setThisMonth(DateUtils.format(new Date(), DateUtils.YYYYMM));
            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(upLinkLogBo.getPage(), upLinkLogBo.getPageCount());
            List<UpLinkLogVo> list = iUpLinkLogDao.list(upLinkLogDto);
            // 拼接数据返回
            return new Pagination<>(upLinkLogBo.getPage(), upLinkLogBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<UpLinkLogVo> list(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            UpLinkLogDto upLinkLogDto = BeanUtils.copy(upLinkLogBo, UpLinkLogDto.class);
            upLinkLogDto.setPreMonth(DateUtils.format(DateUtils.month(new Date(), -1), DateUtils.YYYYMM));
            upLinkLogDto.setThisMonth(DateUtils.format(new Date(), DateUtils.YYYYMM));
            return iUpLinkLogDao.list(upLinkLogDto);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<UpLinkLogVo> getUplinkLogDateList(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException {
        UpLinkLogDto upLinkLogDto = BeanUtils.copy(upLinkLogBo, UpLinkLogDto.class);
        upLinkLogDto.setThisMonth(DateUtils.format(upLinkLogDto.getReceiveDate(), DateUtils.YYYYMM));
        upLinkLogDto.setResult(ConcentratorConstants.RESULT_SUCCESS);
        return iUpLinkLogDao.getUplinkLogDateList(upLinkLogDto);
    }

    @Override
    public List<UpLinkLogVo> getUplinkLogMonthList(UpLinkLogBo upLinkLogBo) throws FrameworkRuntimeException {
        List<UpLinkLogDto> upLinkLogDtos = new ArrayList<>();
        //TODO 需要封装成工具类
        //获取某年第一个月第一天
        DateTime dateTime = new DateTime(upLinkLogBo.getReceiveDate());
        upLinkLogBo.setReceiveDate(dateTime.withMonthOfYear(1).withDayOfMonth(1).toDate());
        for (int i = 0; i < 12; i++) {
            UpLinkLogDto upLinkLogDto = BeanUtils.copy(upLinkLogBo, UpLinkLogDto.class);
            upLinkLogDto.setResult(ConcentratorConstants.RESULT_SUCCESS);
            upLinkLogDto.setThisMonth(DateUtils.format(DateUtils.month(upLinkLogDto.getReceiveDate(), i), DateUtils.YYYYMM));
            upLinkLogDtos.add(upLinkLogDto);
        }
        return iUpLinkLogDao.getUplinkLogMonthList(upLinkLogDtos);
    }
}
