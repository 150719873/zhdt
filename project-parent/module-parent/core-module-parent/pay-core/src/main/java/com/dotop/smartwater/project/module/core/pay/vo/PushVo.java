package com.dotop.smartwater.project.module.core.pay.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: project-parent
 * @description: 推送到别的服务器的数据格式

 * @create: 2019-07-29 16:12
 **/
@Getter
@Setter
public class PushVo {
    private String tradeNumber;
    private String payNumber;
    private String tradeName;
    private String amount;
    private String extra;
    private String mode;
    private String status;
    private String description;
    private String payTime;
    private String enterpriseid;
}
