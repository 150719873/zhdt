package com.dotop.smartwater.project.server.water.rest.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.tool.IDictionaryChildFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DictionaryChildForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: project-parent
 * @description: 字典管理

 * @create: 2019-03-04 09:05
 **/
@RestController

@RequestMapping("/dictionary/child")
public class DictionaryChildController implements BaseController<DictionaryChildForm> {

    @Autowired
    private IDictionaryChildFactory iDictionaryChildFactory;

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody DictionaryChildForm dictionaryChildForm) {
        iDictionaryChildFactory.add(dictionaryChildForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);

    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody DictionaryChildForm dictionaryChildForm) {
        iDictionaryChildFactory.edit(dictionaryChildForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);

    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody DictionaryChildForm dictionaryChildForm) {
        iDictionaryChildFactory.del(dictionaryChildForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);

    }

    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody DictionaryChildForm dictionaryChildForm) {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iDictionaryChildFactory.get(dictionaryChildForm));

    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody DictionaryChildForm dictionaryChildForm) {
        Pagination<DictionaryChildVo> list = iDictionaryChildFactory.page(dictionaryChildForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);

    }
}
