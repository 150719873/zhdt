package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.IDownLinkFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorForm;
import com.dotop.smartwater.project.third.concentrator.core.form.DownLinkForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/test")
public class TestController implements BaseController<DownLinkForm> {

    private final static Logger LOGGER = LogManager.getLogger(TestController.class);

    @Autowired
    private IDownLinkFactory iDownLinkFactory;

    @Override
    @GetMapping(value = "/{enterpriseId}", produces = GlobalContext.PRODUCES)
    public String get(DownLinkForm downLinkForm) {
        LOGGER.info(LogMsg.to(downLinkForm, downLinkForm));
        ConcentratorForm concentratorForm = new ConcentratorForm();
        concentratorForm.setCode("440500132");
        downLinkForm.setConcentrator(concentratorForm);
        iDownLinkFactory.readDate(downLinkForm);
        return resp(downLinkForm.getEnterpriseid(), new DateTime().toString("yyyy-MM-dd HH:mm:ss"), null);
    }

    @Override
    @GetMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(DownLinkForm downLinkForm) {
        LOGGER.info(LogMsg.to(downLinkForm, downLinkForm));
        ConcentratorForm concentratorForm = new ConcentratorForm();
        concentratorForm.setCode("440500132");
        downLinkForm.setConcentrator(concentratorForm);
        iDownLinkFactory.setGprs(downLinkForm);
        return resp(downLinkForm.getEnterpriseid(), new DateTime().toString("yyyy-MM-dd HH:mm:ss"), null);
    }

}
