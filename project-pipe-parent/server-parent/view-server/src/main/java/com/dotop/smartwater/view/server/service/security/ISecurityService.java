package com.dotop.smartwater.view.server.service.security;

import com.dotop.smartwater.view.server.core.security.vo.SecurityLogVo;
import com.dotop.smartwater.view.server.core.security.vo.SecuritySwitchVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.security.form.SecurityLogForm;
import com.dotop.smartwater.view.server.core.security.form.SecuritySwitchForm;

import java.util.List;

public interface ISecurityService {
    String edit(SecuritySwitchForm securitySwitchForm);

    Pagination<SecuritySwitchVo> list(SecuritySwitchForm securitySwitchForm);

    void adds(List<SecuritySwitchForm> list);

    Pagination<SecurityLogVo> logList(SecurityLogForm securityLogForm);

    // 随机生成门禁记录
    void addLog(SecurityLogForm securityLogForm);
}
