package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.tool.INoticeFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.MessageBo;
import com.dotop.smartwater.project.module.core.water.bo.NoticeBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.form.NoticeForm;
import com.dotop.smartwater.project.module.core.water.vo.NoticeVo;
import com.dotop.smartwater.project.module.core.water.vo.ReceiveObjectVo;
import com.dotop.smartwater.project.module.service.tool.IEmailService;
import com.dotop.smartwater.project.module.service.tool.INoticeService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;

/**
 * 通知管理
 *

 * @date 2019-03-06 11:29
 */
@Component
public class NoticeFactoryImpl implements INoticeFactory {

	  @Autowired private INoticeService iNoticeService;

	  @Autowired private ISmsToolService iSmsToolService;
	
	  @Autowired private IEmailService iEmailService;
	
	  @Override
	  public Pagination<NoticeVo> page(NoticeForm noticeForm) {
	    UserVo user = AuthCasClient.getUser();
	    String operEid = user.getEnterpriseid();
	    String userBy = user.getUserid();
	    String roleId = user.getRoleid();
	
	    NoticeBo noticeBo = new NoticeBo();
	    BeanUtils.copyProperties(noticeForm, noticeBo);
	    noticeBo.setEnterpriseid(operEid);
	    noticeBo.setUserBy(userBy);
	    // 用来查询收件箱中通知
	    noticeBo.setReceiveObj(roleId);
	
	    return iNoticeService.page(noticeBo);
	  }
	
	  @Override
	  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	  public Integer addNotice(NoticeForm noticeForm, String type) {
	    boolean flag = false;
	    String userBy = null;
	    String operEid = null;
	    if(type == "inner") {
	    	UserVo user = AuthCasClient.getUser();
	        userBy = user.getUserid();
	        operEid = user.getEnterpriseid();
	    }else {
	    	operEid = noticeForm.getEnterpriseid();
	    }
	    Date curr = new Date();
	    NoticeBo noticeBo = new NoticeBo();
	    BeanUtils.copyProperties(noticeForm, noticeBo);
	    noticeBo.setNoticeId(UuidUtils.getUuid());
	    noticeBo.setEnterpriseid(operEid);
	    noticeBo.setTime(curr);
	    noticeBo.setType("SEND");
    	noticeBo.setStatus("UNREAD");
	    noticeBo.setUserBy(userBy);
	    noticeBo.setCurr(curr);
	    noticeBo.setIsDel(0);
	    noticeBo.setReceiveObj(JSONUtils.toJSONString(noticeForm.getReceiveObjList()));
	    if (noticeBo.getAccessMap() != null) {
	      noticeBo.setAccess(JSONUtils.toJSONString(noticeForm.getAccessMap()));
	    }
	    // 生成批次号
	    String batchNo = String.valueOf(Config.Generator.nextId());
	
	    if (noticeBo.getReceiveWay() != null && !noticeBo.getReceiveWay().equals("")) {
	      List<UserVo> listU = new ArrayList<>();
	      if ("USER".equals(noticeBo.getReceiveWay()) || "ALL".equals(noticeBo.getReceiveWay())) {
	    	 //获取用户信息 
	    	 listU = iNoticeService.getUsers(noticeBo.getReceiveObjList(), operEid, "USER");
	      } else if ("ROLE".equals(noticeBo.getReceiveWay())) {
	        // 获取相应角色下的用户信息
	        listU = iNoticeService.getUsers(noticeBo.getReceiveObjList(), operEid, "ROLE");
	      }
	      if (!listU.isEmpty()) {
	        for (String item : noticeBo.getSendWayList()) {
	          if ("SYS".equals(item)) {
	            /*for (UserVo uv : listU) {
	              // 收信息日志为每个人
	              NoticeBo noticeReceiveBo = new NoticeBo();
	              BeanUtils.copyProperties(noticeBo, noticeReceiveBo);
	              noticeReceiveBo.setType("RECEIVE");
	              noticeReceiveBo.setStatus("UNREAD");
	              ReceiveObjectVo rov = new ReceiveObjectVo();
	              rov.setId(uv.getUserid());
	              rov.setName(uv.getName());
	              List<ReceiveObjectVo> l = new ArrayList<>();
	              l.add(rov);
	              noticeReceiveBo.setReceiveObj(JSONUtils.toJSONString(l));
//	              iNoticeService.addNotice(noticeReceiveBo);
	            }*/
	        	noticeBo.setType("RECEIVE");
	        	iNoticeService.addNotice(noticeBo);
	        	noticeBo.setNoticeId(UuidUtils.getUuid());
	        	noticeBo.setType("SEND");
	            flag = true;
	          } else if ("EMAIL".equals(item)) {
	            List<ReceiveObjectVo> listRov = new ArrayList<>();
	            for (UserVo uv : listU) {
	              ReceiveObjectVo rov = new ReceiveObjectVo();
	              rov.setId(uv.getUserid());
	              rov.setName(uv.getName());
	              rov.setContact(uv.getEmail());
	              listRov.add(rov);
	            }
	
	            List<MessageBo> list = new ArrayList<>();
	            for (ReceiveObjectVo ob : listRov) {
	              MessageBo messageBo = new MessageBo();
	              Map<String, String> map = new HashMap<>();
	              map.put("roleid", ob.getId());
	              map.put("name", ob.getName());
	              map.put("content", noticeBo.getBody());
	              messageBo.setEnterpriseid(operEid);
	              messageBo.setParams(JSONUtils.toJSONString(map));
	              messageBo.setReceiveaddress(ob.getContact());
	              messageBo.setReceiveusername(ob.getName());
	              messageBo.setTitle(noticeBo.getTitle());
	              messageBo.setContent(noticeBo.getBody());
	              messageBo.setModeltype(noticeBo.getModelType());
	              messageBo.setMessagetype(2);
	              list.add(messageBo);
	            }
	            iEmailService.sendEmail(list, batchNo);
	            iNoticeService.addNotice(noticeBo);
	            noticeBo.setNoticeId(UuidUtils.getUuid());
	        	noticeBo.setType("RECEIVE");
	            flag = true;
	          } else if ("SMS".equals(item)) {
	            Map<String, String> map = new HashMap<>();
	            for (UserVo uv : listU) {
	              map.put("roleid", uv.getRoleid());
	              map.put("name", uv.getName());
	              map.put("title", noticeBo.getTitle());
	              map.put("content", noticeBo.getBody());
	              map.put("devno", noticeBo.getBody());
	              map.put("phone", uv.getPhone());
	              iSmsToolService.sendSMS(noticeBo.getEnterpriseid(), noticeBo.getModelType(), map, batchNo);
	              flag = true;
	            }
	            iNoticeService.addNotice(noticeBo);
	            noticeBo.setNoticeId(UuidUtils.getUuid());
	        	noticeBo.setType("RECEIVE");
	          }
	        }
	      } else {
	        return 0;
	      }
	    }
	    if (flag) {
	      // 发送日志集合所有人员
	      return iNoticeService.addNotice(noticeBo);
	    } else {
	      return 0;
	    }
	  }
	
	  @Override
	  public NoticeVo get(NoticeForm noticeForm) {
	    UserVo user = AuthCasClient.getUser();
	    String operEid = user.getEnterpriseid();
	    NoticeBo noticeBo = new NoticeBo();
	    BeanUtils.copyProperties(noticeForm, noticeBo);
	    noticeBo.setEnterpriseid(operEid);
	    return iNoticeService.get(noticeBo);
	  }
	
	  @Override
	  public String del(NoticeForm noticeForm) {
	    UserVo user = AuthCasClient.getUser();
	    String operEid = user.getEnterpriseid();
	
	    NoticeBo noticeBo = new NoticeBo();
	    BeanUtils.copyProperties(noticeForm, noticeBo);
	    noticeBo.setEnterpriseid(operEid);
	    iNoticeService.del(noticeBo);
	    return noticeBo.getNoticeId();
	  }
	
	  @Override
	  public Integer revise(NoticeForm noticeForm) {
	    UserVo user = AuthCasClient.getUser();
	    String operEid = user.getEnterpriseid();
	
	    NoticeBo noticeBo = new NoticeBo();
	    BeanUtils.copyProperties(noticeForm, noticeBo);
	    noticeBo.setEnterpriseid(operEid);
	    noticeBo.setStatus(NoticeVo.NOTICE_STATUS_READ);
	    noticeBo.setUserBy(user.getUserid());
	    return iNoticeService.revise(noticeBo);
	  }
	
	  @Override
	  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	  public Integer sendNotice(NoticeBo noticeBo, String enterpriseid, String userBy, String name) {
	    // 仅发送站内信
	    Date curr = new Date();
	    noticeBo.setNoticeId(UuidUtils.getUuid());
	    noticeBo.setTime(curr);
	    noticeBo.setCurr(curr);
	    noticeBo.setEnterpriseid(enterpriseid);
	    noticeBo.setUserBy(userBy);
	    noticeBo.setIsDel(0);
	    noticeBo.setType("RECEIVE");
	    noticeBo.setStatus("UNREAD");
	    noticeBo.setSendWay(JSONUtils.toJSONString(noticeBo.getSendWayList()));
	    // 获取用户信息
	    List<UserVo> listU =
	        iNoticeService.getUsers(noticeBo.getReceiveObjList(), enterpriseid, "USER");
	    List<ReceiveObjectVo> lrov = new ArrayList<>();
	    for (int i=0;i < listU.size();i++) {
	      ReceiveObjectVo receiveObjectVo = new ReceiveObjectVo();
	      receiveObjectVo.setId(userBy);
	      receiveObjectVo.setName(name);
	      lrov.add(receiveObjectVo);
	    }
	    noticeBo.setReceiveObj(JSONUtils.toJSONString(lrov));
	    if (noticeBo.getAccessMap() != null) {
	      noticeBo.setAccess(JSONUtils.toJSONString(noticeBo.getAccessMap()));
	    }
	    // 发信日志
	    //    iNoticeService.addNotice(noticeBo);
	    // 收信日志
	    /*for (UserVo uv : listU) {
	      // 收信息日志为每个人
	      NoticeBo noticeReceiveBo = new NoticeBo();
	      BeanUtils.copyProperties(noticeBo, noticeReceiveBo);
	      noticeReceiveBo.setNoticeId(UuidUtils.getUuid());
	      noticeReceiveBo.setType("RECEIVE");
	      noticeReceiveBo.setStatus("UNREAD");
	      ReceiveObjectVo rov = new ReceiveObjectVo();
	      rov.setId(uv.getUserid());
	      rov.setName(uv.getName());
	      List<ReceiveObjectVo> l = new ArrayList<>();
	      l.add(rov);
	      noticeReceiveBo.setReceiveObj(JSONUtils.toJSONString(l));
	    }*/
	    return iNoticeService.addNotice(noticeBo);
	  }
  
}
