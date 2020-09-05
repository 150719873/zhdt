package com.dotop.smartwater.view.server.dao.pipe.device;

import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface IDeviceSummaryDao {

    List<DeviceSummaryVo> list(DeviceSummaryForm deviceSummaryForm);

    List<DeviceSummaryVo> listFactory(DeviceSummaryForm deviceSummaryForm);

    List<DeviceSummaryVo> listFactoryNotGroup(DeviceSummaryForm deviceSummaryForm);

    List<DeviceSummaryVo> listCurr(DeviceSummaryForm deviceSummaryForm);

    List<DeviceSummaryVo> listFactoryCurr(DeviceSummaryForm deviceSummaryForm);

    DeviceSummaryVo isInit(DeviceSummaryForm deviceSummaryForm);

    Integer adds(@Param("deviceSummaryForms") List<DeviceSummaryForm> deviceSummaryForms);

    Integer addCuurs(@Param("deviceSummaryForms") List<DeviceSummaryForm> deviceSummaryForms);

    Integer edits(@Param("deviceSummaryForms") List<DeviceSummaryForm> deviceSummaryForms);

    Integer dels(@Param("deviceSummaryForms") List<DeviceSummaryForm> deviceSummaryForms);

    Integer delByType(@Param("list") List<String> list, @Param("date") Date date);

    /**
     * 水厂大屏数据展示 获取当前最新的数据
     *
     * @param deviceSummaryForm
     * @return
     */
    List<DeviceSummaryVo> getCurr(DeviceSummaryForm deviceSummaryForm);


    /**
     * 分组统计数据 年 月 周
     *
     * @param deviceSummaryForm
     * @return
     */
    List<DeviceSummaryVo> listGroup(DeviceSummaryForm deviceSummaryForm);
    
    List<DeviceSummaryVo> inOutWaterListGroup(DeviceSummaryForm deviceSummaryForm);

    DeviceSummaryVo getSummary(DeviceSummaryForm deviceSummaryForm);
}
