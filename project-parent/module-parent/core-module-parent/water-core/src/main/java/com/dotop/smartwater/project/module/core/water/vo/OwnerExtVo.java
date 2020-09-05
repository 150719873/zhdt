package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerExtVo extends BaseVo {
    private String ownerId;

    private String ownerType;
    //业主类型名称
    private String ownerTypeName;

    private String bankName;

    private String bankAccount;

    private String accountName;

    private String contact;
}