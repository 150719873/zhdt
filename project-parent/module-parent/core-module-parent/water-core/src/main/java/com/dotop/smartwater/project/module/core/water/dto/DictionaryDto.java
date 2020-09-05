package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictionaryDto extends BaseDto {
    private String dictionaryId;

    private String dictionaryCode;

    private String dictionaryType;

    private String dictionaryName;

    private String dictionaryValue;

    private String remark;

    private String createBy;

    private Date createDate;

    private String lastBy;

    private Date lastDate;

    private Integer isDel;

}