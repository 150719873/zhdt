package com.dotop.smartwater.project.server.water.rest.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.IProcessFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 工作中心流程管理
 *

 * @date 2019年4月17日
 * @description
 */
@RestController("WorkCenterProcessController")
@RequestMapping("/workcenter/process")

public class ProcessController implements BaseController<WorkCenterProcessForm>, IAuthCasClient {

    private static final Logger logger = LogManager.getLogger(ProcessController.class);

    @Autowired
    private IProcessFactory iProcessFactory;

    /**
     * 查看分页流程
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody WorkCenterProcessForm processForm) {
        String title = processForm.getTitle();
        String code = processForm.getCode();
        String applicantName = processForm.getApplicantName();
        String status = processForm.getStatus();
        Integer page = processForm.getPage();
        Integer pageCount = processForm.getPageCount();
        Date startDate = processForm.getStartDate();
        Date endDate = processForm.getEndDate();
        if (startDate == null) {
            processForm.setStartDate(DateUtils.day(getCurr(), -30));
        } else {
            processForm.setStartDate(DateUtils.day(processForm.getStartDate(), 0));
        }
        if (endDate == null) {
            processForm.setEndDate(DateUtils.day(getCurr(), 1));
        } else {
            processForm.setEndDate(DateUtils.day(processForm.getEndDate(), 1));
        }
        // 验证
        VerificationUtils.string("title", title, true);
        VerificationUtils.string("code", code, true);
        VerificationUtils.string("applicantName", applicantName, true);
        VerificationUtils.string("status", status);
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageCount", pageCount);
        Pagination<WorkCenterProcessVo> pagination = iProcessFactory.page(processForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    /**
     * 查看代办分页流程
     * <p>
     * 代办 WaterConstants.WORK_CENTER_PROCESS_APPLY
     * WaterConstants.WORK_CENTER_PROCESS_HANDLE
     */
    @PostMapping(value = "/page/agent", produces = GlobalContext.PRODUCES)
    public String pageAgent(@RequestBody WorkCenterProcessForm processForm) {
        List<String> statuss = processForm.getStatuss();
        Integer page = processForm.getPage();
        Integer pageCount = processForm.getPageCount();
        // 验证
        VerificationUtils.strList("statuss", statuss);
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageCount", pageCount);
        Pagination<WorkCenterProcessVo> pagination = iProcessFactory.pageAgent(processForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    /**
     * 根据业务模块类型过滤分页查看
     */
    @PostMapping(value = "/page/business", produces = GlobalContext.PRODUCES)
    public String pageBusiness(@RequestBody WorkCenterProcessForm processForm) {
        String businessType = processForm.getBusinessType();
        Integer page = processForm.getPage();
        Integer pageCount = processForm.getPageCount();
        // 验证
        VerificationUtils.string("businessType", businessType);
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageCount", pageCount);
        Pagination<WorkCenterProcessVo> pagination = iProcessFactory.pageBusiness(processForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    /**
     * 根据业务模块id查看
     */
    @PostMapping(value = "/get/business", produces = GlobalContext.PRODUCES)
    public String getBusiness(@RequestBody WorkCenterProcessForm processForm) {
        String businessId = processForm.getBusinessId();
        // 验证
        VerificationUtils.string("businessId", businessId);
        WorkCenterProcessVo get = iProcessFactory.getBusiness(processForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, get);
    }

}
