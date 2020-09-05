package com.dotop.pipe.core.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogPointForm extends PointForm {

    /**
     * 主键
     */
    private String id;

    /**
     * 版控主表id
     */
    private String logMainId;
}
