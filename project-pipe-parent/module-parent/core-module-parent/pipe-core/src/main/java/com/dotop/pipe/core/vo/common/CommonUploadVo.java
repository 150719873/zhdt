package com.dotop.pipe.core.vo.common;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommonUploadVo extends BasePipeVo {

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
