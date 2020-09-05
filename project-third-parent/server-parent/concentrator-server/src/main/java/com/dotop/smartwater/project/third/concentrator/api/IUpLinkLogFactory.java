package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.project.third.concentrator.core.vo.UpLinkLogVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.concentrator.core.form.UpLinkLogForm;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * 上行日志业务逻辑接口
 *
 *
 */
public interface IUpLinkLogFactory extends BaseFactory<UpLinkLogForm, UpLinkLogVo> {

    /**
     * 获取设备上行数据列表并分页
     * @param upLinkLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    Pagination<UpLinkLogVo> page(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException;

    /**
     * 导出设备上行记录详情
     * @param upLinkLogForm
     * @throws FrameworkRuntimeException
     */
    XSSFWorkbook export(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException;

    /**
     * 查询每日抄表用水量
     * @param upLinkLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    List<UpLinkLogVo> getUplinkLogDateList(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException;

    List<UpLinkLogVo> getUplinkLogMonthList(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException;

    Pagination<UpLinkLogVo> upLinkJump(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException;

    XSSFWorkbook exportUpLinkJump(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException;

    XSSFWorkbook exportRecord(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException;

}
