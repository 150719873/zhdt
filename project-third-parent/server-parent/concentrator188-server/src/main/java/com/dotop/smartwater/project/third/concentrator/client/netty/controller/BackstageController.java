package com.dotop.smartwater.project.third.concentrator.client.netty.controller;

import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.code.ResultCode;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.Demo;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.global.GlobalContext;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.service.OperationService;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: dingtong
 * @description: 服务端操作接口
 *
 * @create: 2019-06-12 09:25
 **/
@RestController
@RequestMapping("/back")
public class BackstageController extends Base {

    @Autowired
    private OperationService operationService;

    /**
     * 后面直接发
     */
    @PostMapping(value = "/send", produces = GlobalContext.PRODUCES)
    public String send(@RequestBody Demo demo) throws InterruptedException {
        if (StrUtil.isBlank(demo.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }

        return respStringOk(operationService.send(demo));
    }


}
