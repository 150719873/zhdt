package com.dotop.smartwater.project.server.pay.rest.service.pay;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.pay.IPayFactory;
import com.dotop.smartwater.project.module.core.pay.vo.PushVo;
import com.dotop.smartwater.project.module.core.pay.wechat.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.dotop.smartwater.project.module.core.pay.wechat.WXPayUtil.mapToXml;
import static com.dotop.smartwater.project.module.core.pay.wechat.WXPayUtil.parseXml;

/**
 * 微信回调通知url
 *

 * @date 2019年3月22日
 */
@RestController

@RequestMapping("/callback")
public class PayCallBackController implements BaseController<BaseForm> {

    private static final Logger logger = LogManager.getLogger(PayCallBackController.class);

    @Autowired
    private IPayFactory iPayFactory;

    @PostMapping(value = "/weChatPay", produces = "application/xml;charset=UTF-8")
    @ResponseBody
    public String weChatPayCallBack(HttpServletRequest request) throws Exception {
        logger.info(LogMsg.to("msg:", "微信支付回调功能开始"));
        Map<String, String> xmlResultMap = new HashMap<>(2);
        InputStream inputStream;
        try {
            inputStream = request.getInputStream();
            Map<String, String> xmlMap = parseXml(inputStream);
            logger.info("[Weixin]微信支付回调 --> {}", xmlMap);

            iPayFactory.handleCallBack(xmlMap);

        } catch (IOException e) {
            logger.error("IOException", e);
            xmlResultMap.put(MessageManager.RESP_RETURN_CODE,MessageManager.FAIL);
            xmlResultMap.put(MessageManager.RESP_RETURN_MSG,"IOException");
            logger.info(LogMsg.to("msg:", "微信支付回调出错"));
            return mapToXml(xmlResultMap);


        } catch (Exception e) {
            logger.error("Exception", e);
            xmlResultMap.put(MessageManager.RESP_RETURN_CODE,MessageManager.FAIL);
            xmlResultMap.put(MessageManager.RESP_RETURN_MSG,"Exception");
            logger.info(LogMsg.to("msg:", "微信支付回调出错"));
            return mapToXml(xmlResultMap);
        }


        xmlResultMap.put(MessageManager.RESP_RETURN_CODE,MessageManager.SUCCESS);
        xmlResultMap.put(MessageManager.RESP_RETURN_MSG,MessageManager.OK);
        logger.info(LogMsg.to("msg:", "微信支付回调功能结束"));
        return mapToXml(xmlResultMap);
    }

    @PostMapping(value = "/test", produces = GlobalContext.PRODUCES)
    public String test(@RequestBody PushVo pushVo) {
        logger.info(LogMsg.to("msg:", "推送测试功能开始"));
        logger.info("支付平台推送过来的信息 --> {}", JSONUtils.toJSONString(pushVo));
        logger.info(LogMsg.to("msg:", "推送测试功能结束"));
        return "0";
    }
}
