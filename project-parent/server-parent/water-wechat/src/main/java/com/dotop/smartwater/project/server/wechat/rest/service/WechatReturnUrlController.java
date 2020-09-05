package com.dotop.smartwater.project.server.wechat.rest.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.api.wechat.IWechatReturnUrlFactory;

/**
 * 微信回调通知url
 * 

 * @date 2019年3月22日
 */
@RestController()

@RequestMapping("/Wechat/NotifyUrl")
public class WechatReturnUrlController implements BaseController<BaseForm> {

	private static final Logger logger = LogManager.getLogger(WechatReturnUrlController.class);

	@Autowired
	private IWechatReturnUrlFactory iWechatReturnUrlFactory;

	@PostMapping(value = "/get", produces = "application/xml;charset=UTF-8")
	@ResponseBody
	public void get(HttpServletRequest request) {
		logger.info(LogMsg.to("msg:", "微信支付回调功能开始"));
		iWechatReturnUrlFactory.get(request);
		logger.info(LogMsg.to("msg:", "微信支付回调功能结束"));
	}

}
