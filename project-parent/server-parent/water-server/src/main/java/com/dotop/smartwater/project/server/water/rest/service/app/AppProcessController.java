package com.dotop.smartwater.project.server.water.rest.service.app;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController("AppWorkCenterProcessController")
@RequestMapping("/app/workcenter/process")

public class AppProcessController implements BaseController<WorkCenterProcessForm>, IAuthCasClient {

    @Autowired
    private IProcessFactory iProcessFactory;

    /**
     * 查看分页流程(代办、已办)
     * <p>
     * 代办 WaterConstants.WORK_CENTER_PROCESS_APPLY
     * WaterConstants.WORK_CENTER_PROCESS_HANDLE
     * WaterConstants.WORK_CENTER_PROCESS_HANG
     * <p>
     * 已办 WaterConstants.WORK_CENTER_PROCESS_OVER
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody WorkCenterProcessForm processForm) {
        List<String> statuss = processForm.getStatuss();
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
        VerificationUtils.strList("statuss", statuss);
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageCount", pageCount);
        Pagination<WorkCenterProcessVo> pagination = iProcessFactory.pageAgent(processForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

}
