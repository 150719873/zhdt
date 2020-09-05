package com.dotop.smartwater.project.third.concentrator.core.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OperLogBo extends BaseBo {

    /**
     * 主键
     */
    private String id;
    /**
     * 操作日志
     */
    private String msg;

}
