package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.project.third.concentrator.core.vo.DownLinkTaskLogVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.concentrator.core.form.DownLinkTaskLogForm;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 下发任务日志业务逻辑接口
 *
 *
 */
public interface IDownLinkTaskLogFactory extends BaseFactory<DownLinkTaskLogForm, DownLinkTaskLogVo> {

    @Override
    DownLinkTaskLogVo get(DownLinkTaskLogForm downLinkTaskLogForm) throws FrameworkRuntimeException;

    @Override
    Pagination<DownLinkTaskLogVo> page(DownLinkTaskLogForm downLinkTaskLogForm) throws FrameworkRuntimeException;

    XSSFWorkbook exportConcentratorRecord(DownLinkTaskLogForm downLinkTaskLogForm) throws FrameworkRuntimeException;

}
