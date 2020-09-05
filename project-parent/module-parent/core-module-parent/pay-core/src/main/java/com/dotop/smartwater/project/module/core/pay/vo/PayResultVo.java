package com.dotop.smartwater.project.module.core.pay.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @program: project-parent
 * @description:

 * @create: 2019-07-22 16:02
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class PayResultVo extends BaseVo {
    private String return_code;
    private String return_msg;
    private Map<String,String> return_data;
    private PushVo pushVo;
}
