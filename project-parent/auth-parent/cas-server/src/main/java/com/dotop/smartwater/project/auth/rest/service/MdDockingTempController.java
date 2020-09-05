package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.auth.api.IMdDockingTempFactory;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingTempForm;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;
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

@RequestMapping("/auth/md_docking_temp")
public class MdDockingTempController extends FoundationController implements BaseController<MdDockingTempForm> {

    private static final Logger LOGGER = LogManager.getLogger(MdDockingTempController.class);

    @Autowired
    private IMdDockingTempFactory iMdDockingTempFactory;

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody MdDockingTempForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("type", form.getType());
        VerificationUtils.string("factory", form.getFactory());
        iMdDockingTempFactory.add(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody MdDockingTempForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        iMdDockingTempFactory.edit(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody MdDockingTempForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        iMdDockingTempFactory.del(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody MdDockingTempForm form) {
        LOGGER.info(LogMsg.to("form ", form));
        VerificationUtils.string("id", form.getId());
        return resp(ResultCode.Success, ResultCode.SUCCESS, iMdDockingTempFactory.get(form));
    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody MdDockingTempForm form) {
        LOGGER.info(LogMsg.to("form ", form));

        VerificationUtils.integer("pageCount", form.getPageCount());
        VerificationUtils.integer("page", form.getPage());

        Pagination<MdDockingTempVo> pagination = iMdDockingTempFactory.page(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(@RequestBody MdDockingTempForm form) {
        LOGGER.info(LogMsg.to("form ", form));

        List<MdDockingTempVo> list = iMdDockingTempFactory.list(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }

}
