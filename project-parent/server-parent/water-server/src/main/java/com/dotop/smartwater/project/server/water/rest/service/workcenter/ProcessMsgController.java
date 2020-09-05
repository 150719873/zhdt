package com.dotop.smartwater.project.server.water.rest.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.IProcessMsgFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DictionaryChildForm;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessMsgForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessMsgVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 工作中心流程消息管理
 *

 * @date 2019年4月17日
 * @description
 */
@RestController("WorkCenterProcessMsgController")
@RequestMapping("/workcenter/process/msg")

public class ProcessMsgController implements BaseController<WorkCenterProcessMsgForm> {

  private static final Logger logger = LogManager.getLogger(ProcessMsgController.class);

  @Autowired private IProcessMsgFactory iProcessMsgFactory;

  /** 查询分页 */
  @Override
  @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
  public String page(@RequestBody WorkCenterProcessMsgForm processMsgForm) {
    String processId = processMsgForm.getProcessId();
    Integer page = processMsgForm.getPage();
    Integer pageCount = processMsgForm.getPageCount();
    // 验证
    VerificationUtils.string("processId", processId);
    VerificationUtils.integer("page", page);
    VerificationUtils.integer("pageCount", pageCount);
    Pagination<WorkCenterProcessMsgVo> pagination = iProcessMsgFactory.page(processMsgForm);
    return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
  }

  /** 新增 */
  @Override
  @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
  public String add(@RequestBody WorkCenterProcessMsgForm processMsgForm) {
    String processId = processMsgForm.getProcessId();
    String status = processMsgForm.getStatus();
    String processNodeId = processMsgForm.getProcessNodeId();
    DictionaryChildForm handleDictChild = processMsgForm.getHandleDictChild();
    // 验证
    VerificationUtils.string("processId", processId);
    VerificationUtils.string("status", status);
    VerificationUtils.string("processNodeId", processNodeId);
    VerificationUtils.obj("handleDictChild", handleDictChild); //
    String childId = handleDictChild.getChildId();
    VerificationUtils.string("childId", childId); //
    WorkCenterProcessMsgVo add = iProcessMsgFactory.add(processMsgForm);
    return resp(ResultCode.Success, ResultCode.SUCCESS, add);
  }

  /** 文件上传 */
  // http://resource-iot.oss-cn-shanghai.aliyuncs.com/water-cas/image%5Cwork_center%5Cwork_center%5C29ac7ef508c9d4c36f957cf1990736a8
  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public String upload(
      @RequestParam("file") MultipartFile file,
      @RequestParam String processId,
      @RequestParam String type) {
    logger.info(LogMsg.to("file", file, "processId", processId, "type", type));
    if (file.getSize() > 2 * 1024 * 1024) { // 2M
      return resp(ResultCode.Fail, "上传的文件过大", null);
    }
    VerificationUtils.string("processId", processId);
    VerificationUtils.string("type", type);
    String url = iProcessMsgFactory.upload(file, processId, type);
    return resp(ResultCode.Success, ResultCode.SUCCESS, url);
  }

  /** 文件上传删除 */
  @PostMapping(value = "/upload/del", produces = GlobalContext.PRODUCES)
  public String uploadDel(@RequestBody Map<String, String> map) {
    String url = map.get("url");
    String processId = map.get("processId");
    logger.info(LogMsg.to("url", url, "processId", processId));
    VerificationUtils.string("url", url, false, 1024);
    VerificationUtils.string("processId", processId);
    iProcessMsgFactory.uploadDel(url, processId);
    return resp(ResultCode.Success, ResultCode.SUCCESS, null);
  }
}
