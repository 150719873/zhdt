package com.dotop.pipe.core.bo.log;

import com.dotop.pipe.core.dto.point.PointMapDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogPointMapBo extends PointMapDto {

    /**
     * 主键
     */
    private String id;

    /**
     * 版控主表id
     */
    private String logMainId;
}
