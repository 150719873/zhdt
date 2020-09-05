package com.dotop.smartwater.view.server.service.alarm.impl;

import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.dto.alarm.AlarmDto;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.view.server.dao.pipe.alarm.IAlarmSummaryDao;
import com.dotop.smartwater.view.server.service.alarm.IAlarmSummaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmSummaryServiceImpl implements IAlarmSummaryService {

    @Autowired
    private IAlarmSummaryDao alarmSummaryDao;

    @Override
    public Pagination<AlarmVo> pageAlarm(AlarmBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmDto alarmDto = BeanUtils.copyProperties(alarmBo, AlarmDto.class);
            alarmDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(alarmBo.getPage(), alarmBo.getPageSize());
            List<AlarmVo> list = alarmSummaryDao.list(alarmDto);

            Pagination<AlarmVo> pagination = new Pagination<AlarmVo>(alarmBo.getPageSize(), alarmBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }
}
