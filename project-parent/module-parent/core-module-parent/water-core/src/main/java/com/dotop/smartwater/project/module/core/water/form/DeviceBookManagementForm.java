package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBookManagementForm extends BaseForm {
    private String bookId;

    private String bookNum;

    private String bookUserId;

    private String bookType;

    private String userName;

    private String workNum;
}