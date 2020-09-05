package com.dotop.pipe.core.bo.log;

import com.dotop.pipe.core.dto.point.PointDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogPointBo extends PointDto {

    /**
     * 主键
     */
    private String id;

    /**
     * 版控主表id
     */
    private String logMainId;
}
