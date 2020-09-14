package com.dotop.smartwater.project.third.concentrator.client.netty.dc.model;

import lombok.Data;

/**
 *
 */
@Data
public class MsgEntity {
    private String code;
    private String msg;
    private Object data;
}
