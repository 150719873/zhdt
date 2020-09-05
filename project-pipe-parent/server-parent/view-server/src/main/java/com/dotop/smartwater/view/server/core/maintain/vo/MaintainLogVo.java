package com.dotop.smartwater.view.server.core.maintain.vo;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MaintainLogVo extends BasePipeVo {

    /**
     * 设备id
     */
    private String id;

    /**
     * 产品类别
     */
    private String category;
    /**
     * 产品类型
     */
    private String type;

    private String content;

    private String address;

}