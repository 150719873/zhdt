package com.dotop.smartwater.project.third.server.meterread.client3.core.third;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
