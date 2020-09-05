package com.dotop.pipe.service.alarm;

import com.dotop.pipe.api.dao.alarm.IAlarmNoticeRuleDao;
import com.dotop.pipe.api.service.alarm.IAlarmNoticeRuleService;
import com.dotop.pipe.core.bo.alarm.AlarmNoticeBo;
import com.dotop.pipe.core.dto.alarm.AlarmNoticeDto;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
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

import java.util.Date;
import java.util.List;

@Service
public class AlarmNoticeRuleServiceImpl implements IAlarmNoticeRuleService {

    private final static Logger logger = LogManager.getLogger(AlarmNoticeRuleServiceImpl.class);

    @Autowired
    private IAlarmNoticeRuleDao iAlarmDao;

    @Override
    public Pagination<AlarmNoticeRuleVo> page(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmNoticeDto alarmNoticeDto = BeanUtils.copyProperties(alarmBo, AlarmNoticeDto.class);
            alarmNoticeDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(alarmBo.getPage(), alarmBo.getPageSize());
            List<AlarmNoticeRuleVo> list = iAlarmDao.list(alarmNoticeDto);

            Pagination<AlarmNoticeRuleVo> pagination = new Pagination<AlarmNoticeRuleVo>(alarmBo.getPageSize(), alarmBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }


    @Override
    public AlarmNoticeRuleVo add(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmNoticeDto alarmNoticeDto = BeanUtils.copyProperties(alarmBo, AlarmNoticeDto.class);
            alarmNoticeDto.setIsDel(isDel);
            alarmNoticeDto.setId(UuidUtils.getUuid());
            iAlarmDao.add(alarmNoticeDto);
            return null;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }

    }

    @Override
    public String del(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.DEL;
            AlarmNoticeDto alarmNoticeDto = BeanUtils.copyProperties(alarmBo, AlarmNoticeDto.class);
            alarmNoticeDto.setIsDel(isDel);
            iAlarmDao.del(alarmNoticeDto);
            return null;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }


    @Override
    public Pagination<AlarmNoticeRuleVo> logPage(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmNoticeDto alarmNoticeDto = BeanUtils.copyProperties(alarmBo, AlarmNoticeDto.class);
            alarmNoticeDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(alarmBo.getPage(), alarmBo.getPageSize());
            List<AlarmNoticeRuleVo> list = iAlarmDao.logList(alarmNoticeDto);

            Pagination<AlarmNoticeRuleVo> pagination = new Pagination<AlarmNoticeRuleVo>(alarmBo.getPageSize(), alarmBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }


    @Override
    public AlarmNoticeRuleVo addLog(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmNoticeDto alarmNoticeDto = BeanUtils.copyProperties(alarmBo, AlarmNoticeDto.class);
            alarmNoticeDto.setIsDel(isDel);
            alarmNoticeDto.setId(UuidUtils.getUuid());
            alarmNoticeDto.setCurr(new Date());
            iAlarmDao.addLog(alarmNoticeDto);
            return null;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExist(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmNoticeDto alarmNoticeDto = BeanUtils.copyProperties(alarmBo, AlarmNoticeDto.class);
            alarmNoticeDto.setIsDel(isDel);
            return iAlarmDao.isExist(alarmNoticeDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public AlarmNoticeRuleVo edit(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmNoticeDto alarmNoticeDto = BeanUtils.copyProperties(alarmBo, AlarmNoticeDto.class);
            alarmNoticeDto.setIsDel(isDel);
            iAlarmDao.edit(alarmNoticeDto);
            return null;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }
}
