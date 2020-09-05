package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.auth.api.IMdDockingFactory;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingEnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingForm;
import com.dotop.smartwater.project.module.core.auth.vo.*;
import com.dotop.smartwater.project.module.core.auth.vo.md.MdDockingExtForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 * @date 2019年5月9日
 * @description
 */
@RestController

@RequestMapping("/auth/md_docking")
public class MdDockingController extends FoundationController implements BaseController<MdDockingForm> {

    private static final Logger LOGGER = LogManager.getLogger(MdDockingController.class);

    @Autowired
    private IMdDockingFactory iMdDockingFactory;

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody MdDockingForm form) {
        LOGGER.info(LogMsg.to("form ", form));

        VerificationUtils.string("id", form.getId());
        iMdDockingFactory.add(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody MdDockingForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        iMdDockingFactory.edit(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody MdDockingForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        iMdDockingFactory.del(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody MdDockingForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        return resp(ResultCode.Success, ResultCode.SUCCESS, iMdDockingFactory.get(form));
    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody MdDockingForm form) {
        LOGGER.info(LogMsg.to("form ", form));

        VerificationUtils.integer("pageCount", form.getPageCount());
        VerificationUtils.integer("page", form.getPage());

        Pagination<MdDockingVo> pagination = iMdDockingFactory.page(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(@RequestBody MdDockingForm form) {
        LOGGER.info(LogMsg.to("form ", form));

        List<MdDockingVo> list = iMdDockingFactory.list(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }


    @PostMapping(value = "/load", produces = GlobalContext.PRODUCES)
    public String load(@RequestBody MdDockingEnterpriseForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        VerificationUtils.string("enterpriseId", form.getEnterpriseid());
        return resp(ResultCode.Success, ResultCode.SUCCESS, iMdDockingFactory.load(form));
    }

    @PostMapping(value = "/save", produces = GlobalContext.PRODUCES)
    public String save(@RequestBody MdDockingExtForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("mdeId", form.getMdeId());
        iMdDockingFactory.save(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @PostMapping(value = "/loadConfigList", produces = GlobalContext.PRODUCES)
    public String loadConfigList(@RequestBody MdDockingEnterpriseForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("enterpriseId", form.getEnterpriseid());
        return resp(ResultCode.Success, ResultCode.SUCCESS, iMdDockingFactory.loadConfigList(form));
    }
}
