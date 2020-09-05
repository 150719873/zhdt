package com.dotop.pipe.server.scheduleEmail;

import com.dotop.pipe.web.api.factory.emailreceiver.IEmailReceiverFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Component
@RestController()
@RequestMapping("/email")
public class EmailReceiverControll implements BaseController<BaseForm> {
    private static final Logger LOGGER = LogManager.getLogger(EmailReceiverControll.class);

    private boolean flag = false;

    @Autowired
    private IEmailReceiverFactory iEmailReceiverFactory;

    @Scheduled(initialDelay = 10 * 1000, fixedRate = 300 * 1000)
    public void receive() {
        try {
            if (this.flag) {
                LOGGER.info("email 定时采集信息 康宝莱-----------");
                LOGGER.info("email 定时采集标志信息:" + this.flag);
                iEmailReceiverFactory.receive();
            }
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
        }
    }


    @PutMapping(value = "/update", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(HttpServletRequest request) {
        String sss = request.getParameter("flag");
        // this.flag = flag == "1";
        if ("0".equals(sss)) {
            this.flag = false;
        } else {
            this.flag = true;
        }
        return resp();
    }


}
