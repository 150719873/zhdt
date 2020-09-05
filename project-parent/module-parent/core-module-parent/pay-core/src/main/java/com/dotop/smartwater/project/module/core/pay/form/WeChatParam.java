package com.dotop.smartwater.project.module.core.pay.form;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: project-parent
 * @description:

 * @create: 2019-07-24 14:18
 **/
@Getter
@Setter
public class WeChatParam extends AttachParam{
    private String amount;
    private String ip;
    private String payCard;
    private String outTradeNo;
    private String openid;
    private String tradeName;
}
