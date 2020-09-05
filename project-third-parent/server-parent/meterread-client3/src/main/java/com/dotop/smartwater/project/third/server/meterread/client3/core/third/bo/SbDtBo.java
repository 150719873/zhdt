package com.dotop.smartwater.project.third.server.meterread.client3.core.third.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SbDtBo extends BaseBo {

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
