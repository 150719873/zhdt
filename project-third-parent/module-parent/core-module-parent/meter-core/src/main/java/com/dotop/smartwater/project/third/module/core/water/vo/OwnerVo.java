package com.dotop.smartwater.project.third.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业主
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OwnerVo  extends BaseVo {
    /**
     * 主键id
     */
    private String id;

    /**
     *第三方业主id
     */
    private String thirdid;

    /**
     *业主id
     */
    private String ownerid;

    /**
     *用户编号
     */
    private String userno;

    /**
     *业主名称
     */
    private String username;

    /**
     *业主地址
     */
    private String useraddr;

    /**
     * 业主电话
     */
    private String userphone;

    /**
     * 业主身份证
     */
    private String cardid;

    /**
     *备注
     */
    private String remark;

    /**
     *绑定的设备id
     */
    private String devid;

    /**
     *json
     */
    private String json;

    private DeviceVo device;
}
