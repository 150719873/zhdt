package com.dotop.pipe.server.rest.service.runingdata;

import com.dotop.pipe.core.form.RuningDataForm;
import com.dotop.pipe.core.vo.runingdata.RuningDataVo;
import com.dotop.pipe.server.config.TimerConfig;
import com.dotop.pipe.web.api.factory.runingdata.IRuningDataFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/runData")
public class RuningDataController implements BaseController<RuningDataForm> {

    private final static Logger logger = LogManager.getLogger(RuningDataController.class);

    @Autowired
    private IRuningDataFactory iRuningDataFactory;

    @Autowired
    private TimerConfig timerConfig;

    public RuningDataController() {
        super();
        System.out.println("TestController");
    }

    /**
     * 分页查询
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody RuningDataForm runingDataForm) {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "runingDataForm", runingDataForm));
        Integer page = runingDataForm.getPage();
        Integer pageSize = runingDataForm.getPageSize();
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        Pagination<RuningDataVo> pagination = iRuningDataFactory.page(runingDataForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(pagination);
    }


    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody RuningDataForm runingDataForm) {
        logger.info(LogMsg.to("msg:", "新增开始", "runingDataForm", runingDataForm));
        VerificationUtils.date("startDate", runingDataForm.getStartDate());
        VerificationUtils.date("startDate", runingDataForm.getEndDate());
        VerificationUtils.string("taskName", runingDataForm.getTaskName());
        VerificationUtils.string("taskDes", runingDataForm.getTaskDes());
        VerificationUtils.string("type", runingDataForm.getType());
        VerificationUtils.integer("interval", runingDataForm.getInterval());
        VerificationUtils.string("interval", runingDataForm.getProductCategory());
        VerificationUtils.strList("interval", runingDataForm.getDeviceIds());
        RuningDataVo runingDataVo = iRuningDataFactory.add(runingDataForm);
        if (!"".equals(runingDataVo.getTaskId()) && runingDataVo.getTaskId() != null) {
            runingDataForm.setEnterpriseId(runingDataVo.getEnterpriseId());
            runingDataForm.setTaskId(runingDataVo.getTaskId());
            runingDataForm.setNextStartDate(runingDataForm.getStartDate());
            timerConfig.startCron(runingDataVo.getTaskId(), runingDataVo.getStartDate(), runingDataVo.getEndDate(), runingDataVo.getInterval(), runingDataVo.getType(), runingDataForm);
        }
        logger.info(LogMsg.to("msg:", "新增结束", "runingDataForm", runingDataForm));
        return resp();
    }

    /**
     * 删除
     */
    @Override
    @DeleteMapping(value = "/{taskId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(RuningDataForm runingDataForm) {
        logger.info(LogMsg.to("msg:", "删除 开始", runingDataForm, runingDataForm));
        String taskId = runingDataForm.getTaskId();
        // 校验
        VerificationUtils.string("taskId", taskId);
        String count = iRuningDataFactory.del(runingDataForm);
        TimerConfig.stopCron(taskId);
        logger.info(LogMsg.to("msg:", "删除 结束", "更新数据", count));
        return resp();
    }

    /**
     * 修改
     */
    @Override
    @PutMapping(value = "/update", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody RuningDataForm runingDataForm) {
        logger.info(LogMsg.to("msg:", "修改 开始", runingDataForm, runingDataForm));
        String taskId = runingDataForm.getTaskId();
        // 校验
        VerificationUtils.string("taskId", taskId);
        iRuningDataFactory.edit(runingDataForm);
        TimerConfig.stopCron(taskId);
        logger.info(LogMsg.to("msg:", "修改 结束"));
        return resp();
    }
}
