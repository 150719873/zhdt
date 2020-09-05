package com.dotop.smartwater.project.third.module.client.fegin;

import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 对接内部系统调用接口
 *
 *
 */
@FeignClient(name = "water-cas", fallbackFactory = CasHystrixFallbackFactory.class, path = "/water-cas/")
public interface ICasFeginClient {

    /**
     * 验证授权接口
     */
    @PostMapping(value = "/auth/UserController/login", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String login(@RequestBody UserForm user);

}
