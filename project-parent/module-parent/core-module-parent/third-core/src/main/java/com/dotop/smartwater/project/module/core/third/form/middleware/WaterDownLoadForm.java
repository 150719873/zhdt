package com.dotop.smartwater.project.module.core.third.form.middleware;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description: 中间平台命令下发

 * @create: 2019-10-11 14:06
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterDownLoadForm extends BaseForm {
    private String devid;
    /**命令类型 */
    private Integer command;
    private String water;
    /**设置上报周期 */
    private String cycle;

    /**设置生命周期 */
    private String life;

    /**设置定时上报 */
    private String period;
}
