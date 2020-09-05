package com.dotop.smartwater.view.server.api.factory.view;

import com.dotop.smartwater.view.server.core.security.vo.SecurityLogVo;
import com.dotop.smartwater.view.server.core.security.vo.SecuritySwitchVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.security.form.SecurityLogForm;
import com.dotop.smartwater.view.server.core.security.form.SecuritySwitchForm;

public interface ISecurityFactory {

    Pagination<SecuritySwitchVo> list(SecuritySwitchForm securitySwitchForm) throws FrameworkRuntimeException;

    String edit(SecuritySwitchForm securitySwitchForm) throws FrameworkRuntimeException;

    void init(String enterpriseId) throws FrameworkRuntimeException;

    Pagination<SecurityLogVo> logList(SecurityLogForm securityLogForm) throws FrameworkRuntimeException;

    String updateTask(String enterpriseId) throws FrameworkRuntimeException;
}
