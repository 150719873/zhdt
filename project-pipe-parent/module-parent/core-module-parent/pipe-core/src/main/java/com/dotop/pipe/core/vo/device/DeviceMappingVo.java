package com.dotop.pipe.core.vo.device;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceMappingVo extends BasePipeVo {

    private String mapId;
    private String deviceId;
    private String otherId;
    private String direction;
    private String level;

    private DeviceVo deviceVo;
    private DeviceVo otherDeviceVo;
    private List<DeviceVo> otherDeviceVos;
}
