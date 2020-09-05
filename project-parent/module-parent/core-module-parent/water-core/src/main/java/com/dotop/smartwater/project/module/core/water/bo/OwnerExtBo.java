package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerExtBo extends BaseBo {
    private String ownerId;

    private String ownerType;

    private String bankName;

    private String bankAccount;

    private String accountName;

    private String contact;
}