package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerExtDto extends BaseDto {
    private String ownerId;
    private String userno;

    private String ownerType;

    private String bankName;

    private String bankAccount;

    private String accountName;

    private String contact;
}