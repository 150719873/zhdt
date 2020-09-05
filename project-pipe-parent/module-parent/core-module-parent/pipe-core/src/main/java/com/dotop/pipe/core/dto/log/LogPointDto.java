package com.dotop.pipe.core.dto.log;

import com.dotop.pipe.core.dto.point.PointDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogPointDto extends PointDto {

    /**
     * 主键
     */
    private String id;

    /**
     * 版控主表id
     */
    private String logMainId;
}
