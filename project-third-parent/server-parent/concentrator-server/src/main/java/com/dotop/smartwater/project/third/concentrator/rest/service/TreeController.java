package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.ITreeFactory;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.form.BookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tree")
public class TreeController implements BaseController<BookForm> {

    @Autowired
    private ITreeFactory iTreeFactory;

    @PostMapping(value = "", produces = GlobalContext.PRODUCES)
    public String tree() {
        Map<String, List<ConcentratorVo>> treeMap = iTreeFactory.tree();
        return resp(ResultCode.Success, ResultCode.SUCCESS, treeMap);
    }

}
