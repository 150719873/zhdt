package com.dotop.smartwater.project.server.water.rest.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IReportBindFactory;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.ReportBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/ReportBind")
public class ReportBindController implements BaseController<ReportBindForm>, IAuthCasClient {

    @Autowired
    private IReportBindFactory iReportBindFactory;

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody ReportBindForm reportBindForm) {
        Integer page = reportBindForm.getPage();
        Integer pageCount = reportBindForm.getPageCount();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageCount", pageCount);
        Pagination<ReportBindVo> pagination = iReportBindFactory.page(reportBindForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(@RequestBody ReportBindForm reportBindForm) {
        List<ReportBindVo> list = iReportBindFactory.list(reportBindForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody ReportBindForm reportBindForm) {
        String enterpriseid = reportBindForm.getEnterpriseid();
        String reportid = reportBindForm.getReportid();
        String reportname = reportBindForm.getReportname();
        String description = reportBindForm.getDescription();
        String type = reportBindForm.getType();
        VerificationUtils.string("enterpriseid", enterpriseid);
        VerificationUtils.string("reportid", reportid);
        VerificationUtils.string("reportname", reportname);
        VerificationUtils.string("description", description, true, 1024);
        VerificationUtils.string("type", type);
        if (getUsertype() == WaterConstants.USER_TYPE_ENTERPRISE_NORMAL) {
            return resp(AuthResultCode.NoPermission, "没有权限操作", null);
        }
        ReportBindVo add = iReportBindFactory.add(reportBindForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, add);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody ReportBindForm reportBindForm) {
        String bindid = reportBindForm.getBindid();
        VerificationUtils.string("bindid", bindid);
        if (getUsertype() == WaterConstants.USER_TYPE_ENTERPRISE_NORMAL) {
            return resp(AuthResultCode.NoPermission, "没有权限操作", null);
        }
        iReportBindFactory.del(reportBindForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody ReportBindForm reportBindForm) {
        String bindid = reportBindForm.getBindid();
        VerificationUtils.string("bindid", bindid);
        ReportBindVo reportBindVo = iReportBindFactory.get(reportBindForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, reportBindVo);
    }
}
