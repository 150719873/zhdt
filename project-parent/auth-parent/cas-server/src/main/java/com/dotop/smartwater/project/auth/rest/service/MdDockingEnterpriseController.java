package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.auth.api.IMdDockingEnterpriseFactory;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingEnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingEnterpriseVo;
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

@RequestMapping("/auth/md_docking_enterprise")
public class MdDockingEnterpriseController extends FoundationController implements BaseController<MdDockingEnterpriseForm> {

    private static final Logger LOGGER = LogManager.getLogger(MdDockingEnterpriseController.class);

    @Autowired
    private IMdDockingEnterpriseFactory iMdDockingEnterpriseFactory;

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody MdDockingEnterpriseForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        iMdDockingEnterpriseFactory.add(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody MdDockingEnterpriseForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        iMdDockingEnterpriseFactory.edit(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody MdDockingEnterpriseForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        iMdDockingEnterpriseFactory.del(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody MdDockingEnterpriseForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        return resp(ResultCode.Success, ResultCode.SUCCESS, iMdDockingEnterpriseFactory.get(form));
    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody MdDockingEnterpriseForm form) {
        LOGGER.info(LogMsg.to("form ", form));

        VerificationUtils.integer("pageCount", form.getPageCount());
        VerificationUtils.integer("page", form.getPage());

        Pagination<MdDockingEnterpriseVo> pagination = iMdDockingEnterpriseFactory.page(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(@RequestBody MdDockingEnterpriseForm form) {
        LOGGER.info(LogMsg.to("form ", form));

        List<MdDockingEnterpriseVo> list = iMdDockingEnterpriseFactory.list(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }


    @PostMapping(value = "/enterpriseList", produces = GlobalContext.PRODUCES)
    public String enterpriseList() {
        List<EnterpriseVo> list = iMdDockingEnterpriseFactory.enterpriseList();
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }
}
