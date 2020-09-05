package com.dotop.smartwater.view.server.core.area.vo;

import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import com.dotop.smartwater.view.server.core.device.vo.DeviceDataVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AreaVo extends BasePipeVo {

    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域编号
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 区域描述
     */
    private String des;
    /**
     * 是否根节点(0:非叶子;1:非叶子)
     */
    private Integer isLeaf = PipeConstants.AREA_IS_LEAF;
    /**
     * 父节点
     */
    private String parentCode = PipeConstants.AREA_PARENT_CODE;

    /**
     * 区域坐标的exten字段
     */
    private String extent;

    private DeviceDataVo deviceData;
}
