package com.dotop.pipe.service.alarm;

import com.dotop.pipe.api.dao.alarm.IAlarmSettingTemplateDao;
import com.dotop.pipe.api.service.alarm.IAlarmSettingTemplateService;
import com.dotop.pipe.core.bo.alarm.AlarmSettingTemplateBo;
import com.dotop.pipe.core.dto.alarm.AlarmSettingTemplateDto;
import com.dotop.pipe.core.vo.alarm.AlarmSettingTemplateVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlarmSettingTemplateServiceImpl implements IAlarmSettingTemplateService {

    private static final Logger logger = LogManager.getLogger(AlarmSettingTemplateServiceImpl.class);

    @Autowired
    private IAlarmSettingTemplateDao iAlarmSettingDao;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AlarmSettingTemplateVo add(AlarmSettingTemplateBo alarmSettingBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmSettingTemplateDto alarmSettingDto = new AlarmSettingTemplateDto();
            BeanUtils.copyProperties(alarmSettingBo, alarmSettingDto);
            alarmSettingDto.setIsDel(isDel);
            // 查看是否存在 存在则先删除
            iAlarmSettingDao.del(alarmSettingDto);
            // 在插入
            iAlarmSettingDao.add(alarmSettingDto);

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
    public List<AlarmSettingTemplateVo> lists(AlarmSettingTemplateBo alarmSettingBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmSettingTemplateDto alarmSettingDto = new AlarmSettingTemplateDto();
            BeanUtils.copyProperties(alarmSettingBo, alarmSettingDto);
            alarmSettingDto.setIsDel(isDel);
            return iAlarmSettingDao.lists(alarmSettingDto);
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
    public Pagination<AlarmSettingTemplateVo> page(AlarmSettingTemplateBo alarmSettingBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            AlarmSettingTemplateDto alarmSettingTemplateDto = new AlarmSettingTemplateDto();
            BeanUtils.copyProperties(alarmSettingBo, alarmSettingTemplateDto);
            alarmSettingTemplateDto.setIsDel(isDel);

            Page<Object> pageHelper = PageHelper.startPage(alarmSettingBo.getPage(), alarmSettingBo.getPageSize());
            List<AlarmSettingTemplateVo> list = iAlarmSettingDao.lists(alarmSettingTemplateDto);

            Pagination<AlarmSettingTemplateVo> pagination = new Pagination<AlarmSettingTemplateVo>(alarmSettingBo.getPageSize(), alarmSettingBo.getPage());
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
    public AlarmSettingTemplateVo get(AlarmSettingTemplateBo alarmSettingBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmSettingTemplateDto alarmSettingDto = new AlarmSettingTemplateDto();
            BeanUtils.copyProperties(alarmSettingBo, alarmSettingDto);
            alarmSettingDto.setIsDel(isDel);
            AlarmSettingTemplateVo alarmSettingTemplateVo = iAlarmSettingDao.get(alarmSettingDto);
            return alarmSettingTemplateVo;
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
    public String del(AlarmSettingTemplateBo alarmSettingTemplateBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmSettingTemplateDto alarmSettingDto = new AlarmSettingTemplateDto();
            BeanUtils.copyProperties(alarmSettingTemplateBo, alarmSettingDto);
            alarmSettingDto.setIsDel(isDel);
            iAlarmSettingDao.del(alarmSettingDto);
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
