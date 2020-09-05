package com.dotop.smartwater.view.server.core.owner.vo;


import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OwnerVo extends BasePipeVo {

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
}
