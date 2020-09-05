package com.dotop.smartwater.view.server.dao.pipe.monitor;

import com.dotop.smartwater.view.server.core.monitor.form.LiquidLevelForm;
import com.dotop.smartwater.view.server.core.monitor.form.PondAlarmForm;
import com.dotop.smartwater.view.server.core.monitor.vo.LiquidLevelVo;
import com.dotop.smartwater.view.server.core.monitor.vo.PondAlarmVo;

import java.util.List;

public interface IMonitorDao {
    public List<LiquidLevelVo> liquidPage(LiquidLevelForm liquidLevelForm);

    public List<PondAlarmVo> pondAlarmPage(PondAlarmForm pondAlarmForm);

    void addLiquidLists(List<LiquidLevelForm> list);

    void addPondALarmLists(List<PondAlarmForm> list);
}
