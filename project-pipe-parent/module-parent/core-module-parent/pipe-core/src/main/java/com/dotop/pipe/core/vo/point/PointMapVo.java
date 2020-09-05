package com.dotop.pipe.core.vo.point;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PointMapVo extends BasePipeVo {

    // 主键
    private String mapId;

    // 坐标主键
    private String pointId;

    // 设备主键
    private String deviceId;

    // 坐标主键
    private List<String> pointIds;

    // 设备主键
    private List<String> deviceIds;
}
