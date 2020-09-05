package com.dotop.smartwater.view.server.api.factory.view;

import com.dotop.smartwater.view.server.core.monitor.form.LiquidLevelForm;
import com.dotop.smartwater.view.server.core.monitor.form.PondAlarmForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.monitor.vo.LiquidLevelVo;
import com.dotop.smartwater.view.server.core.monitor.vo.PondAlarmVo;

/**
 * 监控相关
 */
public interface IMonitorFactory {

    /**
     * 液位监控分页查询
     *
     * @param liquidLevelForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<LiquidLevelVo> liquidPage(LiquidLevelForm liquidLevelForm) throws FrameworkRuntimeException;

    
    /**
     * 水池预警监控
     *
     * @param pondAlarmForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<PondAlarmVo> pondAlarmPage(PondAlarmForm pondAlarmForm) throws FrameworkRuntimeException;

    /**
     * 液位模拟上报
     *
     * @param enterpriseId
     * @return
     * @throws FrameworkRuntimeException
     */
    String updateTaskLiquid(String enterpriseId) throws FrameworkRuntimeException;


    /**
     * 水池预警
     *
     * @param enterpriseId
     * @return
     * @throws FrameworkRuntimeException
     */
    String updateTaskPondALarm(String enterpriseId) throws FrameworkRuntimeException;

}
