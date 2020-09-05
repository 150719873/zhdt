package com.dotop.smartwater.view.server.service.monitor;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.monitor.form.LiquidLevelForm;
import com.dotop.smartwater.view.server.core.monitor.form.PondAlarmForm;
import com.dotop.smartwater.view.server.core.monitor.vo.LiquidLevelVo;
import com.dotop.smartwater.view.server.core.monitor.vo.PondAlarmVo;

import java.util.List;

public interface IMonitorService {

    Pagination<LiquidLevelVo> liquidPage(LiquidLevelForm liquidLevelForm);

    Pagination<PondAlarmVo> pondAlarmPage(PondAlarmForm pondAlarmForm);

    void addLiquidLists(List<LiquidLevelForm> list);

    void addPondALarmLists(List<PondAlarmForm> list);
}
