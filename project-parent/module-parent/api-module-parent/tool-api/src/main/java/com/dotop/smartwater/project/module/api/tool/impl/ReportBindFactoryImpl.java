package com.dotop.smartwater.project.module.api.tool.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.api.tool.IReportBindFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.ReportBindBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.ReportBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportBindVo;
import com.dotop.smartwater.project.module.service.tool.IReportBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ReportBindFactoryImpl implements IReportBindFactory, IAuthCasClient {

    @Autowired
    private IReportBindService iReportBindService;

    @Override
    public Pagination<ReportBindVo> page(ReportBindForm reportBindForm) {
        ReportBindBo reportBindBo = BeanUtils.copy(reportBindForm, ReportBindBo.class);
        if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
            // 最高管理员admin
        } else {
            // 非admin管理员
            reportBindBo.setEnterpriseid(getEnterpriseid());
        }
        return iReportBindService.page(reportBindBo);
    }

    @Override
    public List<ReportBindVo> list(ReportBindForm reportBindForm) {
        ReportBindBo reportBindBo = BeanUtils.copy(reportBindForm, ReportBindBo.class);
        if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
            // 最高管理员admin
        } else {
            // 非admin管理员
            reportBindBo.setEnterpriseid(getEnterpriseid());
        }
        return iReportBindService.list(reportBindBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ReportBindVo add(ReportBindForm reportBindForm) {
        ReportBindBo reportBindBo = BeanUtils.copy(reportBindForm, ReportBindBo.class);
        if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
            // 最高管理员admin
        } else {
            // 非admin管理员
            reportBindBo.setEnterpriseid(getEnterpriseid());
        }
        reportBindBo.setUserBy(getAccount());
        reportBindBo.setCurr(getCurr());
        return iReportBindService.add(reportBindBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(ReportBindForm reportBindForm) {
        ReportBindBo reportBindBo = new ReportBindBo();
        reportBindBo.setBindid(reportBindForm.getBindid());
        if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
            // 最高管理员admin
        } else {
            // 非admin管理员
            reportBindBo.setEnterpriseid(getEnterpriseid());
            reportBindBo.setUserBy(getAccount());
            reportBindBo.setCurr(getCurr());
        }
        iReportBindService.del(reportBindBo);
        return null;
    }

    @Override
    public List<ReportBindVo> listByBind(ReportBindBo reportBindBo) {
        ReportBindBo rb = new ReportBindBo();
        rb.setBindids(reportBindBo.getBindids());
        if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
            // 最高管理员admin
        } else {
            rb.setEnterpriseid(getEnterpriseid());
        }
        return iReportBindService.list(reportBindBo);
    }

    @Override
    public ReportBindVo get(ReportBindForm reportBindForm) {
        ReportBindBo reportBindBo = new ReportBindBo();
        reportBindBo.setBindid(reportBindForm.getBindid());
        reportBindBo.setEnterpriseid(getEnterpriseid());
        ReportBindVo reportBindVo = iReportBindService.get(reportBindBo);
        return reportBindVo;
    }
}
