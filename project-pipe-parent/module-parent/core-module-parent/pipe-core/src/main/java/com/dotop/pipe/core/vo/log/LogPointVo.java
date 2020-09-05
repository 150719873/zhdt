package com.dotop.pipe.core.vo.log;

import com.dotop.pipe.core.vo.point.PointVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogPointVo extends PointVo {

    /**
     * 主键
     */
    private String id;

    /**
     * 版控主表id
     */
    private String logMainId;
}
