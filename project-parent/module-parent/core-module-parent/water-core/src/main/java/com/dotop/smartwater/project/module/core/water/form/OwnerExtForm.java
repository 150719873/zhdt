package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerExtForm extends BaseForm {
    private String ownerId;

    private String ownerType;

    private String bankName;

    private String bankAccount;

    private String accountName;

    private String contact;
}