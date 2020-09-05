package com.dotop.smartwater.project.server.water.rest.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.IProcessHandleFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessHandleForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessHandleVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 工作中心流程处理管理
 *

 * @date 2019年4月17日
 * @description
 */
@RestController("WorkCenterProcessHandleController")
@RequestMapping("/workcenter/process/handle")

public class ProcessHandleController implements BaseController<WorkCenterProcessHandleForm> {

  private static final Logger logger = LogManager.getLogger(ProcessHandleController.class);

  @Autowired private IProcessHandleFactory iProcessHandleFactory;

  /** 接收模板参数调整显示新增页面 */
  @PostMapping(value = "/show", produces = GlobalContext.PRODUCES)
  public String show(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
    // String tmplId = processHandleForm.getTmplId();
    String businessId = processHandleForm.getBusinessId();
    String businessType = processHandleForm.getBusinessType();
    // Map<String, String> sqlParams = processHandleForm.getSqlParams();
    // Map<String, String> showParams = processHandleForm.getShowParams();
    // Map<String, String> fillParams = processHandleForm.getFillParams();
    // Map<String, String> carryParams = processHandleForm.getCarryParams();
    // VerificationUtils.string("tmplId", tmplId);
    VerificationUtils.string("businessId", businessId);
    VerificationUtils.string("businessType", businessType);
    // VerificationUtils.obj("sqlParams", sqlParams);
    // VerificationUtils.obj("showParams", showParams);
    // VerificationUtils.obj("fillParams", fillParams);
    // VerificationUtils.obj("carryParams", carryParams);
    WorkCenterProcessHandleVo show = iProcessHandleFactory.show(processHandleForm);
    return resp(ResultCode.Success, ResultCode.SUCCESS, show);
  }

  /** 流程模板表单信息展示sql数据源源 */
  @PostMapping(value = "/list/tmpl/db/auto", produces = GlobalContext.PRODUCES)
  public String listTmplDbAuto(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
    String businessId = processHandleForm.getBusinessId();
    String businessType = processHandleForm.getBusinessType();
    String dbId = processHandleForm.getDbId();
    Integer page = processHandleForm.getPage();
    Integer pageCount = processHandleForm.getPageCount();
    VerificationUtils.string("businessId", businessId);
    VerificationUtils.string("businessType", businessType);
    VerificationUtils.string("dbId", dbId);
    VerificationUtils.integer("page", page);
    VerificationUtils.integer("pageCount", pageCount);
    WorkCenterProcessHandleVo pagination = iProcessHandleFactory.listTmplDbAuto(processHandleForm);
    return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
  }

  /** 新增流程 */
  @Override
  @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
  public String add(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
    String title = processHandleForm.getTitle();
    // String tmplId = processHandleForm.getTmplId();
    String businessId = processHandleForm.getBusinessId();
    String businessType = processHandleForm.getBusinessType();
    // Map<String, String> sqlParams = processHandleForm.getSqlParams();
    // Map<String, String> showParams = processHandleForm.getShowParams();
    Map<String, String> fillParams = processHandleForm.getFillParams();
    // Map<String, String> carryParams = processHandleForm.getCarryParams();
    VerificationUtils.string("title", title);
    // VerificationUtils.string("tmplId", tmplId);
    VerificationUtils.string("businessId", businessId);
    VerificationUtils.string("businessType", businessType);
    // VerificationUtils.obj("sqlParams", sqlParams);
    // VerificationUtils.obj("showParams", showParams);
    VerificationUtils.obj("fillParams", fillParams);
    // VerificationUtils.obj("carryParams", carryParams);
    WorkCenterProcessHandleVo add = iProcessHandleFactory.add(processHandleForm);
    return resp(ResultCode.Success, ResultCode.SUCCESS, add);
  }

  /** 流程模板表单信息展示 */
  @Override
  @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
  public String get(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
    String processId = processHandleForm.getProcessId();
    VerificationUtils.string("processId", processId);
    WorkCenterProcessHandleVo get = iProcessHandleFactory.get(processHandleForm);
    return resp(ResultCode.Success, ResultCode.SUCCESS, get);
  }

  /** 流程模板表单信息展示sql数据源源 */
  @PostMapping(value = "/list/process/db/auto", produces = GlobalContext.PRODUCES)
  public String listProcessDbAuto(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
    String processId = processHandleForm.getProcessId();
    String processDbId = processHandleForm.getProcessDbId();
    Integer page = processHandleForm.getPage();
    Integer pageCount = processHandleForm.getPageCount();
    VerificationUtils.string("processId", processId);
    VerificationUtils.string("processDbId", processDbId);
    VerificationUtils.integer("page", page);
    VerificationUtils.integer("pageCount", pageCount);
    WorkCenterProcessHandleVo pagination =
        iProcessHandleFactory.listProcessDbAuto(processHandleForm);
    return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
  }

  /** 查看当前进程处理节点信息展示流程信息处理 */
  @PostMapping(value = "/get/curr/node", produces = GlobalContext.PRODUCES)
  public String getCurrNode(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
    String processId = processHandleForm.getProcessId();
    // 验证
    VerificationUtils.string("processId", processId);
    WorkCenterProcessHandleVo get = iProcessHandleFactory.getCurrNode(processHandleForm);
    return resp(ResultCode.Success, ResultCode.SUCCESS, get);
  }
}
