package com.dotop.pipe.core.vo.log;

import com.dotop.pipe.core.vo.point.PointMapVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogPointMapVo extends PointMapVo {

    /**
     * 主键
     */
    private String id;

    /**
     * 版控主表id
     */
    private String logMainId;
}
