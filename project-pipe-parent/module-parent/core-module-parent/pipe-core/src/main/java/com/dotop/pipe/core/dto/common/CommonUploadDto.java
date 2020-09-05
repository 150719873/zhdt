package com.dotop.pipe.core.dto.common;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonUploadDto extends BasePipeDto {
    private String id;
    private String type;
    private String originalName;
    private String fileName;
    private String filePath;
    private String thirdId;
    private String createBy;
    private Date createDate;
    private String lastBy;
    private Date lastDate;
    private Integer isDel;
    private byte[] content;
}
