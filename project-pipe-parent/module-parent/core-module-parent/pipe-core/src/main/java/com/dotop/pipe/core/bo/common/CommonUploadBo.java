package com.dotop.pipe.core.bo.common;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonUploadBo extends BasePipeBo {
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
