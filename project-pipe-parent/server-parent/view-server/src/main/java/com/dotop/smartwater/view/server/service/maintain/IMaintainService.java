package com.dotop.smartwater.view.server.service.maintain;


import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.maintain.form.MaintainLogForm;
import com.dotop.smartwater.view.server.core.maintain.vo.MaintainLogVo;

/**
 *
 */
public interface IMaintainService {
    Pagination<MaintainLogVo> pageMaintain(MaintainLogForm maintainLogForm) throws FrameworkRuntimeException;
}
