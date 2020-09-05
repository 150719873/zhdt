package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.tool.IMessageFactory;
import com.dotop.smartwater.project.module.core.water.constants.NumRuleSetCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.MessageForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.MessageVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
 * 消息中心
 * 

 * @date 2019-03-07 10:02
 *
 */
@RestController

@RequestMapping("/message")
public class MessageController extends FoundationController implements BaseController<MessageForm> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private IMessageFactory iMessageFactory;

	@Autowired
	private INumRuleSetFactory iNumRuleFactory;

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody MessageForm messageForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", messageForm));
		Integer page = messageForm.getPage();
		Integer pageCount = messageForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<MessageVo> pagination = iMessageFactory.page(messageForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@PostMapping(value = "/sendMessage", produces = GlobalContext.PRODUCES)
	public String sendMessage(@RequestBody List<MessageForm> list_messageForm) {
		LOGGER.info(LogMsg.to("msg:", " 补发通知开始", "list_messageForm", list_messageForm));

		if (list_messageForm.isEmpty()) {
			return resp(ResultCode.Fail, "获取消息失败！", null);
		}
		for (MessageForm messageForm : list_messageForm) {
			// 数据校验
			Integer modelType = messageForm.getModeltype();
			Integer messageType = messageForm.getMessagetype();
			String receiveAddress = messageForm.getReceiveaddress();
			// 验证
			VerificationUtils.integer("modelType", modelType);
			VerificationUtils.integer("MessageType", messageType);
			VerificationUtils.string("receiveAddress", receiveAddress);
		}
		Integer flag = 0;
		MakeNumRequest mnr = new MakeNumRequest();
		mnr.setCount(1);
		mnr.setRuleid(NumRuleSetCode.BATCH_NUM_SET);
		String batchNo = iNumRuleFactory.makeNo(mnr).getNumbers().get(0);
		for (MessageForm messageForm : list_messageForm) {
			messageForm.setBatchNo(batchNo);
			flag += iMessageFactory.sendMessage(messageForm);
		}

		if (flag == list_messageForm.size()) {
			LOGGER.info(LogMsg.to("msg:", " 补发通知结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "FAIL", null);
		}
	}

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody MessageForm messageForm) {
		LOGGER.info(LogMsg.to("msg:", "新增功能开始", messageForm));
		Integer messageType = messageForm.getMessagetype();
		Integer modelType = messageForm.getModeltype();
		String sendAddress = messageForm.getSendaddress();
		String receiveUsername = messageForm.getReceiveusername();
		String receiveAddress = messageForm.getReceiveaddress();
		String title = messageForm.getTitle();
		String content = messageForm.getContent();
		String params = messageForm.getParams();
		// 验证
		VerificationUtils.integer("modelType", modelType);
		VerificationUtils.integer("MessageType", messageType);
		VerificationUtils.string("sendAddress", sendAddress);
		VerificationUtils.string("receiveUsername", receiveUsername);
		VerificationUtils.string("receiveAddress", receiveAddress);
		VerificationUtils.string("title", title);
		VerificationUtils.string("content", content);
		VerificationUtils.string("params", params);
		auditLog(OperateTypeEnum.MESSAGE_CENTER, "新增消息", "消息内容", messageForm);
		Integer count = iMessageFactory.addMessage(messageForm);
		if (count == 1) {
			LOGGER.info(LogMsg.to("msg:", "新增功能结束", messageForm));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "FAIL", null);
		}
	}
}
