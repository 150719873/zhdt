package com.dotop.smartwater.view.server.dao.pipe.security;

import com.dotop.smartwater.view.server.core.security.vo.SecurityLogVo;
import com.dotop.smartwater.view.server.core.security.vo.SecuritySwitchVo;
import com.dotop.smartwater.view.server.core.security.form.SecurityLogForm;
import com.dotop.smartwater.view.server.core.security.form.SecuritySwitchForm;

import java.util.List;

public interface ISecurityDao {
    Integer edit(SecuritySwitchForm securitySwitchForm);

    List<SecuritySwitchVo> list(SecuritySwitchForm securitySwitchForm);

    void adds(List<SecuritySwitchForm> list);

    List<SecurityLogVo> logList(SecurityLogForm securityLogForm);

    void addLog(SecurityLogForm securityLogForm);
}
