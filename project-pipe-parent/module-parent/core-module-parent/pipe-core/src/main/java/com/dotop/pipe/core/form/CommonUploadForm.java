package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonUploadForm extends BasePipeForm {
    private List<String> ids;
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
