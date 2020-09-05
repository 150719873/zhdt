package com.dotop.smartwater.project.module.core.third.form.middleware;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @program: project-parent
 * @description: 中间平台上报水量

 * @create: 2019-10-11 14:06
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterUploadForm extends BaseForm {
    private String devid;
    private String deveui;
    private String water;
    /**阀门状态 */
    private Integer tapstatus;
    private Date uplinkDate;

    private Long rssi;
    private Double lsnr;
}
