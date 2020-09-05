package com.dotop.smartwater.project.module.core.third.vo.pipe;

import com.dotop.smartwater.project.module.core.third.form.iot.PushDataForm;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description:

 * @create: 2020-03-11 12:12
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class PipePushVo {
    private DeviceUplinkBo form;
    private String devno;
    private String enterptiseid;
}
