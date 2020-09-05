package com.dotop.pipe.core.dto.decive;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/11/12.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceUpDownStreamDto extends BasePipeDto {
    private String id;
    private String deviceId;
    private List<String> parentDeviceIds;
    private String type;
    private String areaId;
    private String name;
    private String code;
    private String alarmProperties;
}
