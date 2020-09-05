package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IReportDesignFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.ReportDesignForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportDesignVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
 * 
 * 报表展示设计Controller

 * @date 2019-07-22
 *
 */

@RestController

@RequestMapping("/reportDesign")
public class ReportDesignController extends FoundationController implements BaseController<ReportDesignForm> {
	
	@Resource
	private IReportDesignFactory iReportDesignFactory;
	
	@Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(@RequestBody ReportDesignForm reportDesignForm) {
        List<ReportDesignVo> list = iReportDesignFactory.list(reportDesignForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }
	
	@Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody ReportDesignForm reportDesignForm) {
		Integer page = reportDesignForm.getPage();
        Integer pageCount = reportDesignForm.getPageCount();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageCount", pageCount);
        Pagination<ReportDesignVo> pagination = iReportDesignFactory.page(reportDesignForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }
	
	@Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody ReportDesignForm reportDesignForm) {
        String name = reportDesignForm.getName();
        // 验证
        VerificationUtils.string("name", name);
        auditLog(OperateTypeEnum.REPORT_DESIGN, "新增报表设计", "报表设计内容", reportDesignForm.getName());
        ReportDesignVo reportDesignVo = iReportDesignFactory.add(reportDesignForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, reportDesignVo);
    }
	
	@Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody ReportDesignForm reportDesignForm) {
        String reportDesignId = reportDesignForm.getReportDesignId();
        // 验证
        VerificationUtils.string("reportDesignId", reportDesignId);
        ReportDesignVo reportDesignVo = iReportDesignFactory.get(reportDesignForm);
        if(reportDesignVo != null) {
        	return resp(ResultCode.Success, ResultCode.SUCCESS, reportDesignVo);
        }else {
        	return resp(ResultCode.Fail, "该报表已删除", null);
        }
    }
	
	@Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody ReportDesignForm reportDesignForm) {
        String reportDesignId = reportDesignForm.getReportDesignId();
        // 验证
        VerificationUtils.string("reportDesignId", reportDesignId);
        
        auditLog(OperateTypeEnum.REPORT_DESIGN, "修改报表设计", "报表设计ID", reportDesignId);
        ReportDesignVo reportDesignVo = iReportDesignFactory.edit(reportDesignForm);
        if(reportDesignVo != null) {
        	return resp(ResultCode.Success, ResultCode.SUCCESS, reportDesignVo);
        }else {
        	return resp(ResultCode.Fail, "编辑失败", null);
        }
    }
	
	@Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody ReportDesignForm reportDesignForm) {
        String reportDesignId = reportDesignForm.getReportDesignId();
        // 验证
        VerificationUtils.string("reportDesignId", reportDesignId);
        auditLog(OperateTypeEnum.REPORT_DESIGN, "删除报表设计", "报表设计ID", reportDesignId);
        String result = iReportDesignFactory.del(reportDesignForm);
        if(result == "success") {
        	return resp(ResultCode.Success, ResultCode.SUCCESS, null);
        }else {
        	return resp(ResultCode.Fail, "删除失败", null);
        }
    }
}
