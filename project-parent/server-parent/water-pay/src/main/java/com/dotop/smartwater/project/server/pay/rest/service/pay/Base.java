package com.dotop.smartwater.project.server.pay.rest.service.pay;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: project-parent
 * @description: 基础方法

 * @create: 2019-07-22 10:36
 **/
public class Base {
    protected void string(String name, String str) {
        if (StringUtils.isBlank(str)) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, name + "不能为空");
        }
    }
}
