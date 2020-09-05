package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description: 对接第三方阀控命令

 * @create: 2019-06-10 09:11
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class OperationBo extends BaseBo {
    //类型 2开阀 1关阀
    private Integer type;
    //水表号
    private String devNo;

    private Integer expire;

    private String userid;

    private String username;
}
