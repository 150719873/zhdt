package com.dotop.smartwater.view.server.service.security.impl;

import com.dotop.smartwater.view.server.core.security.vo.SecurityLogVo;
import com.dotop.smartwater.view.server.core.security.vo.SecuritySwitchVo;
import com.dotop.smartwater.view.server.service.security.ISecurityService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.security.form.SecurityLogForm;
import com.dotop.smartwater.view.server.core.security.form.SecuritySwitchForm;
import com.dotop.smartwater.view.server.dao.pipe.security.ISecurityDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SecurityServiceImpl implements ISecurityService {

    @Autowired
    private ISecurityDao iSecurityDao;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String edit(SecuritySwitchForm securitySwitchForm) {
        Integer count = iSecurityDao.edit(securitySwitchForm);
        return null;
    }

    @Override
    public Pagination<SecuritySwitchVo> list(SecuritySwitchForm securitySwitchForm) {
        Page<Object> pageHelper = PageHelper.startPage(securitySwitchForm.getPage(), securitySwitchForm.getPageSize());
        List<SecuritySwitchVo> list = iSecurityDao.list(securitySwitchForm);
        Pagination<SecuritySwitchVo> pagination = new Pagination<>(securitySwitchForm.getPageSize(), securitySwitchForm.getPage());
        pagination.setData(list);
        pagination.setTotalPageSize(pageHelper.getTotal());
        return pagination;
    }

    @Override
    public void adds(List<SecuritySwitchForm> list) {
        iSecurityDao.adds(list);
    }

    @Override
    public Pagination<SecurityLogVo> logList(SecurityLogForm securityLogForm) {
        Page<Object> pageHelper = PageHelper.startPage(securityLogForm.getPage(), securityLogForm.getPageSize());
        List<SecurityLogVo> list = iSecurityDao.logList(securityLogForm);
        Pagination<SecurityLogVo> pagination = new Pagination<>(securityLogForm.getPageSize(), securityLogForm.getPage());
        pagination.setData(list);
        pagination.setTotalPageSize(pageHelper.getTotal());
        return pagination;
    }

    @Override
    public void addLog(SecurityLogForm securityLogForm) {
        iSecurityDao.addLog(securityLogForm);
    }


}
