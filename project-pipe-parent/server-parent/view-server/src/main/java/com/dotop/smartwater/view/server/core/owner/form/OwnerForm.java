package com.dotop.smartwater.view.server.core.owner.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OwnerForm extends BasePipeForm {

    /**
     * 业主id
     */
    private String ownerId;
    /**
     * 业主编号
     */
    private String userNo;
    /**
     * 业主名称
     */
    private String userName;
    /**
     * 关联设备id
     */
    private String devId;
    /**
     * 区域id
     */
    private String communityId;

    private Integer offset;
    private Integer limit;
    private Date curr;
    private String userBy;
    private Integer isDel;
    private Integer newIsDel;
    private Boolean ifSort;
}
