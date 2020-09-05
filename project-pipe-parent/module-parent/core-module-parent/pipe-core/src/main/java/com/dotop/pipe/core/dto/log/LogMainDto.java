package com.dotop.pipe.core.dto.log;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogMainDto extends BasePipeDto {

    /**
     * 主键
     */
    private String id;

    /**
     * 编号
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 颜色
     */
    private String color;

    /**
     * 是否显示
     */
    private Integer isShow;
}
