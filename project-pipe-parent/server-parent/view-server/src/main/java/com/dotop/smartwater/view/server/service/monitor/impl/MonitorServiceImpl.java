package com.dotop.smartwater.view.server.service.monitor.impl;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.monitor.form.LiquidLevelForm;
import com.dotop.smartwater.view.server.core.monitor.form.PondAlarmForm;
import com.dotop.smartwater.view.server.core.monitor.vo.LiquidLevelVo;
import com.dotop.smartwater.view.server.core.monitor.vo.PondAlarmVo;
import com.dotop.smartwater.view.server.dao.pipe.monitor.IMonitorDao;
import com.dotop.smartwater.view.server.service.monitor.IMonitorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    private IMonitorDao iMonitorDao;

    @Override
    public Pagination<LiquidLevelVo> liquidPage(LiquidLevelForm liquidLevelForm) {
        Page<Object> pageHelper = PageHelper.startPage(liquidLevelForm.getPage(), liquidLevelForm.getPageSize());
        List<LiquidLevelVo> list = iMonitorDao.liquidPage(liquidLevelForm);
        Pagination<LiquidLevelVo> pagination = new Pagination<>(liquidLevelForm.getPageSize(), liquidLevelForm.getPage());
        pagination.setData(list);
        pagination.setTotalPageSize(pageHelper.getTotal());
        return pagination;
    }

    @Override
    public Pagination<PondAlarmVo> pondAlarmPage(PondAlarmForm pondAlarmForm) {
        Page<PondAlarmVo> pageHelper = PageHelper.startPage(pondAlarmForm.getPage(), pondAlarmForm.getPageSize());
        List<PondAlarmVo> list = iMonitorDao.pondAlarmPage(pondAlarmForm);
        Pagination<PondAlarmVo> pagination = new Pagination<>(pondAlarmForm.getPageSize(), pondAlarmForm.getPage());
        pagination.setData(list);
        pagination.setTotalPageSize(pageHelper.getTotal());
        return pagination;
    }

    @Override
    public void addLiquidLists(List<LiquidLevelForm> list) {
        this.iMonitorDao.addLiquidLists(list);
    }

    @Override
    public void addPondALarmLists(List<PondAlarmForm> list) {
        this.iMonitorDao.addPondALarmLists(list);
    }
}
