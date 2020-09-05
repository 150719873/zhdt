package com.dotop.pipe.server.rest.service.brustpipe;

import com.dotop.pipe.server.rest.service.collection.CollectionDeviceController;
import com.dotop.pipe.core.form.BrustPipeForm;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.pipe.web.api.factory.brustpipe.IBrustPipeFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 爆管管理入口层辑接口
 *
 *
 */
@RestController
@RequestMapping("/brustpipe")
public class BrustPipeController implements BaseController<BrustPipeForm> {

    private static final Logger logger = LogManager.getLogger(CollectionDeviceController.class);

    @Resource
    private IBrustPipeFactory iBrustPipeFactory;



    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@RequestBody BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        BrustPipeVo brustPipeVo = iBrustPipeFactory.get(brustPipeForm);
        return resp(brustPipeVo);

    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        Pagination<BrustPipeVo> page = iBrustPipeFactory.page(brustPipeForm);
        return resp(page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        BrustPipeVo brustPipeVo = iBrustPipeFactory.add(brustPipeForm);
        return resp(brustPipeVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(@RequestBody BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        String del = iBrustPipeFactory.del(brustPipeForm);
        return resp(del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        BrustPipeVo brustPipeVo = iBrustPipeFactory.edit(brustPipeForm);
        return resp(brustPipeForm);
    }

    @PostMapping(value = "/edit/status", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editStatus(@RequestBody BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        BrustPipeVo brustPipeVo = iBrustPipeFactory.editStatus(brustPipeForm);
        return resp(brustPipeVo);
    }


    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody BrustPipeForm brustPipeFor) throws FrameworkRuntimeException {
        List<BrustPipeVo> list = iBrustPipeFactory.list(brustPipeFor);
        return resp(list);
    }


}
