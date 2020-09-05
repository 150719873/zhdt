package com.dotop.smartwater.project.module.api.workcenter.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.workcenter.IProcessMsgFactory;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.api.tool.INoticeFactory;
import com.dotop.smartwater.project.module.client.third.IOssService;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.OssPathCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DictionaryChildForm;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessMsgForm;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.service.workcenter.IProcessMsgService;
import com.dotop.smartwater.project.module.service.workcenter.IProcessNodeService;
import com.dotop.smartwater.project.module.service.workcenter.IProcessService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("IProcessMsgFactory")
public class ProcessMsgFactoryImpl implements IProcessMsgFactory, IAuthCasClient {

  private static final Logger logger = LogManager.getLogger(ProcessMsgFactoryImpl.class);

  @Autowired private IProcessService iProcessService;

  @Autowired private IProcessNodeService iProcessNodeService;

  @Autowired private IProcessMsgService iProcessMsgService;

  // @Autowired
  // private IDictionaryChildService iDictionaryChildService;

  @Resource(name = "IWorkCenterFeedbackFactoryMap")
  private Map<String, IWorkCenterFeedbackFactory> iWorkCenterFeedbackFactoryMap;

  @Autowired private IOssService iOssService;

  // 该功能分布式可以改为fegin调用，脱离业务系统
  @Autowired private INoticeFactory iNoticeFactory;

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
  public WorkCenterProcessMsgVo add(WorkCenterProcessMsgForm processMsgForm)
      throws FrameworkRuntimeException {
    String processId = processMsgForm.getProcessId();
    String processNodeId = processMsgForm.getProcessNodeId();
    DictionaryChildForm handleDictChildForm = processMsgForm.getHandleDictChild();
    String status = processMsgForm.getStatus();
    // 查询流程
    WorkCenterProcessBo processBo = new WorkCenterProcessBo();
    processBo.setId(processId);
    processBo.setEnterpriseid(getEnterpriseid());
    WorkCenterProcessVo processVo = iProcessService.get(processBo);
    if (processVo == null) {
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程不存在");
    }
    // 流程验证
    if (WaterConstants.WORK_CENTER_PROCESS_HANDLE.equals(status)
        && WaterConstants.WORK_CENTER_PROCESS_HANG.equals(processVo.getStatus())) {
      // 流程挂起处理
      if (!getUserid().equals(processVo.getAssignHandler())) {
        throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程已挂起");
      }
    } else if (WaterConstants.WORK_CENTER_PROCESS_RETURN.equals(status)
        && WaterConstants.WORK_CENTER_PROCESS_HANG.equals(processVo.getStatus())) {
      // 流程挂起退回
      if (!getUserid().equals(processVo.getAssignHandler())) {
        throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程已挂起");
      }
    } else if (WaterConstants.WORK_CENTER_PROCESS_HANDLE.equals(status)
        && WaterConstants.WORK_CENTER_PROCESS_RETURN.equals(processVo.getStatus())) {
      // 流程退回处理
      if (!getUserid().equals(processVo.getApplicant())) {
        throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程已退回");
      }
    } else if (WaterConstants.WORK_CENTER_PROCESS_OVER.equals(processVo.getStatus())) {
      // 流程已结束
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程已结束！");
    } else if (WaterConstants.WORK_CENTER_PROCESS_HANG.equals(processVo.getStatus())) {
      // 流程已挂起
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程已挂起");
    } else if (WaterConstants.WORK_CENTER_PROCESS_RETURN.equals(processVo.getStatus())) {
      // 流程已退回
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程已退回！");
    }
    // 通知业务模块
    IWorkCenterFeedbackFactory iWorkCenterFeedbackFactory =
        iWorkCenterFeedbackFactoryMap.get(processVo.getBusinessType());
    if (iWorkCenterFeedbackFactory == null) {
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块不存在");
    }
    // 获取当前正在处理流程节点信息
    List<WorkCenterProcessNodeVo> processNodeVos = processVo.getProcessNodes();
    Map<String, WorkCenterProcessNodeVo> processNodeVoMap =
        processNodeVos.stream()
            .collect(Collectors.toMap(WorkCenterProcessNodeVo::getId, a -> a, (k1, k2) -> k1));
    String currNextProcessNodeId = processVo.getNextProcessNodeId(); // 需要比较相同processNodeId
    if (!processNodeId.equals(currNextProcessNodeId)) {
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "当前正在处理不是同一个流程节点");
    }
    // 判断获取当前流程节点流程
    WorkCenterProcessNodeVo currProcessNodeVo = processNodeVoMap.get(processNodeId);
    if (currProcessNodeVo == null) {
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "不存在当前处理节点");
    }
    // 校验处理意见
    if (WaterConstants.WORK_CENTER_NODE_USE.equals(currProcessNodeVo.getIfOpinion())) {
      String opinionContent = processMsgForm.getOpinionContent();
      String opinionNum = currProcessNodeVo.getOpinionNum();
      if (opinionContent != null && Integer.valueOf(opinionNum) < opinionContent.length()) {
        throw new FrameworkRuntimeException(
            BaseExceptionConstants.BASE_ERROR, "处理内容字数不能为空或大于" + opinionNum + "字");
      } else if (opinionContent == null) {
        processMsgForm.setOpinionContent(null);
      }
    } else {
      if (WaterConstants.WORK_CENTER_PROCESS_HANG.equals(status)) {
        processMsgForm.setOpinionContent("挂起");
      } else {
        processMsgForm.setOpinionContent(null);
      }
    }
    // 校验上传图片
    if (WaterConstants.WORK_CENTER_NODE_USE.equals(currProcessNodeVo.getIfPhoto())) {
      List<String> uploadPhotos = processMsgForm.getUploadPhotos();
      String photoNum = currProcessNodeVo.getPhotoNum();
      if (uploadPhotos != null
          && !uploadPhotos.isEmpty()
          && Integer.valueOf(photoNum) < uploadPhotos.size()) {
        throw new FrameworkRuntimeException(
            BaseExceptionConstants.BASE_ERROR, "上传图片不能不上传图片或上大于" + photoNum + "张");
      } else if (uploadPhotos == null || uploadPhotos.isEmpty()) {
        processMsgForm.setUploadPhotos(null);
      }
    } else {
      processMsgForm.setUploadPhotos(null);
    }
    // 校验上传文件
    if (WaterConstants.WORK_CENTER_NODE_USE.equals(currProcessNodeVo.getIfUpload())) {
      List<String> uploadFiles = processMsgForm.getUploadFiles();
      String uploadNum = currProcessNodeVo.getUploadNum();
      if (uploadFiles != null
          && !uploadFiles.isEmpty()
          && Integer.valueOf(uploadNum) < uploadFiles.size()) {
        throw new FrameworkRuntimeException(
            BaseExceptionConstants.BASE_ERROR, "上传附件不能不上传文件或上传大于" + uploadNum + "个");
      } else if (uploadFiles == null || uploadFiles.isEmpty()) {
        processMsgForm.setUploadFiles(null);
      }
    } else {
      processMsgForm.setUploadFiles(null);
    }

    // 下个节点信息(当前节点下一个需要处理的节点信息)
    WorkCenterProcessBo processNextBo = new WorkCenterProcessBo();
    // 判断是否需要走验证路线
    WorkCenterProcessNodeVo nextProcessNodeVo = null;
    switch (status) {
      case WaterConstants.WORK_CENTER_PROCESS_HANDLE:
        String currNoVerifyProcessNodeId = currProcessNodeVo.getNoVerifyProcessNodeId();
        if (WaterConstants.WORK_CENTER_NODE_USE.equals(currProcessNodeVo.getIfVerify())) {
          String currVerifyProcessNodeId = currProcessNodeVo.getVerifyProcessNodeId();
          // 获取字典类型
          // DictionaryChildVo dictionaryChildVo = iDictionaryChildService
          // .get(BeanUtils.copy(handleDictChildForm, DictionaryChildBo.class));
          // String childValue = dictionaryChildVo.getChildValue();
          String childValue = DictionaryCode.getChildValue(handleDictChildForm.getChildId());
          // 判断获取下一个流程节点流程走向
          if (WaterConstants.DICTIONARY_1.equals(childValue)) {
            // 肯定
            nextProcessNodeVo = processNodeVoMap.get(currVerifyProcessNodeId);
          } else {
            // 否定
            nextProcessNodeVo = processNodeVoMap.get(currNoVerifyProcessNodeId);
          }
        } else {
          nextProcessNodeVo = processNodeVoMap.get(currNoVerifyProcessNodeId);
        }
        if (nextProcessNodeVo == null) {
          throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程节点异常结束");
        }
        // 提交
        // 流程修改节点为下一个节点，流程状态为处理中状态
        processNextBo.setId(processId);
        processNextBo.setCurrHandleDictChild(
            BeanUtils.copy(handleDictChildForm, DictionaryChildBo.class));
        processNextBo.setStatus(WaterConstants.WORK_CENTER_PROCESS_HANDLE);
        processNextBo.setNextProcessNodeId(nextProcessNodeVo.getId());
        processNextBo.setNextStatus(WaterConstants.WORK_CENTER_PROCESS_HANDLE);
        processNextBo.setNextHandlers(nextProcessNodeVo.getHandlers());
        processNextBo.setNextCarbonCopyers(nextProcessNodeVo.getCarbonCopyers());
        processNextBo.setNextHandlerRoles(nextProcessNodeVo.getHandlerRoles());
        processNextBo.setNextCarbonCopyerRoles(nextProcessNodeVo.getCarbonCopyerRoles());
        processNextBo.setAssignHandler(null);
        processNextBo.setAssignHandlerName(null);
        break;
      case WaterConstants.WORK_CENTER_PROCESS_HANG:
        // 挂起
        // 流程不用修改节点，流程状态为挂起状态
        // 指定处理人为挂起人
        processNextBo.setId(processId);
        processNextBo.setCurrHandleDictChild(
            BeanUtils.copy(handleDictChildForm, DictionaryChildBo.class));
        processNextBo.setStatus(WaterConstants.WORK_CENTER_PROCESS_HANG);
        processNextBo.setNextStatus(WaterConstants.WORK_CENTER_PROCESS_HANG);
        processNextBo.setNextProcessNodeId(currProcessNodeVo.getId());
        processNextBo.setNextHandlers(null);
        processNextBo.setNextCarbonCopyers(null);
        processNextBo.setNextHandlerRoles(null);
        processNextBo.setNextCarbonCopyerRoles(null);
        processNextBo.setAssignHandler(getUserid());
        processNextBo.setAssignHandlerName(getName());
        // 如果是挂起, 需要修改管漏的工单状态为挂起
        if(WaterConstants.WORK_CENTER_BUSINESS_TYPE_PIPE.equals(processVo.getBusinessType())) {
          WorkCenterFeedbackBo feedbackBo = new WorkCenterFeedbackBo();
          feedbackBo.setBusinessId(processVo.getBusinessId());
          feedbackBo.setProcessStatus(WaterConstants.WORK_CENTER_PROCESS_HANG);
          iWorkCenterFeedbackFactory.exchange(feedbackBo);
        }
        break;
      case WaterConstants.WORK_CENTER_PROCESS_RETURN:
        // 退回
        // 流程修改节点为申请，流程状态为退回中状态
        processNextBo.setStatus(WaterConstants.WORK_CENTER_PROCESS_APPLY);
        // 查询申请节点
        WorkCenterProcessNodeBo processNodeBo = new WorkCenterProcessNodeBo();
        processNodeBo.setProcessId(processId);
        processNodeBo.setName(WaterConstants.WORK_CENTER_NODE_NAME_APPLY);
        processNodeBo.setEnterpriseid(getEnterpriseid());
        WorkCenterProcessNodeVo processNodeVo = iProcessNodeService.get(processNodeBo);
        // 指定处理人为申请人
        processNextBo.setId(processId);
        processNextBo.setCurrHandleDictChild(
            BeanUtils.copy(handleDictChildForm, DictionaryChildBo.class));
        processNextBo.setNextProcessNodeId(processNodeVo.getId());
        processNextBo.setNextStatus(WaterConstants.WORK_CENTER_PROCESS_RETURN);
        processNextBo.setNextHandlers(null);
        processNextBo.setNextCarbonCopyers(null);
        processNextBo.setNextHandlerRoles(null);
        processNextBo.setNextCarbonCopyerRoles(null);
        processNextBo.setAssignHandler(processVo.getApplicant());
        processNextBo.setAssignHandlerName(processVo.getApplicantName());
        break;
      default:
        throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程状态不存在");
    }
    processNextBo.setUserBy(getName());
    processNextBo.setCurr(getCurr());
    processNextBo.setEnterpriseid(getEnterpriseid());
    iProcessService.editNext(processNextBo);
    // 处理填写意见
    WorkCenterProcessMsgBo processMsgBo =
        BeanUtils.copy(processMsgForm, WorkCenterProcessMsgBo.class);
    // 图片上传(前端保存oss返回url)
    processMsgBo.setUploadPhotos(processMsgForm.getUploadPhotos());
    // 附件上传(前端保存oss返回url)
    processMsgBo.setUploadFiles(processMsgForm.getUploadFiles());
    processMsgBo.setCompleter(getUserid());
    processMsgBo.setCompleterName(getName());
    processMsgBo.setCompleteDate(getCurr());
    processMsgBo.setHandlers(currProcessNodeVo.getHandlers());
    processMsgBo.setCarbonCopyers(currProcessNodeVo.getCarbonCopyers());
    processMsgBo.setHandlerRoles(currProcessNodeVo.getHandlerRoles());
    processMsgBo.setCarbonCopyerRoles(currProcessNodeVo.getCarbonCopyerRoles());
    processMsgBo.setNoticers(currProcessNodeVo.getNoticers());
    processMsgBo.setNoticerRoles(currProcessNodeVo.getNoticerRoles());
    processMsgBo.setEnterpriseid(getEnterpriseid());
    processMsgBo.setUserBy(getName());
    processMsgBo.setCurr(getCurr());
    WorkCenterProcessMsgVo processMsgVo = iProcessMsgService.add(processMsgBo);
    // 通知业务模块
    if (WaterConstants.WORK_CENTER_PROCESS_RETURN.equals(status)) {
      // 不发送
      // } else if (WaterConstants.WORK_CENTER_PROCESS_HANDLE.equals(status)
      // &&
      // WaterConstants.WORK_CENTER_NODE_USE.equals(currProcessNodeVo.getIfUpdate()))
      // {
    } else if (WaterConstants.WORK_CENTER_PROCESS_HANDLE.equals(status)) {
      // 发起消息通知给当前通知业务模块
      WorkCenterFeedbackBo feedbackBo = new WorkCenterFeedbackBo();
      feedbackBo.setProcessId(processId);
      feedbackBo.setProcessCode(processVo.getCode());
      feedbackBo.setBusinessId(processVo.getBusinessId());
      feedbackBo.setBusinessType(processVo.getBusinessType());
      feedbackBo.setSqlParams(processVo.getSqlParams());
      feedbackBo.setShowParams(processVo.getShowParams());
      feedbackBo.setFillParams(processVo.getFillParams());
      feedbackBo.setCarryParams(processVo.getCarryParams());
      feedbackBo.setIfVerify(currProcessNodeVo.getIfVerify());
      feedbackBo.setHandleDictChildId(handleDictChildForm.getChildId());
      feedbackBo.setHandleResult(DictionaryCode.getChildValue(handleDictChildForm.getChildId()));
      feedbackBo.setIfUpdate(currProcessNodeVo.getIfUpdate());
      if (currProcessNodeVo.getUpdateDictChild() == null) {
        feedbackBo.setUpdateDictChildId(null);
      } else {
        feedbackBo.setUpdateDictChildId(currProcessNodeVo.getUpdateDictChild().getChildId());
      }
      feedbackBo.setProcessStatus(status);
      logger.info(LogMsg.to("feedbackBo", feedbackBo));
      logger.info(LogMsg.to("msg", " 通知业务模块"));
      iWorkCenterFeedbackFactory.exchange(feedbackBo);
    }
    // TODO 发起消息通知(优化title和body)
    if (WaterConstants.WORK_CENTER_PROCESS_RETURN.equals(status)) {
      // 发起消息通知给当前申请人
      NoticeBo noticeBo = new NoticeBo();
      noticeBo.setTitle("流程：" + processVo.getTitle() + "," + "有新的消息");
      noticeBo.setBody("流程：" + processVo.getTitle() + "," + "已退回");
      noticeBo.setSendWayList(BeanUtils.list(NoticeVo.NOTICE_SENDWAY_SYS));
      noticeBo.setReceiveWay(NoticeVo.NOTICE_RECEIVEWAY_USER);
      ReceiveObjectVo receiveObject = new ReceiveObjectVo();
      receiveObject.setId(processVo.getApplicant());
      noticeBo.setReceiveObjList(BeanUtils.list(receiveObject));
      iNoticeFactory.sendNotice(noticeBo, getEnterpriseid(), getUserid(), getName());
    } else if (WaterConstants.WORK_CENTER_PROCESS_HANDLE.equals(status)
        && WaterConstants.WORK_CENTER_NODE_USE.equals(currProcessNodeVo.getIfNotice())) {
      // 发起消息通知给当前节点
      logger.info(LogMsg.to("msg", "发起消息通知"));
      NoticeBo noticeBo = new NoticeBo();
      noticeBo.setTitle("流程：" + processVo.getTitle() + "," + "有新的消息");
      noticeBo.setBody("流程：" + processVo.getTitle() + "," + "已处理");
      noticeBo.setSendWayList(BeanUtils.list(NoticeVo.NOTICE_SENDWAY_SYS));
      if (currProcessNodeVo.getNoticers() != null && !currProcessNodeVo.getNoticers().isEmpty()) {
        // 发送用户
        noticeBo.setReceiveWay(NoticeVo.NOTICE_RECEIVEWAY_USER);
        List<ReceiveObjectVo> users = new ArrayList<>();
        for (String userid : currProcessNodeVo.getNoticers()) {
          ReceiveObjectVo receiveObject = new ReceiveObjectVo();
          receiveObject.setId(userid);
          users.add(receiveObject);
        }
        noticeBo.setReceiveObjList(users);
        iNoticeFactory.sendNotice(noticeBo, getEnterpriseid(), getUserid(), getName());
      } else if (currProcessNodeVo.getNoticerRoles() != null
          && !currProcessNodeVo.getNoticerRoles().isEmpty()) {
        // 发送角色
        noticeBo.setReceiveWay(NoticeVo.NOTICE_RECEIVEWAY_ROLE);
        List<ReceiveObjectVo> roles = new ArrayList<>();
        for (String roleid : currProcessNodeVo.getNoticerRoles()) {
          ReceiveObjectVo receiveObject = new ReceiveObjectVo();
          receiveObject.setId(roleid);
          roles.add(receiveObject);
        }
        noticeBo.setReceiveObjList(roles);
        iNoticeFactory.sendNotice(noticeBo, getEnterpriseid(), getUserid(), getName());
      }
    }
    // 如果下个节点为结束节点
    // 判断是否为结束节点，如果为结束节点，状态修改为结束
    if (WaterConstants.WORK_CENTER_PROCESS_HANDLE.equals(status)
        && WaterConstants.WORK_CENTER_NODE_TYPE_END.equals(nextProcessNodeVo.getType())) {
      // 结束时间延长10秒
      Date endCurr = DateUtils.second(getCurr(), 10);
      // 流程修改节点为下一个节点，流程状态为处理为完成
      WorkCenterProcessBo processEndBo = new WorkCenterProcessBo();
      processEndBo.setId(processId);
      processEndBo.setCurrHandleDictChild(
          BeanUtils.copy(handleDictChildForm, DictionaryChildBo.class)); // TODO
      processEndBo.setStatus(WaterConstants.WORK_CENTER_PROCESS_OVER);
      processEndBo.setNextProcessNodeId(nextProcessNodeVo.getId());
      processEndBo.setNextStatus(WaterConstants.WORK_CENTER_PROCESS_OVER);
      processEndBo.setNextHandlers(nextProcessNodeVo.getHandlers());
      processEndBo.setNextCarbonCopyers(nextProcessNodeVo.getCarbonCopyers());
      processEndBo.setNextHandlerRoles(nextProcessNodeVo.getHandlerRoles());
      processEndBo.setNextCarbonCopyerRoles(nextProcessNodeVo.getCarbonCopyerRoles());
      processEndBo.setAssignHandler(getUserid());
      processEndBo.setAssignHandlerName(getName());
      processEndBo.setUserBy(getName());
      processEndBo.setCurr(endCurr);
      processEndBo.setEnterpriseid(getEnterpriseid());
      iProcessService.editNext(processEndBo);
      // 处理填写完成意见
      WorkCenterProcessMsgBo processMsgEndBo = new WorkCenterProcessMsgBo();
      processMsgEndBo.setProcessId(processId);
      processMsgEndBo.setStatus(WaterConstants.WORK_CENTER_PROCESS_OVER);
      processMsgEndBo.setProcessNodeId(processNodeId);
      processMsgEndBo.setOpinionContent(WaterConstants.WORK_CENTER_NODE_NAME_END);
      processMsgEndBo.setCompleter(getUserid());
      processMsgEndBo.setCompleterName(getName());
      processMsgEndBo.setCompleteDate(endCurr);
      processMsgEndBo.setEnterpriseid(getEnterpriseid());
      processMsgEndBo.setUserBy(getName());
      processMsgEndBo.setCurr(endCurr);
      iProcessMsgService.add(processMsgEndBo);
      // 通知业务模块
      // if
      // (WaterConstants.WORK_CENTER_NODE_USE.equals(nextProcessNodeVo.getIfUpdate()))
      // {
      WorkCenterFeedbackBo feedbackBo = new WorkCenterFeedbackBo();
      feedbackBo.setProcessId(processId);
      feedbackBo.setProcessCode(processVo.getCode());
      feedbackBo.setBusinessId(processVo.getBusinessId());
      feedbackBo.setBusinessType(processVo.getBusinessType());
      feedbackBo.setSqlParams(processVo.getSqlParams());
      feedbackBo.setShowParams(processVo.getShowParams());
      feedbackBo.setFillParams(processVo.getFillParams());
      feedbackBo.setCarryParams(processVo.getCarryParams());
      feedbackBo.setIfVerify(nextProcessNodeVo.getIfVerify());
      feedbackBo.setHandleDictChildId(handleDictChildForm.getChildId());
      feedbackBo.setHandleResult(DictionaryCode.getChildValue(handleDictChildForm.getChildId()));
      feedbackBo.setIfUpdate(nextProcessNodeVo.getIfUpdate());
      if (nextProcessNodeVo.getUpdateDictChild() == null) {
        feedbackBo.setUpdateDictChildId(null);
      } else {
        feedbackBo.setUpdateDictChildId(nextProcessNodeVo.getUpdateDictChild().getChildId());
      }
      feedbackBo.setProcessStatus(WaterConstants.WORK_CENTER_PROCESS_OVER);
      logger.info(LogMsg.to("feedbackBo", feedbackBo));
      logger.info(LogMsg.to("msg", " 通知业务模块"));
      iWorkCenterFeedbackFactory.end(feedbackBo);
      // }
      // TODO 发起消息通知(优化title和body)
      if (WaterConstants.WORK_CENTER_NODE_USE.equals(nextProcessNodeVo.getIfNotice())) {
        logger.info(LogMsg.to("msg", "发起消息通知"));
        NoticeBo noticeBo = new NoticeBo();
        noticeBo.setTitle("流程：" + processVo.getTitle() + "," + "有新的消息");
        noticeBo.setBody("流程：" + processVo.getTitle() + "," + "已完成");
        noticeBo.setSendWayList(BeanUtils.list(NoticeVo.NOTICE_SENDWAY_SYS));
        if (nextProcessNodeVo.getNoticers() != null && !nextProcessNodeVo.getNoticers().isEmpty()) {
          // 发送用户
          noticeBo.setReceiveWay(NoticeVo.NOTICE_RECEIVEWAY_USER);
          List<ReceiveObjectVo> users = new ArrayList<>();
          for (String userid : nextProcessNodeVo.getNoticers()) {
            ReceiveObjectVo receiveObject = new ReceiveObjectVo();
            receiveObject.setId(userid);
            users.add(receiveObject);
          }
          noticeBo.setReceiveObjList(users);
          iNoticeFactory.sendNotice(noticeBo, getEnterpriseid(), getUserid(), getName());
        } else if (nextProcessNodeVo.getNoticerRoles() != null
            && !nextProcessNodeVo.getNoticerRoles().isEmpty()) {
          // 发送角色
          noticeBo.setReceiveWay(NoticeVo.NOTICE_RECEIVEWAY_ROLE);
          List<ReceiveObjectVo> roles = new ArrayList<>();
          for (String roleid : nextProcessNodeVo.getNoticerRoles()) {
            ReceiveObjectVo receiveObject = new ReceiveObjectVo();
            receiveObject.setId(roleid);
            roles.add(receiveObject);
          }
          noticeBo.setReceiveObjList(roles);
          iNoticeFactory.sendNotice(noticeBo, getEnterpriseid(), getUserid(), getName());
        }
      }
    }
    return processMsgVo;
  }

  @Override
  public Pagination<WorkCenterProcessMsgVo> page(WorkCenterProcessMsgForm processMsgForm)
      throws FrameworkRuntimeException {
    WorkCenterProcessMsgBo processMsgBo =
        BeanUtils.copy(processMsgForm, WorkCenterProcessMsgBo.class);
    processMsgBo.setEnterpriseid(getEnterpriseid());
    return iProcessMsgService.page(processMsgBo);
    // 组装图片或文件前缀
    // List<WorkCenterProcessMsgVo> list = pagination.getData();
    // for (WorkCenterProcessMsgVo processMsg : list) {
    // // 图片
    // List<String> newUploadPhotos = new ArrayList<>();
    // List<String> uploadPhotos = processMsg.getUploadPhotos();
    // if (uploadPhotos != null) {
    // for (String uploadPhoto : uploadPhotos) {
    // newUploadPhotos.add(iOssService.getOssPrefix() + uploadPhoto);
    // }
    // }
    // processMsg.setUploadPhotos(newUploadPhotos);
    // // 文件
    // List<String> newUploadFiles = new ArrayList<>();
    // List<String> uploadFiles = processMsg.getUploadFiles();
    // if (uploadFiles != null) {
    // for (String uploadFile : uploadFiles) {
    // newUploadFiles.add(iOssService.getOssPrefix() + uploadFile);
    // }
    // }
    // processMsg.setUploadFiles(newUploadFiles);
    // }
  }

  @Override
  public String upload(MultipartFile file, String processId, String type)
      throws FrameworkRuntimeException {
    try {
      byte[] bytes = file.getBytes();
      // String type = OssPathCode.IMAGE;
      String model = OssPathCode.WORK_CENTER;
      String filename = file.getOriginalFilename();
      // filename = DigestUtils.md5Hex(bytes);
      String contentType = iOssService.getContentType(filename.substring(filename.lastIndexOf('.')));
      return iOssService.upLoadV2(bytes, type, model, filename, contentType);
    } catch (IOException e) {
      logger.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "文件上传异常");
    }
  }

  @Override
  public String uploadDel(String url, String processId) throws FrameworkRuntimeException {
    url = url.replace(iOssService.getOssPrefix(), "");
    try {
      url = URLDecoder.decode(url, "utf-8");
      String[] split = url.split("/");
      String type = split[0];
      String model = split[1];
      String filename = split[2];
      iOssService.del(type, model, filename);
      return null;
    } catch (UnsupportedEncodingException e) {
      logger.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "文件删除异常");
    }
  }
}
