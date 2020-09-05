package com.dotop.smartwater.project.third.server.meterread.client3.core.third.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SbDtDto extends BaseDto {

    private Integer id;

    private String meterId;

    private String userCode;

    private Date readDate;

    private Integer readNumber;

    private String valveStatus;

    private String valveFlag;

    private String processFlag;

    private String clientId;
}
