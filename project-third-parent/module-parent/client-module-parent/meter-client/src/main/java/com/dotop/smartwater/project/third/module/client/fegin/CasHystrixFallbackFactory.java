package com.dotop.smartwater.project.third.module.client.fegin;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CasHystrixFallbackFactory implements FallbackFactory<ICasFeginClient> {

    private static final Logger LOGGER = LogManager.getLogger(CasHystrixFallbackFactory.class);

    @Override
    public ICasFeginClient create(Throwable ex) {
        return new ICasFeginClient() {

            @Override
            public String login(UserForm user) {
                Map<String, String> map = new HashMap<>(1);
                return JSONUtils.toJSONString(map);
            }

        };
    }

}
